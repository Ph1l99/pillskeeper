package com.pillskeeper.activity.registration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.pillskeeper.R
import com.pillskeeper.activity.homefragments.HomepageActivity
import com.pillskeeper.datamanager.FirebaseAuthenticationManager
import com.pillskeeper.datamanager.FirebaseDatabaseManager
import com.pillskeeper.datamanager.LocalDatabase
import com.pillskeeper.interfaces.Callback
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        FirebaseApp.initializeApp(this)
        FirebaseDatabaseManager.enablePersistence()
        FirebaseDatabaseManager.obtainDatabaseReference()
        FirebaseAuthenticationManager.obtainAuthenticationInstance()
        LocalDatabase.sharedPref = this.getPreferences(Context.MODE_PRIVATE)

        checkLogin()
    }

    private fun checkLogin() {
        val user = FirebaseAuthenticationManager.getCurrentUser()
        if (user != null) {
            FirebaseAuthenticationManager.getCurrentUserIdToken(user, object : Callback {
                override fun onSuccess(res: Boolean) {
                    startActivity(Intent(applicationContext, HomepageActivity::class.java))
                    finish()
                    progressBar.visibility = View.GONE
                }

                override fun onError() {
                    FirebaseAuth.getInstance().signOut()
                    LocalDatabase.sharedPref?.edit()?.clear()?.apply()
                    startActivity(Intent(applicationContext, LoginActivity::class.java))
                    finish()
                    progressBar.visibility = View.GONE
                }
            })
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            progressBar.visibility = View.GONE
        }
    }
}
