package com.pillskeeper.activity.registration

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pillskeeper.R
import com.pillskeeper.data.User
import com.pillskeeper.datamanager.FirebaseDatabaseManager
import com.pillskeeper.datamanager.LocalDatabase
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.interfaces.Callback
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
        nameField.requestFocus()
        auth = FirebaseAuth.getInstance()

        signupButton.setOnClickListener {
            signUp()
        }

    }

    private fun signUp() {
        if (mailField.text.toString().isEmpty() || passwordField.text.toString()
                .isEmpty() || nameField.text.toString()
                .isEmpty() || surnameField.text.toString().isEmpty()
        ) {
            Utils.errorEditText(mailField)
            Utils.errorEditText(nameField)
            Utils.errorEditText(surnameField)
            Utils.errorEditText(passwordField)
        }
        auth.createUserWithEmailAndPassword(
            mailField.text.toString(),
            passwordField.text.toString()
        ).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                auth.currentUser?.uid?.let {
                    val userToBeWritten = User(
                        auth.currentUser!!.uid,
                        nameField.text.toString(),
                        surnameField.text.toString(),
                        mailField.text.toString()
                    )

                    FirebaseDatabaseManager.writeUser(userToBeWritten, object : Callback {
                        override fun success(res: Boolean) {
                            LocalDatabase.saveUser(userToBeWritten)
                            finish()
                        }

                        override fun error() {
                            Utils.errorEditText(mailField)
                            Utils.errorEditText(nameField)
                            Utils.errorEditText(surnameField)
                            Utils.errorEditText(passwordField)
                            Toast.makeText(
                                UserInformation.context,
                                R.string.error_values,
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    })
                }
            }
        }

    }
}
