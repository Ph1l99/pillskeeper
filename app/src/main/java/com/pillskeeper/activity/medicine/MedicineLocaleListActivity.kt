package com.pillskeeper.activity.medicine

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.pillskeeper.R
import com.pillskeeper.activity.medicine.reminder.ReminderListActivity
import com.pillskeeper.data.LocalMedicine
import com.pillskeeper.data.RemoteMedicine
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.utility.Menu
import com.pillskeeper.utility.adapter.MedicineLocaleCardAdapter
import com.pillskeeper.utility.adapter.MedicineRemoteCardAdapter
import kotlinx.android.synthetic.main.activity_medicines_list.*
import kotlinx.android.synthetic.main.content_pills_list.*
import kotlinx.android.synthetic.main.content_pills_list.recyclerView
import java.util.*

class MedicineLocaleListActivity : AppCompatActivity() {

    private lateinit var mAdapter: MedicineLocaleCardAdapter

    private lateinit var pillsArray: List<LocalMedicine>
    private var adapter: ArrayAdapter<String>? = null

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


        /*Listeners
        pills_list.setOnItemClickListener { _: AdapterView<*>, _: View, position: Int, _: Long ->

            //val it = Intent(this, MedicinesRemoteListActivity::class.java)
            //startActivity(it)

            val it = Intent(this, ReminderListActivity::class.java)
                .putExtra(ReminderListActivity.REMINDER_MEDICINE, UserInformation.medicines[position - 1])
            startActivity(it)

        }

        pills_list.setOnItemLongClickListener { _, _, position, _ ->
            val it = Intent(this, MedicineFormActivity::class.java)
                .putExtra(MedicineFormActivity.LOCAL_MEDICINE,UserInformation.medicines[position - 1])
            startActivity(it)

            /*GenericDeleteDialog(
                this,
                UserInformation.medicines[position - 1].name,
                DialogModeEnum.DELETE_MEDICINE
            ).show()*/

            return@setOnItemLongClickListener true
        }
         */

        addMedicineButton.setOnClickListener{
            val it = Intent(this, MedicinesRemoteListActivity::class.java)
            startActivity(it)
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        displayListMedicines()
    }

    /*
    private fun initList() {
        pillsArray = LinkedList()
        pillsArray.add(getText(R.string.addMedicineText).toString())

        UserInformation.medicines.forEach { entry -> pillsArray.add(entry.name) }

        adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pillsArray)
        pills_list.adapter = adapter
    }
     */

    private fun displayListMedicines() {
        pillsArray = UserInformation.medicines
        mAdapter = MedicineLocaleCardAdapter(pillsArray, true)
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        mAdapter.setOnItemClickListener {
            val intent = Intent(this, ReminderListActivity::class.java)
                .putExtra(ReminderListActivity.MEDICINE_POSITION, it-1)
            startActivity(intent)
        }
    }


}
