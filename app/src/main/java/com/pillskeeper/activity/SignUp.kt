package com.pillskeeper.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseUser
import com.pillskeeper.R
import com.pillskeeper.data.User
import com.pillskeeper.datamanager.AuthenticationManager
import com.pillskeeper.datamanager.DatabaseManager
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {
    private lateinit var currentUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        FirebaseApp.initializeApp(this)
        DatabaseManager.obtainRemoteDatabase()
        AuthenticationManager.obtainAuthentication()
    }

    fun signUp(view: View) {
        val resultAuth = AuthenticationManager.createNewUser(
            mailField.text.toString(),
            passwordField.text.toString()
        )
        if (resultAuth != null) {
            if (resultAuth.second) {
                Log.w(Log.DEBUG.toString(), AuthenticationManager.getSignedInUser()?.email.toString())
                val resultDB = DatabaseManager.writeNewUser(
                    User(
                        AuthenticationManager.getSignedInUser()?.uid.toString(),
                        "ciao",
                        "phil",
                        mailField.text.toString()
                    )
                )
            } else {
                Toast.makeText(this, "Qualcosa è andato storto", Toast.LENGTH_LONG).show()
            }
        }
    }
}
