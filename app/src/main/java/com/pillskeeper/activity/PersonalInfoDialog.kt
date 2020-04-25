package com.pillskeeper.activity

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.pillskeeper.R
import com.pillskeeper.activity.registration.LoginDialog
import com.pillskeeper.data.User
import com.pillskeeper.datamanager.FirebaseDatabaseManager
import com.pillskeeper.datamanager.LocalDatabase
import com.pillskeeper.interfaces.Callback
import com.pillskeeper.interfaces.FirebaseUserCallback
import com.pillskeeper.utility.Utils
import kotlinx.android.synthetic.main.dialog_personal_info.*

/**
 * Dialog for changing personal informations like name, surname and email
 */
class PersonalInfoDialog(context: Context, private val userId: String) : Dialog(context) {


    private lateinit var auth: FirebaseAuth
    private lateinit var user: User
    private var isEditing: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_personal_info)

        auth = FirebaseAuth.getInstance()

        getUser(userId)

        buttonDenyInfo.text = context.getText(R.string.closeButton)
        buttonConfirmInfo.text = context.getText(R.string.editButton)

        buttonConfirmInfo.setOnClickListener {
            if (isEditing)
                changeInfo()
            else {
                isEditing = !isEditing
                setAllEnable(true)
                buttonDenyInfo.text = context.getText(R.string.abortButton)
                buttonConfirmInfo.text = context.getText(R.string.saveButton)
            }
        }
        buttonDenyInfo.setOnClickListener {
            dismiss()
        }
    }

    //Method for getting the user's data from the Firebase Realtime Database
    private fun getUser(userId: String) {
        FirebaseDatabaseManager.getUser(userId, object : FirebaseUserCallback {
            override fun onCallback(user: User?) {
                if (user != null) {
                    displayUser(user)
                } else {
                    Utils.buildAlertDialog(
                        context,
                        context.getString(R.string.networkError),
                        context.getString(R.string.message_title)
                    ).show()
                    dismiss()
                }
            }

        })

    }

    private fun changeInfo() {
        var closedOK = false
        var isValidInfo = true
        var updateAuth = false
        if (!Utils.checkName(editTextName.text.toString()) || !Utils.checkName(editTextSurname.text.toString())) {
            Utils.errorEditText(editTextName)
            Utils.errorEditText(editTextSurname)
            isValidInfo = false
        }
        if (editTextEmail.text.toString().isNotEmpty()) {
            if (!Utils.checkEmail(editTextEmail.text.toString())) {
                Utils.errorEditText(editTextEmail)
                isValidInfo = false
            }
        }
        //If the user has inserted potential valid infos we open a login dialog
        if (isValidInfo) {
            val dialog = LoginDialog(context, user.email, object : Callback {
                override fun onSuccess(res: Boolean) {
                    closedOK = true
                }

                override fun onError() {
                    closedOK = false
                }

            })
            dialog.setOnDismissListener {
                //If the LoginDialog was closed with success we check if the user has changed the primary email
                if (closedOK) {
                    if (auth.currentUser?.email != editTextEmail.text.toString()) {
                        updateAuth = true
                    }
                    if (updateAuth) {
                        //If the user has changed the primary email we first update the Firebase Authentication instance
                        auth.currentUser?.updateEmail(editTextEmail.text.toString())
                        auth.currentUser?.uid?.let {
                            val userToBeWritten = User(
                                it,
                                editTextName.text.toString(),
                                editTextSurname.text.toString(),
                                editTextEmail.text.toString()
                            )
                            //We then save the user to the Firebase Realtime Database and to the local database
                            FirebaseDatabaseManager.writeUser(userToBeWritten, object : Callback {
                                override fun onSuccess(res: Boolean) {
                                    LocalDatabase.saveUser(userToBeWritten)
                                }

                                override fun onError() {
                                    Utils.buildAlertDialog(
                                        context,
                                        context.getString(R.string.networkError),
                                        context.getString(R.string.message_title)
                                    ).show()
                                }

                            })
                        }
                    } else {
                        //If the user hasn't changed the primary email we simply write the new data on the Firebase Realtime Database
                        auth.currentUser?.uid?.let {
                            val userToBeWritten = User(
                                it,
                                editTextName.text.toString(),
                                editTextSurname.text.toString(),
                                editTextEmail.text.toString()
                            )
                            FirebaseDatabaseManager.writeUser(userToBeWritten, object : Callback {
                                override fun onSuccess(res: Boolean) {
                                    LocalDatabase.saveUser(userToBeWritten)
                                }

                                override fun onError() {
                                    Utils.buildAlertDialog(
                                        context,
                                        context.getString(R.string.networkError),
                                        context.getString(R.string.message_title)
                                    ).show()
                                }

                            })
                        }
                    }
                    dismiss()
                } else {
                    dismiss()
                }
            }
            dialog.show()

        }
    }

    //Method for displaying a user's data on the UI
    private fun displayUser(user: User) {
        this.user = user
        editTextEmail.setText(user.email)
        editTextName.setText(user.name)
        editTextSurname.setText(user.surname)
        setAllEnable(false)
    }

    //Method for enabling the data fields
    private fun setAllEnable(value: Boolean) {
        editTextName.isEnabled = value
        editTextSurname.isEnabled = value
        editTextEmail.isEnabled = value
    }
}
