package com.pillskeeper.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pillskeeper.R
import com.pillskeeper.data.User
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
        if (mailField.text.toString().isNullOrEmpty() || passwordField.text.toString()
                .isNullOrEmpty() || nameField.text.toString()
                .isNullOrEmpty() || surnameField.text.toString().isNullOrEmpty()
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
                    databaseReference.child(PATH_USERS).child(it).setValue(
                        User(
                            auth.currentUser!!.uid,
                            nameField.text.toString(),
                            surnameField.text.toString(),
                            mailField.text.toString()
                        )
                    )
                        .addOnCompleteListener {
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
