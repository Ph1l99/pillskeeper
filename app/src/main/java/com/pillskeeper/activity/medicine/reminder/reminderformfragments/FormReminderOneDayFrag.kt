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
import com.pillskeeper.data.ReminderMedicine
import com.pillskeeper.datamanager.UserInformation
import java.util.*
import kotlin.collections.ArrayList

class FormReminderOneDayFrag(
    private val viewPager: ViewPager?,
    private val medName: String?
) : Fragment()  {

    private var dateSelected: Date? = null

    private lateinit var buttonDateReminder: Button
    private lateinit var abortButtonReminder: Button
    private lateinit var saveButtonReminder: Button
    private lateinit var hourReminderSpinner: Spinner
    private lateinit var minutesReminderSpinner: Spinner
    private lateinit var dosageQtyReminder: Spinner
    private lateinit var reminderAddNotesEditT: EditText


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_day_reminder, container, false)

        buttonDateReminder = view.findViewById(R.id.buttonDateReminder)
        abortButtonReminder = view.findViewById(R.id.abortButtonReminder)
        saveButtonReminder = view.findViewById(R.id.saveButtonReminder)
        hourReminderSpinner = view.findViewById(R.id.hourReminderSpinner)
        minutesReminderSpinner = view.findViewById(R.id.minutesReminderSpinner)
        dosageQtyReminder = view.findViewById(R.id.dosageQtyReminder)
        reminderAddNotesEditT = view.findViewById(R.id.reminderAddNotesEditT)

        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        initSpinner()

        buttonDateReminder.setOnClickListener {
            DatePickerDialog(activity!!, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
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

        saveButtonReminder.setOnClickListener {

            if(dateSelected != null && dosageQtyReminder.selectedItem.toString().toFloat() > 0){
                cal.time = dateSelected!!
                cal.set(Calendar.HOUR_OF_DAY, hourReminderSpinner.selectedItem.toString().toInt())
                cal.set(Calendar.MINUTE, minutesReminderSpinner.selectedItem.toString().toInt())
                if (cal.time > Date()){
                    val reminder = ReminderMedicine(
                        dosageQtyReminder.selectedItem.toString().toFloat(),
                        minutesReminderSpinner.selectedItem.toString().toInt(),//TODO can be null
                        hourReminderSpinner.selectedItem.toString().toInt(),// TODO can be null
                        cal.time,
                        null,
                        cal.time,
                        reminderAddNotesEditT.text.toString()
                    )
                    if(viewPager != null) {
                        FormAdapter.addReminder(reminder)
                        viewPager.currentItem = FormAdapter.FORM_SAVE_OR_REMINDER
                    } else {
                        UserInformation.addNewReminder(medName!!,reminder)
                        activity?.finish()
                    }
                } else {
                    Toast.makeText(UserInformation.context,"Perfavore inserire informazioni corrette!",Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(UserInformation.context,"Perfavore inserire informazioni corrette!",Toast.LENGTH_LONG).show()
            }
        }

        abortButtonReminder.setOnClickListener {
            if(viewPager != null)
                viewPager.currentItem = FormAdapter.FORM_SAVE_OR_REMINDER
            else
                activity?.finish()
        }

        return view
    }

    private fun initSpinner(){

        val arrayAdapterHours = ArrayAdapter(UserInformation.context,android.R.layout.simple_spinner_item, ReminderActivity.hours)
        arrayAdapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        hourReminderSpinner.adapter = arrayAdapterHours


        val minutesArray = ArrayList<String>()
        for (i in 0..12)
            minutesArray.add(if(i < 2) "0${i*5}" else "${i*5}")

        val arrayAdapterMinutes = ArrayAdapter(UserInformation.context,android.R.layout.simple_spinner_item, minutesArray)
        arrayAdapterMinutes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        minutesReminderSpinner.adapter = arrayAdapterMinutes


        val qtyArray = ArrayList<String>()
        for (i in 0..10)
            qtyArray.add("${i/2F}")

        val arrayAdapterDosage = ArrayAdapter(UserInformation.context,android.R.layout.simple_spinner_item, qtyArray)
        arrayAdapterDosage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dosageQtyReminder.adapter = arrayAdapterDosage

    }
}