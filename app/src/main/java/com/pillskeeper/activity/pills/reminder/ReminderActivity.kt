package com.pillskeeper.activity.pills.reminder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.pillskeeper.R
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.enums.RelationEnum
import kotlinx.android.synthetic.main.activity_new_friend.*
import kotlinx.android.synthetic.main.activity_reminder.*

class ReminderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        initSpinner()
    }

    private fun initSpinner(){
        val hours: ArrayList<String> = arrayListOf("00", "01", "02", "03", "04","05","06","07","08","09"
            ,"10","11","12","13","14","15","16","17","18","19","20","21","22","23")
        val minutes: ArrayList<String> = arrayListOf("00", "15", "30", "45")

        val arrayAdapterHours = ArrayAdapter(applicationContext,android.R.layout.simple_spinner_item, hours)
        arrayAdapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerHours.adapter = arrayAdapterHours

        val arrayAdapterMinutes = ArrayAdapter(applicationContext,android.R.layout.simple_spinner_item, minutes)
        arrayAdapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerMinutes.adapter = arrayAdapterMinutes
    }
}

//TODO USARE I CHECKBOX
