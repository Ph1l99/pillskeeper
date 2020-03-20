package com.pillskeeper.activity.pills.reminder

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.pillskeeper.R
import kotlinx.android.synthetic.main.activity_advanced_info_reminder.*

class AdvancedInfoReminderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advanced_info_reminder)

        initSpinners()

        confirmButtonInfoRem.setOnClickListener {
            val it = Intent(this, ReminderActivity::class.java)
            //it.putExtra("pillName", null)
            setResult(Activity.RESULT_OK, it)
            finish()
        }

        deleteButton.setOnClickListener {
            finish()
        }

    }

    private fun initSpinners(){

        val values : ArrayList<String> = ArrayList()
        for(i in 1 .. 10)
            values.add(i.toString())

        val arrayAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item, values)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerPause.adapter = arrayAdapter
        consecutiveSpinner.adapter = arrayAdapter

    }


}
