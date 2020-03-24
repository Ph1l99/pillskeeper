package com.pillskeeper.activity.appointment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import com.pillskeeper.R
import com.pillskeeper.datamanager.UserInformation
import kotlinx.android.synthetic.main.activity_appointment_list.*

class AppointmentListActivity : AppCompatActivity() {

    private lateinit var appointmentListView : ListView
    private lateinit var listAppointmentName : ArrayList<String>
    private lateinit var adapter : ArrayAdapter<String>

    companion object {
        const val APPOINTMENT_VALUE = "AppointmentMode"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_list)

        initList()

        addAppointmentFab.setOnClickListener {
            val intent = Intent(this,AppointmentFormActivity::class.java)
            startActivity(intent)
        }

        appointmentListView.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this,AppointmentFormActivity::class.java)
                .putExtra(APPOINTMENT_VALUE,UserInformation.appointments[position])
            startActivity(intent)
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        initList()
    }

    private fun initList(){
        Log.i(Log.DEBUG.toString(), "AppointmentListActivity - initList() Started")

        appointmentListView = findViewById(R.id.appointmentListView)
        listAppointmentName = ArrayList(UserInformation.appointments.size)

        for (appointment in UserInformation.appointments) {
            listAppointmentName.add(appointment.name)
        }

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listAppointmentName)
        appointmentListView.adapter = adapter

        Log.i(Log.DEBUG.toString(), "AppointmentListActivity - initList() Ended")
    }

}
