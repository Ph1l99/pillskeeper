package com.pillskeeper.activity.medicine.reminder.reminderformfragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.pillskeeper.R
import com.pillskeeper.activity.medicine.medicineformfragments.FormAdapter
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.utility.Utils.hours
import java.util.*
import kotlin.collections.ArrayList

class FormReminderOneDayTimeFrag(private val viewPager: ViewPager?, private val medName: String?) : Fragment()  {

    private var dateSelected: Date? = null

    private lateinit var buttonDateReminder: Button
    private lateinit var hourReminderSpinner: Spinner
    private lateinit var minutesReminderSpinner: Spinner
    private lateinit var textViewNext: TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_day_reminder_time, container, false)

        buttonDateReminder = view.findViewById(R.id.buttonDateReminder)
        hourReminderSpinner = view.findViewById(R.id.hourReminderSpinner)
        minutesReminderSpinner = view.findViewById(R.id.minutesReminderSpinner)
        textViewNext = view.findViewById(R.id.textViewNext)

        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        if(FormAdapter.isAReminderEditing){
            val calEnd = Calendar.getInstance()
            calEnd.time = FormAdapter.startDay!!
            buttonDateReminder.text = getString(R.string.dateButtonFormatted, calEnd.get(Calendar.DAY_OF_MONTH),calEnd.get(Calendar.MONTH) + 1,calEnd.get(Calendar.YEAR))
            FormAdapter.startDay = calEnd.time
            dateSelected = FormAdapter.startDay

            val arrayAdapterHours = ArrayAdapter(requireActivity(),android.R.layout.simple_spinner_item, hours)
            arrayAdapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            hourReminderSpinner.adapter = arrayAdapterHours
            hourReminderSpinner.setSelection(FormAdapter.reminderHour)

            val minutesArray = ArrayList<String>()
            for (i in 0..11)
                minutesArray.add(if(i < 2) "0${i*5}" else "${i*5}")

            val arrayAdapterMinutes = ArrayAdapter(activity?.applicationContext!!,android.R.layout.simple_spinner_item, minutesArray)
            arrayAdapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            minutesReminderSpinner.adapter = arrayAdapterMinutes
            minutesReminderSpinner.setSelection(FormAdapter.reminderMinute/5)
        } else {
            initSpinner()
        }

        buttonDateReminder.setOnClickListener {
            DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                buttonDateReminder.text = getString(R.string.dateButtonFormatted,dayOfMonth,monthOfYear+1,year)

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

        /*
        saveButtonReminder.setOnClickListener {

            if(dateSelected != null && dosageQtyReminder.selectedItem.toString().toFloat() > 0){
                cal.time = dateSelected!!
                cal.set(Calendar.HOUR_OF_DAY, hourReminderSpinner.selectedItem.toString().toInt())
                cal.set(Calendar.MINUTE, minutesReminderSpinner.selectedItem.toString().toInt())
                if (cal.time > Date()){
                    val reminder = ReminderMedicine(
                        dosageQtyReminder.selectedItem.toString().toFloat(),
                        minutesReminderSpinner.selectedItem.toString().toInt(),
                        hourReminderSpinner.selectedItem.toString().toInt(),
                        cal.time,
                        null,
                        cal.time,
                        reminderAddNotesEditT.text.toString()
                    )
                    if(viewPager != null) {
                        FormAdapter.addReminder(reminder)
                        viewPager.currentItem = FormAdapter.FORM_SAVE_OR_REMINDER
                    } else {
                        if (UserInformation.addNewReminder(medName!!,reminder)) {
                            Utils.getSingleReminderListNormalized(
                                medName,
                                UserInformation.getSpecificMedicine(medName)!!.medicineType,
                                reminder
                            ).forEach {
                                NotifyPlanner.planSingleAlarm(
                                    activity!!,
                                    activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager,
                                    it
                                )
                            }
                            activity?.finish()
                        }
                    }
                } else {
                    Toast.makeText(UserInformation.context,"Perfavore inserire informazioni corrette!",Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(UserInformation.context,"Perfavore inserire informazioni corrette!",Toast.LENGTH_LONG).show()
            }
        }

         */


        textViewNext.setOnClickListener {
            if(dateSelected != null) {

                cal.time = dateSelected!!
                cal.set(Calendar.HOUR_OF_DAY, hourReminderSpinner.selectedItem.toString().toInt())
                cal.set(Calendar.MINUTE, minutesReminderSpinner.selectedItem.toString().toInt())

                    if(cal.time >= Date()) {
                        FormAdapter.startDay = cal.time
                        FormAdapter.finishDay = cal.time

                        FormAdapter.reminderHour = hourReminderSpinner.selectedItem.toString().toInt()
                        FormAdapter.reminderMinute = minutesReminderSpinner.selectedItem.toString().toInt()

                        if (viewPager != null) {
                            viewPager.currentItem = FormAdapter.FORM_ONE_DAY_REMINDER_QUANTITY
                        }
                    } else {
                        Toast.makeText(UserInformation.context,"Per favore inserire informazioni corrette!",Toast.LENGTH_LONG).show()
                    }

            } else {
                Toast.makeText(UserInformation.context,"Per favore inserire informazioni corrette!",Toast.LENGTH_LONG).show()
            }
        }

        return view
    }

    private fun initSpinner(){

        val arrayAdapterHours = ArrayAdapter(UserInformation.context,android.R.layout.simple_spinner_item, hours)
        arrayAdapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        hourReminderSpinner.adapter = arrayAdapterHours


        val minutesArray = ArrayList<String>()
        for (i in 0..12)
            minutesArray.add(if(i < 2) "0${i*5}" else "${i*5}")

        val arrayAdapterMinutes = ArrayAdapter(UserInformation.context,android.R.layout.simple_spinner_item, minutesArray)
        arrayAdapterMinutes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        minutesReminderSpinner.adapter = arrayAdapterMinutes

    }
}