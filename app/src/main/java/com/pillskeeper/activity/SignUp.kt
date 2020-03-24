package com.pillskeeper.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.pillskeeper.R
import com.pillskeeper.data.User
import com.pillskeeper.datamanager.DatabaseManager
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {

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
                if (DatabaseManager.writeNewUser(
                        User(
                            auth.currentUser?.uid.toString(),
                            nameField.text.toString(),
                            surnameField.text.toString(),
                            mailField.text.toString()
                        )
                    ).second
                ) {
                    finish()
                }
            }
        }

    }
}
