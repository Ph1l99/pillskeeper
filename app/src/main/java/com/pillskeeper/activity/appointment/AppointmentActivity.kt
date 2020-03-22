package com.pillskeeper.activity.appointment

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.Toast
import com.pillskeeper.R
import com.pillskeeper.activity.pills.reminder.ReminderActivity
import com.pillskeeper.data.Appointment
import com.pillskeeper.data.ReminderMedicine
import com.pillskeeper.datamanager.LocalDatabase
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.utility.Utils
import kotlinx.android.synthetic.main.activity_appointment.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class AppointmentActivity : AppCompatActivity() {

    private lateinit var minuteArray: LinkedList<String>
    private var dateSelected: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment)

        initSpinner()

        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)




        buttonDate.setOnClickListener {
            DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                buttonDate.text = getString(R.string.dateButtonFormatted,dayOfMonth,monthOfYear+1,year)

                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear+1)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)

                dateSelected = cal.time

            }, year, month, day).show()
        }

        println(cal.time)

        buttonDeleteAppointment.setOnClickListener { finish() }

        buttonConfirmAppointment.setOnClickListener {
            resetEditText()
            if(checkValues()) {
                if(UserInformation.addNewAppointment(Appointment(
                        appointmentNameTV.text.toString(),
                        minuteArray[minuteSpinner.selectedItemPosition].toInt(),
                        ReminderActivity.hours[hourSpinner.selectedItemPosition].toInt(),
                        cal.time,
                        additionalNoteAppointment.text.toString()
                    ))
                ) {
                    LocalDatabase.saveAppointmentList()
                    finish()
                } else {
                    Toast.makeText(this,"Appuntamento già presente!",Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    private fun checkValues(): Boolean {
        var result = true

        if(appointmentNameTV.text.toString().isEmpty()){
            Utils.colorEditText(appointmentNameTV)
            result= false
        }

        if(dateSelected == null || dateSelected!!.compareTo(Date(System.currentTimeMillis())) == -1){
            Toast.makeText(this,"Perfavore inserire una data corretta",Toast.LENGTH_LONG).show()
            result = false
        }

        return result
    }

    private fun resetEditText(){
        Utils.colorEditText(appointmentNameTV,false)
    }

    private fun initSpinner(){
        minuteArray = LinkedList()
        for (i in 0..1)
            minuteArray.add("0${i*5}")
        for (i in 2..12)
            minuteArray.add("${i*5}")

        val arrayAdapterHours = ArrayAdapter(this,android.R.layout.simple_spinner_item, ReminderActivity.hours)
        arrayAdapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        hourSpinner.adapter = arrayAdapterHours

        val arrayAdapterMinutes = ArrayAdapter(this,android.R.layout.simple_spinner_item, minuteArray)
        arrayAdapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        minuteSpinner.adapter = arrayAdapterMinutes

    }


}
