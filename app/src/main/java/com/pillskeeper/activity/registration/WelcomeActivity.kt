package com.pillskeeper.activity.registration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.pillskeeper.R
import com.pillskeeper.activity.HomepageActivity
import com.pillskeeper.datamanager.LocalDatabase

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        FirebaseApp.initializeApp(this)
        LocalDatabase.sharedPref = this.getPreferences(Context.MODE_PRIVATE)

        checkLogin()
    }

    private fun checkLogin() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.addOnSuccessListener {
                if (it.token != null) {
                    startActivity(Intent(this, HomepageActivity::class.java))
                    finish()
                } else {
                    FirebaseAuth.getInstance().signOut()
                    LocalDatabase.sharedPref?.edit()?.clear()?.apply()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
