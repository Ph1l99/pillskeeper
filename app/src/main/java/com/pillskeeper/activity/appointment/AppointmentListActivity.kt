package com.pillskeeper.activity.appointment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.pillskeeper.R
import com.pillskeeper.activity.GenericDeleteDialog
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.enums.DialogModeEnum
import com.pillskeeper.utility.Menu
import kotlinx.android.synthetic.main.content_appointement_list.*

class AppointmentListActivity : AppCompatActivity() {

    private lateinit var appointmentListView : ListView
    private lateinit var listAppointmentName : ArrayList<String>
    private lateinit var adapter : ArrayAdapter<String>

    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    companion object {
        const val APPOINTMENT_VALUE = "AppointmentMode"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_list)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        //set toolbar
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        //set menu
        val menu = Menu(toolbar, drawerLayout, navView, this)
        menu.createMenu()

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

        appointmentListView.setOnItemLongClickListener { _, _, position, _ ->
            GenericDeleteDialog(
                this,
                UserInformation.appointments[position].name,
                DialogModeEnum.DELETE_APPOINTMENT
            ).show()
            return@setOnItemLongClickListener true
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
