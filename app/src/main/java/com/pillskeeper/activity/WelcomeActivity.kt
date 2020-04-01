package com.pillskeeper.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.pillskeeper.R
import com.pillskeeper.activity.registration.LoginActivity
import com.pillskeeper.datamanager.LocalDatabase

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        LocalDatabase.sharedPref = this.getPreferences(Context.MODE_PRIVATE)

        checkLogin()
    }

    private fun checkLogin() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.addOnSuccessListener {
                if (it.token != null) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    FirebaseAuth.getInstance().signOut()
                    LocalDatabase.sharedPref?.edit()?.clear()?.apply()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
        }
    }
}
