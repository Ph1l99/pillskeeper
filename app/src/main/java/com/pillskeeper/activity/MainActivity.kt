package com.pillskeeper.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pillskeeper.R
import com.pillskeeper.datamanager.LocalDatabase

class MainActivity : AppCompatActivity() {

    var isFirstLogin : Boolean = true;
    var username : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        LocalDatabase.sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        readFirstLogin();

        if(isFirstLogin){
            val activity = Intent(this, FirstLoginActivity::class.java)
            startActivity(activity)
        } else {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
        }
    }

    private fun readFirstLogin(){
        val userN = LocalDatabase.readUsername()
        if (userN != null) {
            if(userN.isNotEmpty() || userN != "") {
                username = userN
                isFirstLogin = false
                return
            }
        }
        isFirstLogin = true
        return
    }

}
