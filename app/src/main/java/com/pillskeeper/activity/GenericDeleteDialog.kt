package com.pillskeeper.activity

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import com.pillskeeper.R
import com.pillskeeper.datamanager.LocalDatabase
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.enums.DialogModeEnum
import kotlinx.android.synthetic.main.dialog_appointment.*

class GenericDeleteDialog (context: Context, private val itemName: String, private val dialogModeEnum: DialogModeEnum) : Dialog(context)  {

    init {
        setCancelable(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_appointment)

        val item =
            if(dialogModeEnum == DialogModeEnum.DELETE_APPOINTMENT) {
                titleDeleteTV.text = "Cancellare l'Appuntamento?"
                "Appuntamento"
            } else {
                titleDeleteTV.text = "Cancellare l'Medicina?"
                "Medicina"
            }

        nameTitleTV.text = itemName

        deleteConfirm.setOnClickListener {

            if(dialogModeEnum == DialogModeEnum.DELETE_APPOINTMENT){
                if(UserInformation.deleteAppointment(itemName))
                    LocalDatabase.saveAppointmentList()
                else
                    Toast.makeText(context,"Non è stato possibile cancellare l'$item",Toast.LENGTH_LONG).show()
            } else {
                if(UserInformation.deleteMedicine(itemName))
                    LocalDatabase.saveMedicineList()
                else
                    Toast.makeText(context,"Non è stato possibile cancellare la $item",Toast.LENGTH_LONG).show()
            }

            dismiss()
        }

        deleteDeny.setOnClickListener {
            dismiss()
        }
    }

}