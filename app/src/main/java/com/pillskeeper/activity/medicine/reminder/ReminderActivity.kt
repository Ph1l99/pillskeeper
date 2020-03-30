package com.pillskeeper.activity.medicine.reminder

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pillskeeper.R
import com.pillskeeper.activity.medicine.PillsFormActivity
import com.pillskeeper.data.ReminderMedicine
import com.pillskeeper.enums.DaysEnum
import com.pillskeeper.utility.Utils
import kotlinx.android.synthetic.main.activity_reminder.*
import java.util.*

class ReminderActivity : AppCompatActivity()  {

    companion object{
        val hours: ArrayList<String> = arrayListOf("00", "01", "02", "03", "04","05","06","07","08","09"
            ,"10","11","12","13","14","15","16","17","18","19","20","21","22","23")
        val minutes: ArrayList<String> = arrayListOf("00", "15", "30", "45")
    }

    private var isEditing = false
    private var expDateSelected: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        val cal = Calendar.getInstance()

        initSpinner()



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

                expDateSelected = cal.time

            }, year, month, day).show()
        }

        buttonDeleteRem.setOnClickListener { finish() }

        buttonConfirmRem.setOnClickListener {
            addOrEditReminder()
        }
    }

    private fun addOrEditReminder(){
        val days: LinkedList<DaysEnum> = buildDaysArray()

        if(checkValue(days)){

            val cal = Calendar.getInstance()
            cal.time = Date()
            cal.set(Calendar.MINUTE,0)
            cal.set(Calendar.SECOND,0)
            cal.set(Calendar.MILLISECOND,0)
            cal.set(Calendar.HOUR_OF_DAY,0)

            val reminder = ReminderMedicine(
                editTextQuantityRem.text.toString().toFloat(),
                spinnerMinutesRem.selectedItem.toString().toInt(),
                spinnerHoursRem.selectedItem.toString().toInt(),
                cal.time,
                days,
                expDateSelected,
                editTextAddNotesRem.text.toString()
            )

            val it = Intent(this, PillsFormActivity::class.java)
            it.putExtra(PillsFormActivity.REMINDER, reminder)
            setResult(Activity.RESULT_OK, it)
            finish()
        }
    }

    private fun buildDaysArray(): LinkedList<DaysEnum>{
        val days: LinkedList<DaysEnum> = LinkedList()
        if (checkBoxMonday.isChecked)
            days.add(DaysEnum.MON)
        if (checkBoxTuesday.isChecked)
            days.add(DaysEnum.TUE)
        if (checkBoxWednesday.isChecked)
            days.add(DaysEnum.WED)
        if (checkBoxThursday.isChecked)
            days.add(DaysEnum.THU)
        if (checkBoxFriday.isChecked)
            days.add(DaysEnum.FRI)
        if (checkBoxSaturday.isChecked)
            days.add(DaysEnum.SAT)
        if (checkBoxSunday.isChecked)
            days.add(DaysEnum.SUN)
        return days
    }

    private fun checkValue(days: LinkedList<DaysEnum>): Boolean{
        var result = true


        if(expDateSelected != null)
            if (!Utils.checkDate(expDateSelected!!, this))
                result = false


        if(editTextQuantityRem.text.toString().isEmpty()){
            result = false
            Utils.errorEditText(editTextQuantityRem)
        }

        if (days.size == 0){
            result = false
            Toast.makeText(this,"Perfavore selezionare almeno un giorno della settimana",Toast.LENGTH_LONG).show()
        }

        return result
    }

    private fun initSpinner(){

        val arrayAdapterHours = ArrayAdapter(this,android.R.layout.simple_spinner_item, hours)
        arrayAdapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerHoursRem.adapter = arrayAdapterHours

        val arrayAdapterMinutes = ArrayAdapter(this,android.R.layout.simple_spinner_item, minutes)
        arrayAdapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerMinutesRem.adapter = arrayAdapterMinutes
    }

}
