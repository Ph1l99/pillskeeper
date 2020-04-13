package com.pillskeeper.activity

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.pillskeeper.R
import com.pillskeeper.activity.registration.LoginDialog
import com.pillskeeper.data.User
import com.pillskeeper.datamanager.FirebaseAuthenticationManager
import com.pillskeeper.datamanager.FirebaseDatabaseManager
import com.pillskeeper.datamanager.LocalDatabase
import com.pillskeeper.datamanager.UserInformation.user
import com.pillskeeper.interfaces.Callback
import com.pillskeeper.interfaces.FirebaseUserCallback
import com.pillskeeper.utility.Utils
import kotlinx.android.synthetic.main.dialog_personal_info.*

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

    //TODO passare messaggio quando viene chiuso il dialog
    private fun changeInfo() {
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
        if (isValidInfo) {
            val dialog = LoginDialog(context, user.email)
            dialog.setOnDismissListener {
                if (auth.currentUser?.email != editTextEmail.text.toString()) {
                    updateAuth = true
                }
                if (updateAuth) {

                    auth.currentUser?.updateEmail(editTextEmail.text.toString())
                    auth.currentUser?.uid?.let {
                        val userToBeWritten = User(
                            it,
                            editTextName.text.toString(),
                            editTextSurname.text.toString(),
                            editTextEmail.text.toString()
                        )
                        FirebaseDatabaseManager.writeUser(userToBeWritten, object : Callback {
                            override fun success(res: Boolean) {
                                LocalDatabase.saveUser(userToBeWritten)
                            }

                            override fun error() {
                                Utils.buildAlertDialog(
                                    context,
                                    context.getString(R.string.networkError),
                                    context.getString(R.string.message_title)
                                ).show()
                            }

                        })
                    }
                } else {
                    auth.currentUser?.uid?.let {
                        val userToBeWritten = User(
                            it,
                            editTextName.text.toString(),
                            editTextSurname.text.toString(),
                            editTextEmail.text.toString()
                        )
                        FirebaseDatabaseManager.writeUser(userToBeWritten, object : Callback {
                            override fun success(res: Boolean) {
                                LocalDatabase.saveUser(userToBeWritten)
                            }

                            override fun error() {
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
            }
            dialog.show()
        }
    }


    private fun displayUser(user: User) {
        this.user = user
        editTextEmail.setText(user.email)
        editTextName.setText(user.name)
        editTextSurname.setText(user.surname)
        setAllEnable(false)
    }

    private fun setAllEnable(value: Boolean) {
        editTextName.isEnabled = value
        editTextSurname.isEnabled = value
        editTextEmail.isEnabled = value
    }
}
