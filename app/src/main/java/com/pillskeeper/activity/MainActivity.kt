package com.pillskeeper.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.pillskeeper.R
import com.pillskeeper.activity.friend.FriendListActivity
import com.pillskeeper.activity.pills.PillsListActivity
import com.pillskeeper.data.LocalMedicine
import com.pillskeeper.data.ReminderMedicine
import com.pillskeeper.datamanager.DatabaseManager
import com.pillskeeper.datamanager.LocalDatabase
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.enums.DaysEnum
import com.pillskeeper.enums.MedicineTypeEnum
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.system.exitProcess

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

        //TODO DEBUG - to be removed
        funTest()

        readFirstLogin()
    }

    private fun readFirstLogin() {
        //val userN = LocalDatabase.readUsername()
        if (/*FirebaseAuth.getInstance().currentUser != null*/ true) {
            val it = Intent(this, PillsListActivity::class.java)
            startActivity(it)
            finish()
        } else {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
            finish()
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

}
