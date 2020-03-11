package com.pillskeeper.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pillskeeper.R
import com.pillskeeper.data.Medicine
import com.pillskeeper.datamanager.LocalDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.system.exitProcess
import com.pillskeeper.data.Reminder
import com.pillskeeper.datamanager.UserInformation
import java.util.*
import kotlin.collections.LinkedHashMap

class MainActivity : AppCompatActivity() {

    var isFirstLogin : Boolean = true;
    var username : String? = null

    companion object {
        const val START_FIRST_LOGIN_ACTIVITY_CODE = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        LocalDatabase.sharedPref = this.getPreferences(Context.MODE_PRIVATE)

        UserInformation

        //TODO DEBUG
        funTest()

        readFirstLogin();

        if(isFirstLogin){
            val activity = Intent(this, FirstLoginActivity::class.java)
            startActivityForResult(activity,START_FIRST_LOGIN_ACTIVITY_CODE)
        }

        //TODO for debug, to be removed
        buttonResetLocalMemory.setOnClickListener {
            LocalDatabase.resetMemory()
            exitProcess(-1)
        }
    }

    private fun readFirstLogin(){
        val userN = LocalDatabase.readUsername()
        println(userN)
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
        val med = Medicine("Tachipirina",24F,24F,reminders)

        val reminders2 = LinkedList<Reminder>()
        reminders.add(Reminder(1.5F,0,19,"Lun-Mer", Date(),null))
        reminders.add(Reminder(1F,0,19,"Wed",Date(),null))
        var med2 = Medicine("Aulin",24F,24F,reminders2)

        val medicines = LinkedList<Medicine>()
        medicines.add(med)
        medicines.add(med2)

        LocalDatabase.saveMedicineList(medicines)



        println("Stampo adesso quello che ho letto: ${LocalDatabase.readMedicineList()}")

    }

}
