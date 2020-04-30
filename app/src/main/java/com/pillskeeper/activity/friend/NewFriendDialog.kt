package com.pillskeeper.activity.friend

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Window
import android.widget.ArrayAdapter
import com.pillskeeper.R
import com.pillskeeper.data.Friend
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.enums.DialogModeEnum
import com.pillskeeper.enums.RelationEnum
import com.pillskeeper.utility.Utils
import kotlinx.android.synthetic.main.dialog_new_friend.*


class NewFriendDialog(
    context: Context,
    private val mode: DialogModeEnum,
    private val friend: Friend?
) : Dialog(context) {

    private var isEditing: Boolean = false
    private var stdLayout: Drawable? = null

    init {
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_new_friend)

        stdLayout = editTextName.background
        if (mode == DialogModeEnum.CREATE_NEW_FRIEND) {
            initSpinner()
            buttonConfirm.setOnClickListener {
                restoreAllLayout()
                addOrEditFriend()
            }
        } else {

            if (friend != null) {

                editTextName.setText(friend.name)
                editTextSurname.setText(friend.surname)
                editTextEmail.setText(friend.email)
                editTextPhone.setText(friend.phone)

                val relationValues: ArrayList<String> = ArrayList()
                relationValues.add(friend.relationEnum.toString())
                val arrayAdapter =
                    ArrayAdapter(context, android.R.layout.simple_spinner_item, relationValues)
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                spinnerRelation.adapter = arrayAdapter

                setAllEnable(false)


                buttonDeny.text = context.getString(R.string.closeButton)
                buttonConfirm.text = context.getString(R.string.editButton)

                buttonConfirm.setOnClickListener {
                    restoreAllLayout()
                    if (isEditing)
                        addOrEditFriend()
                    else {
                        isEditing = !isEditing
                        setAllEnable(true)
                        initSpinner()
                        buttonDeny.text = context.getString(R.string.abortButton)
                        buttonConfirm.text = context.getString(R.string.saveButton)
                    }
                }
            } else {
                dismiss()
            }
        }

        buttonDeny.setOnClickListener {
            dismiss()
        }

    }

    private fun addOrEditFriend() {
        var isValidInfo = true
        if (editTextPhone.text.toString().isNotEmpty())
            if (!Utils.checkPhoneNumber(editTextPhone.text.toString())) {
                isValidInfo = false
                Utils.errorEditText(editTextPhone)
            }

        if (editTextEmail.text.toString().isNotEmpty())
            if (!Utils.checkEmail(editTextEmail.text.toString())) {
                isValidInfo = false
                Utils.errorEditText(editTextEmail)
            }

        if (!Utils.checkName(editTextName.text.toString()) || !Utils.checkName(editTextSurname.text.toString())) {
            isValidInfo = false
            Utils.errorEditText(editTextName)
            Utils.errorEditText(editTextSurname)
        }

        if (isValidInfo) {
            val newFriend = Friend(
                editTextName.text.toString(),
                editTextSurname.text.toString(),
                editTextPhone.text.toString(),
                editTextEmail.text.toString(),
                RelationEnum.valueOf(spinnerRelation.selectedItem.toString())
            )

            if (mode == DialogModeEnum.CREATE_NEW_FRIEND) {
                UserInformation.addNewFriend(newFriend)
            } else {
                if (friend != null)
                    UserInformation.editFriend(friend.name, newFriend)

            }

            dismiss()
        }
    }

    private fun initSpinner() {
        val relationValues: ArrayList<String> = ArrayList()
        RelationEnum.values()
            .forEach { relationEnum -> relationValues.add(relationEnum.toString()) }

        val arrayAdapter =
            ArrayAdapter(context, android.R.layout.simple_spinner_item, relationValues)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerRelation.adapter = arrayAdapter
    }

    private fun setAllEnable(value: Boolean) {
        editTextName.isEnabled = value
        editTextSurname.isEnabled = value
        editTextEmail.isEnabled = value
        editTextPhone.isEnabled = value
        spinnerRelation.isEnabled = value
    }

    private fun restoreAllLayout() {
        Utils.validEditText(editTextName)
        Utils.validEditText(editTextSurname)
        Utils.validEditText(editTextPhone)
        Utils.validEditText(editTextEmail)
    }

}
