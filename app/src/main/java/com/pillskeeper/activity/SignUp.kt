package com.pillskeeper.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.pillskeeper.R
import com.pillskeeper.data.User
import com.pillskeeper.datamanager.DatabaseManager
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {
    private lateinit var currentUser: FirebaseUser
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        DatabaseManager.obtainRemoteDatabase()
    }

    fun signUp(view: View) {
        auth.createUserWithEmailAndPassword(
            mailField.text.toString(),
            passwordField.text.toString()
        ).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                currentUser.sendEmailVerification().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this,
                            R.string.signup_email_confirmation.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                        Log.i(Log.DEBUG.toString(), "Sent confirmation email")
                    }
                }
                if (checkIfUserHasConfirmedLink()) {
                    if (DatabaseManager.writeNewUser(
                            User(
                                auth.currentUser?.uid.toString(),
                                nameField.text.toString(),
                                surnameField.text.toString(),
                                mailField.text.toString()
                            )
                        ).second
                    ) {
                        //TODO APRIRE NUOVA ACTIVITY
                    }
                } else {
                    //TODO AUTH NON SUCCESSFUL
                }
            }
        }

    }

    private fun checkIfUserHasConfirmedLink(): Boolean {
        while (currentUser.uid == "") {
            currentUser.reload()
        }
        return true
    }
}
