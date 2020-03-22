package com.pillskeeper.activity.friend

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.EditText
import com.pillskeeper.R
import com.pillskeeper.data.Friend
import com.pillskeeper.datamanager.LocalDatabase
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.enums.DialogModeEnum
import com.pillskeeper.enums.RelationEnum
import com.pillskeeper.utility.Utils
import kotlinx.android.synthetic.main.activity_new_friend.*


class NewFriendDialog(context: Context, private val mode: DialogModeEnum, private val friend: Friend?) : Dialog(context) {

    private var isEditing: Boolean = false
    private var stdLayout: Drawable? = null

    init {
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_new_friend)

        stdLayout = editTextName.background
        if(mode == DialogModeEnum.CREATE_NEW_FRIEND){
            initSpinner()
            buttonConfirm.setOnClickListener{
                restoreAllLayout()
                addOrEditFriend()
            }
        } else {

            if (friend != null) {

                editTextName.setText(friend.name)
                editTextSurname.setText(friend.surname)
                editTextEmail.setText(friend.email)
                editTextPhone.setText(friend.phone)

                val relationValues : ArrayList<String> = ArrayList()
                relationValues.add(friend.relationEnum.toString())
                val arrayAdapter = ArrayAdapter(context,android.R.layout.simple_spinner_item, relationValues)
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                spinnerRelation.adapter = arrayAdapter

                setAllEnable(false)

                buttonDeny.text = "Chiudi"
                buttonConfirm.text = "Modifica"

                buttonConfirm.setOnClickListener {
                    restoreAllLayout()
                    if(isEditing)
                        addOrEditFriend()
                    else {
                        isEditing = !isEditing
                        setAllEnable(true)
                        initSpinner()
                        buttonDeny.text = "Annulla"
                        buttonConfirm.text = "Salva"
                    }
                }
            } else {
                dismiss()
            }
        }

        buttonDeny.setOnClickListener{
            dismiss()
        }

    }

    private fun addOrEditFriend(){
        var isValidInfo = true
        if(editTextPhone.text.toString().isNotEmpty())
            if(!Utils.checkPhoneNumber(editTextPhone.text.toString())) {
                isValidInfo = false
                Utils.colorEditText(editTextPhone)
            }

        if(editTextEmail.text.toString().isNotEmpty())
            if(!Utils.checkEmail(editTextEmail.text.toString())) {
                isValidInfo = false
                Utils.colorEditText(editTextEmail)
            }

        if(!Utils.checkName(editTextName.text.toString()) || !Utils.checkName(editTextSurname.text.toString())) {
            isValidInfo = false
            Utils.colorEditText(editTextName)
            Utils.colorEditText(editTextSurname)
        }

        if (isValidInfo) {
            val newFriend = Friend(
                editTextName.text.toString(),
                editTextSurname.text.toString(),
                editTextPhone.text.toString(),
                editTextEmail.text.toString(),
                RelationEnum.valueOf(spinnerRelation.selectedItem.toString())
            )

            if (mode == DialogModeEnum.CREATE_NEW_FRIEND){
                if (UserInformation.addNewFriend(newFriend)) {
                    LocalDatabase.saveFriendList()
                }
            } else {
                if (friend != null && UserInformation.editFriend(friend.name, newFriend)) {
                    LocalDatabase.saveFriendList()
                }
            }

            dismiss()
        }
    }

    private fun initSpinner(){
        val relationValues : ArrayList<String> = ArrayList()
        RelationEnum.values().forEach { relationEnum -> relationValues.add(relationEnum.toString()) }

        val arrayAdapter = ArrayAdapter(context,android.R.layout.simple_spinner_item, relationValues)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerRelation.adapter = arrayAdapter
    }

    private fun setAllEnable(value: Boolean){
        editTextName.isEnabled = value
        editTextSurname.isEnabled = value
        editTextEmail.isEnabled = value
        editTextPhone.isEnabled = value
        spinnerRelation.isEnabled = value
    }

    private fun restoreAllLayout(){
        Utils.colorEditText(editTextName,false)
        Utils.colorEditText(editTextSurname,false)
        Utils.colorEditText(editTextPhone,false)
        Utils.colorEditText(editTextEmail,false)
    }

}
