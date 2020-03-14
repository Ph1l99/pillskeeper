package com.pillskeeper.activity

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.ArrayAdapter
import com.pillskeeper.R
import com.pillskeeper.data.Friend
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.enums.RelationEnum
import kotlinx.android.synthetic.main.activity_new_friend.*

class NewFriendActivity(context: Context) : Dialog(context)/*AppCompatActivity()*/ {

    init {
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_new_friend)

        initSpinner()

        buttonConfirm.setOnClickListener{

            val friend: Friend = Friend(
                editTextName.text.toString() + " " + editTextSurname.text.toString(),
                editTextPhone.text.toString(),
                editTextEmail.text.toString(),
                RelationEnum.valueOf(spinnerRelation.selectedItem.toString())
            )

            UserInformation.addNewFriend(friend)
            dismiss()
        }

        buttonDeny.setOnClickListener{
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
}
