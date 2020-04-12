package com.pillskeeper.activity.medicine

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.pillskeeper.R
import com.pillskeeper.activity.GenericDeleteDialog
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.enums.DialogModeEnum
import com.pillskeeper.utility.Menu
import kotlinx.android.synthetic.main.content_pills_list.*
import java.util.*

class MedicineLocaleListActivity : AppCompatActivity() {

    private lateinit var pillsArray: LinkedList<String>
    private var adapter: ArrayAdapter<String>? = null

    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pills_list)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        //set toolbar
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        initList()

        val menu = Menu(toolbar, drawerLayout, navView, this)
        menu.createMenu()


        //Listeners
        pills_list.setOnItemClickListener { _: AdapterView<*>, _: View, position: Int, _: Long ->
            if (position == 0) {
                val it = Intent(this, MedicinesRemoteListActivity::class.java)
                startActivity(it)
            } else {
                val it = Intent(this, MedicineFormActivity::class.java)
                    .putExtra(MedicineFormActivity.LOCAL_MEDICINE,UserInformation.medicines[position - 1])
                startActivity(it)
            }
        }

        pills_list.setOnItemLongClickListener { _, _, position, _ ->
            if(position > 0)
                GenericDeleteDialog(
                    this,
                    UserInformation.medicines[position - 1].name,
                    DialogModeEnum.DELETE_MEDICINE
                ).show()

            return@setOnItemLongClickListener true
        }


    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        initList()
    }

    private fun initList() {
        pillsArray = LinkedList()
        pillsArray.add(getText(R.string.addMedicineText).toString())

        UserInformation.medicines.forEach { entry -> pillsArray.add(entry.name) }

        adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pillsArray)
        pills_list.adapter = adapter
    }
}
