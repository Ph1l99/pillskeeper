package com.pillskeeper.activity

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pillskeeper.R
import com.pillskeeper.data.User
import com.pillskeeper.datamanager.LocalDatabase
import com.pillskeeper.utility.Utils
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    companion object {
        private const val PATH_USERS = "users"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()

        signupButton.setOnClickListener {
            signUp()
        }

    }

    fun signUp() {
        if (mailField.text.toString().isEmpty() || passwordField.text.toString()
                .isEmpty() || nameField.text.toString()
                .isEmpty() || surnameField.text.toString().isEmpty()
        ) {
            Utils.colorEditText(mailField)
            Utils.colorEditText(nameField)
            Utils.colorEditText(surnameField)
            Utils.colorEditText(passwordField)
        }
        auth.createUserWithEmailAndPassword(
            mailField.text.toString(),
            passwordField.text.toString()
        ).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val databaseReference = Firebase.database.reference
                auth.currentUser?.uid?.let {
                    val userToBeWritten = User(
                        auth.currentUser!!.uid,
                        nameField.text.toString(),
                        surnameField.text.toString(),
                        mailField.text.toString()
                    )

                    databaseReference.child(PATH_USERS).child(it).setValue(userToBeWritten)
                        .addOnCompleteListener {
                            LocalDatabase.saveUser(userToBeWritten)
                            finish()
                        }
                        .addOnFailureListener {
                            Utils.colorEditText(mailField)
                            Utils.colorEditText(nameField)
                            Utils.colorEditText(surnameField)
                            Utils.colorEditText(passwordField)
                            Toast.makeText(this, R.string.error_values, Toast.LENGTH_LONG).show()
                        }
                }
            }
        }

    }
}
