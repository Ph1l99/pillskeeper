package com.pillskeeper.activity.medicine.medicineformfragments

import android.app.Activity
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.common.hash.Hashing
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pillskeeper.data.LocalMedicine
import com.pillskeeper.data.ReminderMedicine
import com.pillskeeper.data.ReminderMedicineSort
import com.pillskeeper.data.RemoteMedicine
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.enums.MedicineTypeEnum
import com.pillskeeper.notifier.NotifyPlanner
import com.pillskeeper.utility.Utils
import java.nio.charset.StandardCharsets
import java.util.*

class FormAdapter(fm: FragmentManager, private val intent: Intent, private val viewPager: PillsViewPager): FragmentPagerAdapter(fm) {



    companion object{
        const val FORM_ONE = 0
        const val FORM_TWO = 1
        const val FORM_THREE = 2
        const val FORM_EDIT = 3

        lateinit var pillName: String
        lateinit var medicineType: MedicineTypeEnum
        var totalQuantity: Float = 0.0F
        var remainingQuantity: Float = 0.0F

        private const val PATH_MEDICINES = "medicines"

        var reminderList: LinkedList<ReminderMedicine>? = null
        var remoteMedicine: RemoteMedicine? = null
        var localMedicine: LocalMedicine? = null
        lateinit var stdLayout: Drawable

        lateinit var formActivity: Activity


        fun closeForm(){
            formActivity.finish()
        }

        fun addOrEditMedicine() {
            val newMed = LocalMedicine(
                pillName.toLowerCase(Locale.ROOT),
                medicineType,
                totalQuantity,
                remainingQuantity,
                null,
                hashValue(pillName, medicineType)
            )
            if(UserInformation.addNewMedicine(newMed)) {

                val listMed: LinkedList<LocalMedicine> = LinkedList()
                listMed.add(newMed)
                val reminderListNormalized: LinkedList<ReminderMedicineSort> = Utils.getListReminderNormalized(listMed)

                reminderListNormalized.forEach {
                    NotifyPlanner.planSingleAlarm(
                        formActivity,
                        formActivity.getSystemService(Context.ALARM_SERVICE) as AlarmManager,
                        it
                    )
                }

                writeMedOnDB(
                    RemoteMedicine(
                        pillName,
                        hashValue(pillName, medicineType),
                        medicineType
                    )
                )
            }
        }


        private fun hashValue(name: String, typeEnum: MedicineTypeEnum): String {
            return (Hashing.goodFastHash(64)).newHasher()
                .putString(name + typeEnum, StandardCharsets.UTF_8).hash().toString()
        }

        private fun writeMedOnDB(remoteMedicine: RemoteMedicine) {
            val databaseReference = Firebase.database.reference
            databaseReference.child(PATH_MEDICINES).child(remoteMedicine.id)
                .setValue(remoteMedicine)
        }
    }

    override fun getItem(position: Int): Fragment {
        return when(position) {
            FORM_ONE -> FormOneFragment(intent, viewPager)
            FORM_TWO -> FormTwoFragment(viewPager)
            FORM_THREE -> FormThreeFragment(viewPager)
            FORM_EDIT -> FormEditingFragment()
            else -> FormOneFragment(intent, viewPager)
        }
    }

    override fun getCount(): Int {
        return 4
    }

}