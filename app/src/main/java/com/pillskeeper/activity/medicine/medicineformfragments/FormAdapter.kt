package com.pillskeeper.activity.medicine.medicineformfragments

import android.app.Activity
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.common.hash.Hashing
import com.pillskeeper.activity.medicine.reminder.reminderformfragments.*
import com.pillskeeper.data.LocalMedicine
import com.pillskeeper.data.ReminderMedicine
import com.pillskeeper.data.RemoteMedicine
import com.pillskeeper.datamanager.FirebaseDatabaseManager
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.enums.DaysEnum
import com.pillskeeper.enums.MedicineTypeEnum
import com.pillskeeper.interfaces.Callback
import com.pillskeeper.notifier.NotifyPlanner
import com.pillskeeper.utility.Utils
import com.pillskeeper.utility.Utils.dataNormalizationLimit
import java.nio.charset.StandardCharsets
import java.util.*

class FormAdapter(
    fm: FragmentManager,
    private val intent: Intent,
    private val viewPager: NoSlideViewPager
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        const val FORM_NAME_TYPE = 0
        const val FORM_QUANTITY = 1
        const val FORM_SAVE_OR_REMINDER = 2
        const val FORM_EDIT = 3
        const val FORM_ONE_DAY_REMINDER_TIME = 4
        const val FORM_ONE_DAY_REMINDER_QUANTITY = 5
        const val FORM_SEQ_DATE_REMINDER = 6
        const val FORM_SEQ_TIME_REMINDER = 7
        const val FORM_SEQ_QUANTITY_REMINDER = 8

        var pillName: String? = null
        var medicineType: MedicineTypeEnum? = null
        var totalQuantity: Float = 0.0F
        var remainingQuantity: Float = 0.0F
        var reminderList: LinkedList<ReminderMedicine>? = null

        var remoteMedicine: RemoteMedicine? = null
        var localMedicine: LocalMedicine? = null

        var formActivity: Activity? = null

        var reminderHour: Int = 0
        var reminderMinute: Int = 0
        var reminderQuantity: Float = 0.0F
        var reminderNotes: String? = ""
        var startDay: Date? = null
        var finishDay: Date? = null
        var days: LinkedList<DaysEnum>? = null

        var isANewMedicine: Boolean = true
        var isAReminderEditing: Boolean = false

        fun resetForm() {
            formActivity = null
            localMedicine = null
            remoteMedicine = null
            medicineType = null
            pillName = null
            totalQuantity = 0.0F
            remainingQuantity = 0.0F
            reminderList = null
            isANewMedicine = true
            resetReminder()
        }

        fun resetReminder() {
            reminderHour = 0
            reminderMinute = 0
            reminderQuantity = 0.0F
            reminderNotes = ""
            startDay = null
            finishDay = null
            days = null
            isAReminderEditing = false
        }

        fun closeForm() {
            formActivity?.finish()
        }

        fun addReminder(reminder: ReminderMedicine) {
            if (reminderList == null)
                reminderList = LinkedList()
            reminderList!!.add(reminder)
        }

        fun addNewMedicine() {
            val newMed = LocalMedicine(
                pillName!!.toLowerCase(Locale.ROOT),
                medicineType!!,
                totalQuantity,
                remainingQuantity,
                reminderList,
                hashValue(pillName!!, medicineType!!)
            )
            if (UserInformation.addNewMedicine(newMed)) {

                Utils.getListReminderNormalized(
                    newMed
                ).filter { it.reminder.startingDay < Date(dataNormalizationLimit()) }
                    .forEach {
                    NotifyPlanner.planSingleAlarm(
                        formActivity!!,
                        formActivity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager,
                        it
                    )
                }

                writeMedOnDB(
                    RemoteMedicine(
                        pillName!!,
                        hashValue(pillName!!, medicineType!!),
                        medicineType!!
                    )
                )
            }
        }

        /**
         * Method for generating the ID of a RemoteMedicine. It uses SHA-1 hashing function
         */
        private fun hashValue(name: String, typeEnum: MedicineTypeEnum): String {
            return (Hashing.sha1()).newHasher()
                .putString(name + typeEnum, StandardCharsets.UTF_8).hash().toString()
        }

        /**
         * Method for writing the medicine on Firebase DB
         */
        private fun writeMedOnDB(remoteMedicine: RemoteMedicine) {
            FirebaseDatabaseManager.writeMedicine(
                remoteMedicine,
                object : Callback {
                    override fun onSuccess(res: Boolean) {
                        println("ok")
                    }

                    override fun onError() {
                        println("ok")
                    }
                }
            )
        }


    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            FORM_NAME_TYPE -> FormNameTypeFragment(intent, viewPager)
            FORM_QUANTITY -> FormQuantityFragment(viewPager)
            FORM_SAVE_OR_REMINDER -> FormSaveOrReminderFragment(viewPager)
            FORM_EDIT -> FormEditingFragment()
            FORM_ONE_DAY_REMINDER_TIME -> FormReminderOneDayTimeFrag(viewPager)
            FORM_ONE_DAY_REMINDER_QUANTITY -> FormReminderOneDayQuantityFrag(viewPager, pillName)
            FORM_SEQ_DATE_REMINDER -> FormReminderSeqDateFrag(viewPager)
            FORM_SEQ_TIME_REMINDER -> FormReminderSeqTimeFrag(viewPager)
            FORM_SEQ_QUANTITY_REMINDER -> FormReminderSeqQuantityFrag(viewPager)
            else -> FormNameTypeFragment(intent, viewPager)
        }
    }

    override fun getCount(): Int {
        return 9
    }

}