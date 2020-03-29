package com.pillskeeper.utility

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.pillskeeper.R
import com.pillskeeper.activity.FinishedMedicinesActivity
import com.pillskeeper.activity.LocationActivity
import com.pillskeeper.activity.LoginActivity
import com.pillskeeper.activity.PersonalInfoActivity
import com.pillskeeper.activity.appointment.AppointmentListActivity
import com.pillskeeper.activity.friend.FriendListActivity
import com.pillskeeper.activity.medicine.PillsListActivity

class Menu(
    toolbar: Toolbar,
    drawerLayout: DrawerLayout,
    navigationView: NavigationView,
    activity: AppCompatActivity
) : NavigationView.OnNavigationItemSelectedListener {

    val toolbar = toolbar
    val drawerLayout = drawerLayout
    val navigationView = navigationView
    val activity = activity
    var auth: FirebaseAuth = FirebaseAuth.getInstance()


    fun createMenu() {

        val toggle = ActionBarDrawerToggle(
            activity, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_profile -> {
                PersonalInfoActivity(activity, auth.currentUser?.uid.toString()).show()
            }
            R.id.nav_expiry_med -> {
                val intent =
                    Intent(activity.applicationContext, FinishedMedicinesActivity::class.java)
                intent.flags = FLAG_ACTIVITY_NEW_TASK
                activity.applicationContext.startActivity(intent)
            }
            R.id.nav_friends -> {
                val intent = Intent(activity.applicationContext, FriendListActivity::class.java)
                intent.flags = FLAG_ACTIVITY_NEW_TASK
                activity.applicationContext.startActivity(intent)
            }
            R.id.nav_medicines -> {
                val intent = Intent(activity.applicationContext, PillsListActivity::class.java)
                intent.flags = FLAG_ACTIVITY_NEW_TASK
                activity.applicationContext.startActivity(intent)
            }
            R.id.nav_appointments -> {
                val intent =
                    Intent(activity.applicationContext, AppointmentListActivity::class.java)
                intent.flags = FLAG_ACTIVITY_NEW_TASK
                activity.applicationContext.startActivity(intent)
            }

            R.id.nav_pharmacies -> {
                val intent = Intent(activity.applicationContext, LocationActivity::class.java)
                intent.flags = FLAG_ACTIVITY_NEW_TASK
                activity.applicationContext.startActivity(intent)
            }
            R.id.nav_logout -> {
                auth.signOut()
                activity.applicationContext.startActivity(
                    Intent(
                        activity.applicationContext,
                        LoginActivity::class.java
                    )
                )
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


}