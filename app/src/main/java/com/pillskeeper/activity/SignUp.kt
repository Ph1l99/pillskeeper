package com.pillskeeper.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseUser
import com.pillskeeper.R
import com.pillskeeper.datamanager.AuthenticationManager
import com.pillskeeper.datamanager.DatabaseManager
import com.pillskeeper.datamanager.UserInformation
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        FirebaseApp.initializeApp(UserInformation.context)
        DatabaseManager.obtainRemoteDatabase()
        AuthenticationManager.obtainAuthentication()
    }

    fun signUp(view: View) {
        AuthenticationManager.createNewUser(
            mailField.text.toString(),
            passwordField.text.toString()
        )
    }
}
