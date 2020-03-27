package com.pillskeeper.activity

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pillskeeper.R
import com.pillskeeper.data.User
import com.pillskeeper.utility.Utils
import kotlinx.android.synthetic.main.activity_personal_info.*

class PersonalInfoActivity(context: Context, private val userId: String) : Dialog(context) {

    companion object {
        const val PATH_USERS = "users"
    }

    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var isEditing: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_info)
        auth = FirebaseAuth.getInstance()
        databaseReference = Firebase.database.reference


        getUser(userId)

        buttonDenyInfo.text = "Chiudi"
        buttonConfirmInfo.text = "Modifica"

        buttonConfirmInfo.setOnClickListener {
            if (isEditing)
                changeInfo()
            else {
                isEditing = !isEditing
                setAllEnable(true)
                buttonDenyInfo.text = "Annulla"
                buttonConfirmInfo.text = "Salva"
            }
        }
        buttonDenyInfo.setOnClickListener {
            dismiss()
        }
    }

    private fun getUser(userId: String) {

        databaseReference.child(PATH_USERS).child(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    displayUser(User.fromMap(p0.value as Map<String, String>))
                }

                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(context, "Dati non scaricati", Toast.LENGTH_LONG).show()
                }
            })

    }

    private fun changeInfo() {
        var isValidInfo = true
        var updateAuth = false
        if (!Utils.checkName(editTextName.text.toString()) || !Utils.checkName(editTextSurname.text.toString())) {
            isValidInfo = false
            Utils.colorEditText(editTextName)
            Utils.colorEditText(editTextSurname)
        }
        if (editTextEmail.text.toString().isNotEmpty()) {
            if (!Utils.checkEmail(editTextEmail.text.toString())) {
                isValidInfo = false
                Utils.colorEditText(editTextEmail)
            }
        }
        if (isValidInfo) {
            if (auth.currentUser?.email != editTextEmail.text.toString()) {
                updateAuth = true
            }
            if (updateAuth) {
                auth.currentUser?.updateEmail(editTextEmail.text.toString())
                auth.currentUser?.uid?.let {
                    databaseReference.child(PATH_USERS).child(userId).setValue(
                        User(
                            it,
                            editTextName.text.toString(),
                            editTextSurname.text.toString(),
                            editTextEmail.text.toString()
                        )
                    )
                }
            } else {
                auth.currentUser?.uid?.let {
                    databaseReference.child(PATH_USERS).child(userId).setValue(
                        User(
                            it,
                            editTextName.text.toString(),
                            editTextSurname.text.toString(),
                            editTextEmail.text.toString()
                        )
                    )
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
