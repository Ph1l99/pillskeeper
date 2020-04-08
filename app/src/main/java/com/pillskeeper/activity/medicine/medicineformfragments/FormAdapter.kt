package com.pillskeeper.activity.medicine.medicineformfragments

import android.app.Activity
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.common.hash.Hashing
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pillskeeper.activity.medicine.reminderformfragments.FormReminderOneDayFrag
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

class FormAdapter(fm: FragmentManager, private val intent: Intent, private val viewPager: MedicineViewPager): FragmentPagerAdapter(fm) {

    //TODO MISSING EDIT REMINDER and relative INSERT FLOW(alarm edit flow)
    //TODO bisogna resettare questa classe statica(o renderla non statica) quadno si finisce di salvare la medicina (altrimenti rimangono le info salvate)
    // si potrebbe fare in apertura del form ogni volta, oppure in chiusura

    companion object{
        const val FORM_NAME_TYPE = 0
        const val FORM_QUANTITY = 1
        const val FORM_SAVE_OR_REMINDER = 2
        const val FORM_EDIT = 3
        const val FORM_ONE_DAY_REMINDER = 4

        lateinit var pillName: String
        lateinit var medicineType: MedicineTypeEnum
        var totalQuantity: Float = 0.0F
        var remainingQuantity: Float = 0.0F
        var reminderList: LinkedList<ReminderMedicine>? = null

        private const val PATH_MEDICINES = "medicines"
        var remoteMedicine: RemoteMedicine? = null
        var localMedicine: LocalMedicine? = null

        lateinit var formActivity: Activity


        fun closeForm(){
            formActivity.finish()
        }

        fun addNewMedicine() {
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
            FORM_NAME_TYPE -> FormNameTypeFragment(intent, viewPager)
            FORM_QUANTITY -> FormQuantityFragment(viewPager)
            FORM_SAVE_OR_REMINDER -> FormSaveOrReminderFragment(viewPager)
            FORM_EDIT -> FormEditingFragment()
            FORM_ONE_DAY_REMINDER -> FormReminderOneDayFrag(viewPager)
            else -> FormNameTypeFragment(intent, viewPager)
        }
    }

    override fun getCount(): Int {
        return 5
    }

}