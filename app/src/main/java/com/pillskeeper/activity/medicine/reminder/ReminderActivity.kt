package com.pillskeeper.activity.medicine.reminder

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.pillskeeper.R
import com.pillskeeper.utility.Utils
import kotlinx.android.synthetic.main.activity_reminder.*
import java.util.*

class ReminderActivity : AppCompatActivity()  {

    companion object{
        val hours: ArrayList<String> = arrayListOf("00", "01", "02", "03", "04","05","06","07","08","09"
            ,"10","11","12","13","14","15","16","17","18","19","20","21","22","23")
        val minutes: ArrayList<String> = arrayListOf("00", "15", "30", "45")
    }

    private var dateSelected: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        val cal = Calendar.getInstance()

        initSpinner()

        buttonDeleteRem.setOnClickListener { finish() }

        buttonConfirmRem.setOnClickListener {

        }


        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        buttonDateEnd.setOnClickListener {
            DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                buttonDateEnd.text = getString(R.string.dateButtonFormatted,dayOfMonth,monthOfYear+1,year)

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

    }

    private fun addOrEditReminder(){



    }

    private fun checkValue(): Boolean{
        var result = true


        if(dateSelected != null)
            if (!Utils.checkDate(dateSelected!!, this))
                result = false


        if(editTextQuantityRem.text.toString().isEmpty()){
            result = false
            Utils.errorEditText(editTextQuantityRem)
        }

        //if (radioGroup)

        return result
    }

    private fun initSpinner(){

        val arrayAdapterHours = ArrayAdapter(this,android.R.layout.simple_spinner_item, hours)
        arrayAdapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerHours.adapter = arrayAdapterHours

        val arrayAdapterMinutes = ArrayAdapter(this,android.R.layout.simple_spinner_item, minutes)
        arrayAdapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerMinutes.adapter = arrayAdapterMinutes
    }

}
