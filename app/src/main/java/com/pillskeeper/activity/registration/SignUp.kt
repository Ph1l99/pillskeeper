package com.pillskeeper.activity.registration

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pillskeeper.R
import com.pillskeeper.data.User
import com.pillskeeper.datamanager.FirebaseAuthenticationManager
import com.pillskeeper.datamanager.FirebaseDatabaseManager
import com.pillskeeper.datamanager.LocalDatabase
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.interfaces.Callback
import com.pillskeeper.utility.Utils
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        nameField.requestFocus()

        signupButton.setOnClickListener {
            signUp()
        }

    }

    /**
     * Method for signup users
     */
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

        FirebaseAuthenticationManager.createNewUser(
            mailField.text.toString(),
            passwordField.text.toString(),
            object : Callback {
                override fun onSuccess(res: Boolean) {
                    val userFirebase = FirebaseAuthenticationManager.getCurrentUser()
                    if (userFirebase != null) {
                        val userToBeWritten = User(
                            userFirebase.uid, nameField.text.toString(),
                            surnameField.text.toString(),
                            mailField.text.toString()
                        )
                        FirebaseDatabaseManager.writeUser(userToBeWritten, object : Callback {
                            override fun onSuccess(res: Boolean) {
                                LocalDatabase.saveUser(userToBeWritten)
                                finish()
                            }

                            override fun onError() {
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

                override fun onError() {
                    Utils.buildAlertDialog(
                        this@SignUp,
                        getString(R.string.networkError),
                        getString(R.string.message_title)
                    ).show()
                }

            })


    }
}
