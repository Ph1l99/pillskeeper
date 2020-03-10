package com.pillskeeper.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import com.pillskeeper.R
import com.pillskeeper.data.Medicine
import com.pillskeeper.data.Reminder
import com.pillskeeper.datamanager.LocalDatabase
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.system.exitProcess

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
        if (userN != null) {
            if(userN.isNotEmpty() || userN != "") {
                username = userN
                isFirstLogin = false
            }
        }
        return
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

}
