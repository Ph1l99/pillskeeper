package com.pillskeeper.activity

import android.app.Activity
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pillskeeper.R
import com.pillskeeper.activity.friend.FriendListActivity
import com.pillskeeper.activity.pills.PillsListActivity
import com.pillskeeper.data.LocalMedicine
import com.pillskeeper.data.Reminder
import com.pillskeeper.datamanager.AuthenticationManager
import com.pillskeeper.datamanager.LocalDatabase
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.enums.MedicineTypeEnum
import com.pillskeeper.notifier.ServiceNotifier
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {

    private var isFirstLogin: Boolean = true
    private var username: String? = null

    companion object {
        const val START_FIRST_LOGIN_ACTIVITY_CODE = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        LocalDatabase.sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        AuthenticationManager.obtainAuthentication()

        //UserInformation
        UserInformation.context = this

        //TODO DEBUG - to be removed
        funTest()

        readFirstLogin()

        //TODO for debug, to be removed
        buttonResetLocalMemory.setOnClickListener {
            LocalDatabase.resetMemory()
            exitProcess(-1)
        }

        friendListButton.setOnClickListener {
            val activity = Intent(this, FriendListActivity::class.java)
            startActivity(activity)
        }
    }

    private fun readFirstLogin() {
        val userN = LocalDatabase.readUsername()
        val userR = AuthenticationManager.getSignedInUser()
        if (userR != null) {
            val it = Intent(this, PillsListActivity::class.java)
            startActivity(it)
            finish()
            Toast.makeText(this, "Utente ottenuto", Toast.LENGTH_LONG).show()
        } else {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == START_FIRST_LOGIN_ACTIVITY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                //val username = data!!.getStringExtra("username")
                //todo gestire con phil
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun isJobServiceOn(): Boolean {
        Log.w(Log.DEBUG.toString(),"MainActivity: isJobServiceOn() - Started")
        (UserInformation.context.getSystemService( Context.JOB_SCHEDULER_SERVICE ) as JobScheduler)
            .allPendingJobs.forEach { entry ->
                if ( entry.id == UserInformation.JOB_ID ) {
                    Log.w(Log.DEBUG.toString(),"MainActivity: isJobServiceOn() - Ended, no service running")
                    return true
                }
            }

        Log.w(Log.DEBUG.toString(),"MainActivity: isJobServiceOn() - Ended, service already running")
        return false
    }

    private fun funTest(){


        val reminders = LinkedList<Reminder>()
        reminders.add(Reminder(1.5F, 0, 19, "Lun-Mar-Mer", Date(), null))
        reminders.add(Reminder(1F, 0, 19, "Ven", Date(), null))
        UserInformation.addNewMedicine(
            LocalMedicine(
                "Tachipirina",
                MedicineTypeEnum.Pills,
                24F,
                24F,
                reminders,
                "Tachipirina"
            )
        )


        val reminders2 = LinkedList<Reminder>()
        reminders2.add(Reminder(1.5F, 0, 19, "Lun-Mer", Date(), null))
        reminders2.add(Reminder(1F, 0, 19, "Wed", Date(), null))
        UserInformation.addNewMedicine(
            LocalMedicine(
                "Aulin",
                MedicineTypeEnum.Pills,
                24F,
                24F,
                null,
                "Aulin"
            )
        )
        UserInformation.addNewReminderList("Aulin", reminders2)


        LocalDatabase.saveMedicineList(UserInformation.medicines)

        //startNotifierThread()
    }


    private fun startNotifierThread() {
        Log.w(Log.DEBUG.toString(),"MainActivity: startNotifierThread() - Started")

        if (!isJobServiceOn()) {
            val mComponentName = ComponentName(this, ServiceNotifier::class.java)

            //Now create a JobInfo and give
            val jobInfo = JobInfo.Builder(UserInformation.JOB_ID, mComponentName)
                .setPeriodic(UserInformation.TIME_SERVICE.toLong()).build()

            val mScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

            if (mScheduler.schedule(jobInfo) == JobScheduler.RESULT_SUCCESS)
                Log.w("JOB SCHEDULER SERVICE","Job Scheduled:  ${jobInfo.id}")
            else
                Log.w("JOB SCHEDULER SERVICE","Job not scheduled")
        }

        Log.w(Log.DEBUG.toString(),"MainActivity: startNotifierThread() - Ended")
    }
}
