package com.pillskeeper.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.pillskeeper.R
import com.pillskeeper.activity.pills.PillsListActivity
import com.pillskeeper.data.Appointment
import com.pillskeeper.data.LocalMedicine
import com.pillskeeper.data.ReminderMedicine
import com.pillskeeper.datamanager.DatabaseManager
import com.pillskeeper.datamanager.LocalDatabase
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.enums.DaysEnum
import com.pillskeeper.enums.MedicineTypeEnum
import com.pillskeeper.utility.Utils
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {

    companion object {
        const val START_FIRST_LOGIN_ACTIVITY_CODE = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        LocalDatabase.sharedPref = this.getPreferences(Context.MODE_PRIVATE)

        UserInformation //necessario per inizializzare i componenti interni
        UserInformation.context = this
        FirebaseApp.initializeApp(this)
        DatabaseManager.obtainRemoteDatabase()

        Utils.stdLayout = EditText(this).background

        //TODO DEBUG - to be removed
        funTest()

        readFirstLogin()


    }

    //TODO risolvere questa parte @Phil
    private fun readFirstLogin() {
        //val userN = LocalDatabase.readUsername()
        if (/*FirebaseAuth.getInstance().currentUser == null*/ false) {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
            finish()
        } else {
            initLists()
        }
    }

    private fun funTest() {

        val days1 :LinkedList<DaysEnum> = LinkedList()
        days1.add(DaysEnum.MON)
        days1.add(DaysEnum.THU)
        days1.add(DaysEnum.SUN)

        val days2 :LinkedList<DaysEnum> = LinkedList()
        days1.add(DaysEnum.MON)

        val reminders = LinkedList<ReminderMedicine>()
        reminders.add(ReminderMedicine(1.5F, 0, 19, Date(), days1, Date(), null))
        reminders.add(ReminderMedicine(1F, 0, 19, Date(), days2, Date(), null))
        UserInformation.addNewMedicine(
            LocalMedicine(
                "Tachipirina",
                MedicineTypeEnum.PILLS,
                24F,
                24F,
                reminders,
                "Tachipirina"
            )
        )


        val reminders2 = LinkedList<ReminderMedicine>()
        reminders2.add(ReminderMedicine(1.5F, 0, 19, Date() ,days1, Date(), null))
        reminders2.add(ReminderMedicine(1F, 0, 19, Date(), days2, Date(), null))
        UserInformation.addNewMedicine(
            LocalMedicine(
                "Aulin",
                MedicineTypeEnum.PILLS,
                24F,
                24F,
                null,
                "Aulin"
            )
        )
        UserInformation.addNewReminderList("Aulin", reminders2)


        LocalDatabase.saveMedicineList(UserInformation.medicines)

    }

    private fun initLists(){

        /*Appointment list*/
        UserInformation.appointments.sortWith(compareBy<Appointment> {it.date}.thenBy { it.hours }.thenBy { it.minutes })
        val arrayAdapterAppointments = LinkedList<String>()
        UserInformation.appointments.forEach { arrayAdapterAppointments.add(it.name) }
        appointmentList.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayAdapterAppointments)

        /*Reminder List*/
        val reminderList: HashMap<String,ReminderMedicine> = HashMap()
        UserInformation.medicines.forEach { it.reminders?.forEach { rem -> reminderList[it.name] = rem } }



        .adapter = adapter

    }

}
