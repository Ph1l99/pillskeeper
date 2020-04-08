package com.pillskeeper.activity.appointment

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pillskeeper.R
import com.pillskeeper.activity.appointment.AppointmentListActivity.Companion.APPOINTMENT_VALUE
import com.pillskeeper.activity.medicine.reminderformfragments.ReminderActivity
import com.pillskeeper.data.Appointment
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.notifier.NotifyPlanner
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
            appointmentNameTV.setRawInputType(0)
            dateSelected = appointment!!.date
            cal.time = appointment!!.date
            buttonDate.text = getString(R.string.dateButtonFormatted, cal.get(Calendar.DAY_OF_MONTH),cal.get(Calendar.MONTH) + 1,cal.get(Calendar.YEAR))
            additionalNoteAppointment.setText(appointment!!.additionNotes)
            buttonDeleteAppointment.text = getText(R.string.closeButton)
            buttonConfirmAppointment.text = getText(R.string.editButton)
            setAllEnable(false)
        }

        initSpinner()

        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        buttonDate.setOnClickListener {
            DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                buttonDate.text = getString(R.string.dateButtonFormatted,dayOfMonth,monthOfYear+1,year)

                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)

                dateSelected = cal.time

            }, year, month, day).show()
        }

        buttonDeleteAppointment.setOnClickListener { finish() }

        buttonConfirmAppointment.setOnClickListener {
            if (appointment == null) {
                resetEditText()
                addOrEditAppointment(cal)
            } else {
                if (isEditing)
                    addOrEditAppointment(cal)
                else {
                    isEditing = !isEditing
                    setAllEnable(true)
                    initSpinner()
                    buttonDeleteAppointment.text = getText(R.string.abortButton)
                    buttonConfirmAppointment.text = getText(R.string.saveButton)
                }

            }
        }

    }

    private fun checkValues(): Boolean {
        var result = true

        if(appointmentNameTV.text.toString().isEmpty()){
            Utils.errorEditText(appointmentNameTV)
            result = false
        }

        if(dateSelected != null) {
            if (!Utils.checkDate(dateSelected!!, this))
                result = false
        } else {
            Toast.makeText(this,"Perfavore inserire una data corretta",Toast.LENGTH_LONG).show()
            result = false
        }

        return result
    }

    private fun resetEditText(){
        Utils.validEditText(appointmentNameTV)
    }

    private fun initSpinner(){
        val cal = Calendar.getInstance()
        minuteArray = LinkedList()
        val arrayAdapterHours: Any
        if(appointment == null || isEditing) {
            arrayAdapterHours = ArrayAdapter(this,android.R.layout.simple_spinner_item, ReminderActivity.hours)
            minuteArray = LinkedList(ReminderActivity.minutes)
        } else {
            val hour = LinkedList<String>()
            cal.time = appointment!!.date
            hour.add(cal.get(Calendar.HOUR_OF_DAY).toString())
            arrayAdapterHours = ArrayAdapter(this,android.R.layout.simple_spinner_item, hour)
            minuteArray.add(cal.get(Calendar.MINUTE).toString())
        }

        arrayAdapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        hourSpinnerAppointment.adapter = arrayAdapterHours

        val arrayAdapterMinutes = ArrayAdapter(this,android.R.layout.simple_spinner_item, minuteArray)
        arrayAdapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        minuteSpinnerAppointment.adapter = arrayAdapterMinutes

        if(isEditing){
            cal.time = appointment!!.date
            hourSpinnerAppointment.setSelection(cal.get(Calendar.HOUR_OF_DAY))
            minuteSpinnerAppointment.setSelection(cal.get(Calendar.MINUTE) / 15)
        }

    }

    private fun addOrEditAppointment(cal: Calendar) {
        if (checkValues()) {
            cal.set(Calendar.MINUTE,minuteArray[minuteSpinnerAppointment.selectedItemPosition].toInt())
            cal.set(Calendar.HOUR_OF_DAY,ReminderActivity.hours[hourSpinnerAppointment.selectedItemPosition].toInt())
            val newAppointment = Appointment(
                appointmentNameTV.text.toString(),
                cal.time,
                additionalNoteAppointment.text.toString()
            )
            if(appointment == null){
                if (UserInformation.addNewAppointment(newAppointment)) {
                    NotifyPlanner.planSingleAlarm(
                        this,
                        getSystemService(Context.ALARM_SERVICE) as AlarmManager,
                        newAppointment
                    )
                    finish()
                } else {
                    Toast.makeText(this, "Appuntamento già presente!", Toast.LENGTH_LONG).show()
                }
            } else {
                if(UserInformation.editAppointment(appointment!!.name,newAppointment)){
                    Utils.startNotifyService(this)
                    NotifyPlanner.remove(this,appointment!!)
                    NotifyPlanner.planSingleAlarm(
                        this,
                        getSystemService(Context.ALARM_SERVICE) as AlarmManager,
                        newAppointment
                    )
                    finish()
                } else {
                    Toast.makeText(this, "Appuntamento non modificato!", Toast.LENGTH_LONG).show()
                }
            }

        }
    }

    private fun setAllEnable(flag: Boolean){
        appointmentNameTV.isEnabled = flag
        hourSpinnerAppointment.isEnabled = flag
        minuteSpinnerAppointment.isEnabled = flag
        buttonDate.isEnabled = flag
        additionalNoteAppointment.isEnabled = flag
    }
}
