package com.pillskeeper.activity.appointment

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pillskeeper.R
import com.pillskeeper.activity.appointment.AppointmentListActivity.Companion.APPOINTMENT_VALUE
import com.pillskeeper.activity.pills.reminder.ReminderActivity
import com.pillskeeper.data.Appointment
import com.pillskeeper.datamanager.LocalDatabase
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.utility.Utils
import kotlinx.android.synthetic.main.activity_appointment.*
import java.util.*

class AppointmentFormActivity : AppCompatActivity() {

    private var isEditing: Boolean = false
    private lateinit var minuteArray: LinkedList<String>
    private var dateSelected: Date? = null
    private var appointment: Appointment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment)


        val cal = Calendar.getInstance()


        if (intent.getSerializableExtra(APPOINTMENT_VALUE) != null) {
            appointment = intent.getSerializableExtra(APPOINTMENT_VALUE) as Appointment
            appointmentNameTV.setText(appointment!!.name)
            appointmentNameTV.isEnabled = false
            appointmentNameTV.setRawInputType(0)
            hourSpinner.isEnabled = false
            minuteSpinner.isEnabled = false
            buttonDate.isEnabled = false
            cal.time = appointment!!.date
            buttonDate.text = getString(R.string.dateButtonFormatted,
            cal.get(Calendar.DAY_OF_MONTH),cal.get(Calendar.MONTH),cal.get(Calendar.YEAR))
            additionalNoteAppointment.isEnabled = false
            additionalNoteAppointment.setText(appointment!!.additionNotes)
            buttonDeleteAppointment.text = "Chiudi"
            buttonConfirmAppointment.text = "Modifica"
        }

        initSpinner()

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

        buttonDeleteAppointment.setOnClickListener { finish() }

        if(appointment == null) {
            buttonConfirmAppointment.setOnClickListener {
                resetEditText()
                addOrEditAppointment(cal)
            }
        } else {
            if(isEditing)
                addOrEditAppointment(cal)
            else {
                isEditing = !isEditing
                setAllEnable(true)
                initSpinner()
                buttonDeleteAppointment.text = "Annulla"
                buttonConfirmAppointment.text = "Salva"
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
        val arrayAdapterHours: Any
        if(appointment == null) {
            arrayAdapterHours = ArrayAdapter(this,android.R.layout.simple_spinner_item, ReminderActivity.hours)
            for (i in 0..1)
                minuteArray.add("0${i * 5}")
            for (i in 2..12)
                minuteArray.add("${i * 5}")
        } else {
            val hour = LinkedList<String>()
            hour.add(appointment!!.hours.toString())
            arrayAdapterHours = ArrayAdapter(this,android.R.layout.simple_spinner_item, hour)
            minuteArray.add(appointment!!.minutes.toString())
        }

        arrayAdapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        hourSpinner.adapter = arrayAdapterHours

        val arrayAdapterMinutes = ArrayAdapter(this,android.R.layout.simple_spinner_item, minuteArray)
        arrayAdapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        minuteSpinner.adapter = arrayAdapterMinutes

    }

    private fun addOrEditAppointment(cal: Calendar) {
        if (checkValues()) {
            if (UserInformation.addNewAppointment(
                    Appointment(
                        appointmentNameTV.text.toString(),
                        minuteArray[minuteSpinner.selectedItemPosition].toInt(),
                        ReminderActivity.hours[hourSpinner.selectedItemPosition].toInt(),
                        cal.time,
                        additionalNoteAppointment.text.toString()
                    )
                )
            ) {
                LocalDatabase.saveAppointmentList()
                finish()
            } else {
                Toast.makeText(this, "Appuntamento già presente!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setAllEnable(flag: Boolean){
        appointmentNameTV.isEnabled = flag
        hourSpinner.isEnabled = flag
        minuteSpinner.isEnabled = flag
        buttonDate.isEnabled = flag
        additionalNoteAppointment.isEnabled = flag
    }
}
