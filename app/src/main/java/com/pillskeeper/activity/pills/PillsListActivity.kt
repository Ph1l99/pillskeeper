package com.pillskeeper.activity.pills

import android.app.Activity
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
import com.pillskeeper.activity.MainActivity
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.utility.Menu
import kotlinx.android.synthetic.main.content_pills_list.*
import java.util.*

class PillsListActivity : AppCompatActivity() {

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
            //TODO scrivere cosa fare sul click degli itemssss
            if (position == 0) {
                val it = Intent(this, MedicinesListActivity::class.java)
                startActivityForResult(it, 0)
            } else {
                val it = Intent(this, PillsFormActivity::class.java)
                    .putExtra(PillsFormActivity.LOCAL_MEDICINE,UserInformation.medicines[position])
                startActivityForResult(it, 0)
            }
        }


    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        initList()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == MainActivity.START_FIRST_LOGIN_ACTIVITY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val pillName: String = data!!.getStringExtra("pillName")
                pillsArray.add(pillName)
                adapter!!.notifyDataSetChanged()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun initList() {
        pillsArray = LinkedList()
        pillsArray.add("+ nuova medicina")

        UserInformation.medicines.forEach { entry -> pillsArray.add(entry.name) }

        adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pillsArray)
        pills_list.adapter = adapter
    }

/*
    private fun createMenu() {
        //creo il menu


        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)

        // username_text_view_menu.text = LocalDatabase.readUsername()+""
    }


    //metodo per il menu
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_profile -> {
                Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
                //TODO aprire activity modifica profilo
            }
            R.id.nav_friends -> startActivity(Intent(this, FriendListActivity::class.java))
            R.id.nav_medicines -> startActivity(Intent(this, PillsListActivity::class.java))
            R.id.nav_pharmacies -> {
                Toast.makeText(this, "Pharmacies clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_logout -> {
                Toast.makeText(this, "Logout clicked", Toast.LENGTH_SHORT).show()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

 */


}
