package com.pillskeeper.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.pillskeeper.R
import com.pillskeeper.data.User
import com.pillskeeper.datamanager.AuthenticationManager
import com.pillskeeper.datamanager.CentralDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        FirebaseApp.initializeApp(this)
        CentralDatabase.obtainRemoteDatabase()
        AuthenticationManager.obtainAuthentication()
    }

    fun signUp(view: View) {
        AuthenticationManager.createNewUser(
            mailField.text.toString(),
            passwordField.text.toString()
        ).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = AuthenticationManager.getSignedInUser()
                val localUser = User(24,"name","surname","ciao@ciao.com")
                //CentralDatabase.writeNewUser(localUser)
                //TODO passare utente all'activity dopo attraverso un intent e scrivere le info a DB locale + a db condiviso
            } else {
                Toast.makeText(this, resources.getString(R.string.error_values), Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}
