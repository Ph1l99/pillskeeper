package com.pillskeeper.activity.pills.reminder

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pillskeeper.R
import kotlinx.android.synthetic.main.activity_reminder.*

class ReminderActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener  {

    companion object{
        val hours: ArrayList<String> = arrayListOf("00", "01", "02", "03", "04","05","06","07","08","09"
            ,"10","11","12","13","14","15","16","17","18","19","20","21","22","23")
        val minutes: ArrayList<String> = arrayListOf("00", "15", "30", "45")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)


        initSpinner()
    }

    private fun initSpinner(){

        val arrayAdapterHours = ArrayAdapter(this,android.R.layout.simple_spinner_item, hours)
        arrayAdapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerHours.adapter = arrayAdapterHours

        val arrayAdapterMinutes = ArrayAdapter(this,android.R.layout.simple_spinner_item, minutes)
        arrayAdapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerMinutes.adapter = arrayAdapterMinutes
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Toast.makeText(this, parent!!.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show()
    }

}
