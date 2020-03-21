package com.pillskeeper.activity.appointment

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pillskeeper.R
import kotlinx.android.synthetic.main.activity_appointment.*
import java.util.*

class AppointmentActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment)

        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

                /*

        button.setOnClickListener {
            DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                textView.text = cal.time.toString()
            }, year, month, day).show()
        }

                 */


    }




}
