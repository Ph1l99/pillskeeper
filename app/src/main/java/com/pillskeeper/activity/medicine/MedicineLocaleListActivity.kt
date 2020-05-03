package com.pillskeeper.activity.medicine

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.pillskeeper.R
import com.pillskeeper.activity.medicine.reminder.ReminderListActivity
import com.pillskeeper.data.LocalMedicine
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.utility.Menu
import com.pillskeeper.utility.adapter.LocalMedicineAdapter
import kotlinx.android.synthetic.main.content_pills_list.*

class MedicineLocaleListActivity : AppCompatActivity() {

    private lateinit var mAdapter: LocalMedicineAdapter

    private lateinit var pillsArray: List<LocalMedicine>

    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var addMedicineButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pills_list)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        addMedicineButton = findViewById(R.id.addMedicineButton)

        //set toolbar
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        //initList()
        displayListMedicines()

        val menu = Menu(toolbar, drawerLayout, navView, this)
        menu.createMenu()

        addMedicineButton.setOnClickListener {
            val intent = Intent(this, MedicinesRemoteListActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        displayListMedicines()
    }

    private fun displayListMedicines() {
        pillsArray = UserInformation.medicines
        mAdapter = LocalMedicineAdapter(pillsArray, true)
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        mAdapter.onItemClick = {
            it
            val intent = Intent(this, ReminderListActivity::class.java)
                .putExtra(
                    ReminderListActivity.MEDICINE_NAME,
                    it.name
                )
            startActivity(intent)
        }
    }


}
