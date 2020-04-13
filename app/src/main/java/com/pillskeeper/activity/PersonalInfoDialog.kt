package com.pillskeeper.activity

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.pillskeeper.R
import com.pillskeeper.data.User
import com.pillskeeper.datamanager.FirebaseDatabaseManager
import com.pillskeeper.datamanager.LocalDatabase
import com.pillskeeper.interfaces.FirebaseUserCallback
import com.pillskeeper.utility.Utils
import kotlinx.android.synthetic.main.dialog_personal_info.*

class PersonalInfoDialog(context: Context, private val userId: String) : Dialog(context) {

    private lateinit var auth: FirebaseAuth
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
                    //TODO @phil mettere messaggio di errore
                }
            }

        })

    }

    private fun changeInfo() {
        var isValidInfo = true
        println(isValidInfo)
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
        println(isValidInfo)
        if (isValidInfo) {
            if (auth.currentUser?.email != editTextEmail.text.toString()) {
                println(auth.currentUser?.email.toString())
                updateAuth = true
                println(updateAuth)
            }
            if (updateAuth) {
                //TODO autenticare l'utente nuovamente richiedendo un token
                auth.currentUser?.updateEmail(editTextEmail.text.toString())?.addOnFailureListener {
                    println(it.toString())
                }
                auth.currentUser?.uid?.let {
                    val userToBeWritten = User(
                        it,
                        editTextName.text.toString(),
                        editTextSurname.text.toString(),
                        editTextEmail.text.toString()
                    )
                    FirebaseDatabaseManager.writeUser(userToBeWritten)
                    LocalDatabase.saveUser(userToBeWritten)
                }
            } else {
                auth.currentUser?.uid?.let {
                    val userToBeWritten = User(
                        it,
                        editTextName.text.toString(),
                        editTextSurname.text.toString(),
                        editTextEmail.text.toString()
                    )
                    FirebaseDatabaseManager.writeUser(userToBeWritten)
                    LocalDatabase.saveUser(userToBeWritten)
                }
            }
            dismiss()
        }
    }


    private fun displayUser(user: User) {
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
