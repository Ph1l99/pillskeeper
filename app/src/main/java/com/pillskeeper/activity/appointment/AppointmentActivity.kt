package com.pillskeeper.activity.appointment

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import com.pillskeeper.R
import com.pillskeeper.activity.pills.reminder.ReminderActivity
import com.pillskeeper.data.Appointment
import com.pillskeeper.data.ReminderMedicine
import kotlinx.android.synthetic.main.activity_appointment.*
import java.util.*

class AppointmentActivity : AppCompatActivity() {

    private lateinit var appointment: Appointment
    private lateinit var minuteArray: LinkedList<String>

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
                buttonDate.text = "$dayOfMonth/${monthOfYear+1}/$year"

                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear+1)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)

            }, year, month, day).show()
        }

        buttonDeleteAppointment.setOnClickListener { finish() }

        buttonConfirmAppointment.setOnClickListener {
            appointment = Appointment(
                appointmentNameTV.text.toString(),
                minuteArray[minuteSpinner.selectedItemPosition].toInt(),
                ReminderActivity.hours[hourSpinner.selectedItemPosition].toInt(),
                cal.time,
                additionalNoteAppointment.text.toString()
            )

            println(appointment)

            //todo inserire il metodo di scrittura dell'appointment
            //todo fare i vari controlli sugli input
        }

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
