package com.pillskeeper.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pillskeeper.R
import com.pillskeeper.data.Friend
import com.pillskeeper.data.Medicine
import com.pillskeeper.datamanager.LocalDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.system.exitProcess
import com.pillskeeper.data.Reminder
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.enums.RelationEnum
import java.util.*

class MainActivity : AppCompatActivity() {

    private var isFirstLogin : Boolean = true
    private var username : String? = null

    companion object {
        const val START_FIRST_LOGIN_ACTIVITY_CODE = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        LocalDatabase.sharedPref = this.getPreferences(Context.MODE_PRIVATE)

        UserInformation

        //TODO DEBUG - to be removed
        funTest()

        readFirstLogin()

        if(isFirstLogin){
            val activity = Intent(this, FirstLoginActivity::class.java)
            startActivityForResult(activity,START_FIRST_LOGIN_ACTIVITY_CODE)
        } else
            welcomeTextView.text = "Benvenuto $username"

        //TODO for debug, to be removed
        buttonResetLocalMemory.setOnClickListener {
            LocalDatabase.resetMemory()
            exitProcess(-1)
        }
    }

    private fun readFirstLogin(){
        val userN = LocalDatabase.readUsername()
        if (userN != null) {
            if(userN.isNotEmpty() || userN != "") {
                username = userN
                isFirstLogin = false
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == START_FIRST_LOGIN_ACTIVITY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val username = data!!.getStringExtra("username")
                welcomeTextView.text = "Benvenuto $username"
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun funTest(){


        val reminders = LinkedList<Reminder>()
        reminders.add(Reminder(1.5F,0,19,"Lun-Mar-Mer", Date(),null))
        reminders.add(Reminder(1F,0,19,"Ven",Date(),null))
        UserInformation.addNewMedicine(Medicine("Tachipirina",24F,24F,reminders))


        val reminders2 = LinkedList<Reminder>()
        reminders2.add(Reminder(1.5F,0,19,"Lun-Mer", Date(),null))
        reminders2.add(Reminder(1F,0,19,"Wed",Date(),null))
        UserInformation.addNewMedicine(Medicine("Aulin",24F,24F,null))
        UserInformation.addNewReminderList("Aulin",reminders2)


        LocalDatabase.saveMedicineList(UserInformation.medicines)


        UserInformation.addNewFriend(Friend("paolo","3402323423",null,RelationEnum.Friend))
        UserInformation.addNewFriend(Friend("Carlo","3482945679",null,RelationEnum.Doctor))

        UserInformation.flush()

    }

}
