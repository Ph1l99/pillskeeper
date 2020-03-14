package com.pillskeeper.activity

import android.os.Bundle
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
    lateinit var currentUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        FirebaseApp.initializeApp(this)
        DatabaseManager.obtainRemoteDatabase()
        AuthenticationManager.obtainAuthentication()
    }

    fun signUp(view: View) {
        AuthenticationManager.createNewUser(
            mailField.text.toString(),
            passwordField.text.toString()
        ).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                currentUser = AuthenticationManager.getSignedInUser()!!
                val result = DatabaseManager.writeNewUser(
                    User(
                        currentUser.uid,
                        nameField.text.toString(),
                        surnameField.text.toString(),
                        mailField.text.toString()
                    )
                )
                if (result) {
                    //TODO passare utente all'activity dopo attraverso un intent e scrivere le info a DB locale + a db condiviso
                } else {
                    //TODO spaccare tutto e dire all'utente di reinserire i dati
                }
            } else {
                Toast.makeText(this, resources.getString(R.string.error_values), Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}
