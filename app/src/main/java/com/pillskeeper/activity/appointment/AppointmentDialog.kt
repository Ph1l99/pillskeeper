package com.pillskeeper.activity.appointment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import com.pillskeeper.R
import com.pillskeeper.datamanager.LocalDatabase
import com.pillskeeper.datamanager.UserInformation
import kotlinx.android.synthetic.main.dialog_appointment.*

class AppointmentDialog (context: Context, private val appointmentName: String) : Dialog(context)  {

    init {
        setCancelable(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_appointment)

        appointmentNameTitleTV.text = appointmentName

        deleteAppoitConfirm.setOnClickListener {
            if(UserInformation.deleteAppointment(appointmentName)){
                LocalDatabase.saveAppointmentList()
            } else {
                Toast.makeText(context,"Non è stato possibile cancellare l'appuntamento",Toast.LENGTH_LONG).show()
            }
            dismiss()
        }

        deleteAppoitDeny.setOnClickListener {
            dismiss()
        }
    }


}