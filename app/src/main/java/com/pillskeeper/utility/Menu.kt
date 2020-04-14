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
import com.pillskeeper.activity.DangerActivity
import com.pillskeeper.activity.PersonalInfoDialog
import com.pillskeeper.activity.appointment.AppointmentListActivity
import com.pillskeeper.activity.friend.FriendListActivity
import com.pillskeeper.activity.homefragments.HomepageActivity
import com.pillskeeper.activity.medicine.FinishedMedicinesActivity
import com.pillskeeper.activity.medicine.MedicineLocaleListActivity
import com.pillskeeper.activity.registration.LoginActivity
import com.pillskeeper.datamanager.FirebaseAuthenticationManager

class Menu(
    private val toolbar: Toolbar,
    private val drawerLayout: DrawerLayout,
    private val navigationView: NavigationView,
    val activity: AppCompatActivity
) : NavigationView.OnNavigationItemSelectedListener {


    fun createMenu() {

        val toggle = ActionBarDrawerToggle(
            activity, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        var changed = true

        when (item.itemId) {
            R.id.nav_danger -> {
                if (activity is DangerActivity) {
                    changed = false
                } else {
                    activity.applicationContext.startActivity(
                        Intent(
                            activity.applicationContext,
                            DangerActivity::class.java
                        )
                    )
                }
            }
            R.id.nav_profile -> {
                FirebaseAuthenticationManager.getCurrentUser()?.uid?.let {
                    PersonalInfoDialog(
                        activity,
                        it
                    ).show()
                }
            }
            R.id.nav_expiry_med -> {
                if (activity is FinishedMedicinesActivity) {
                    changed = false
                } else {
                    val intent =
                        Intent(activity.applicationContext, FinishedMedicinesActivity::class.java)
                    intent.flags = FLAG_ACTIVITY_NEW_TASK
                    activity.applicationContext.startActivity(intent)
                }
            }
            R.id.nav_friends -> {
                if (activity is FriendListActivity) {
                    changed = false
                } else {
                    val intent = Intent(activity.applicationContext, FriendListActivity::class.java)
                    intent.flags = FLAG_ACTIVITY_NEW_TASK
                    activity.applicationContext.startActivity(intent)
                }
            }
            R.id.nav_medicines -> {
                if (activity is MedicineLocaleListActivity) {
                    changed = false
                } else {
                    val intent =
                        Intent(activity.applicationContext, MedicineLocaleListActivity::class.java)
                    intent.flags = FLAG_ACTIVITY_NEW_TASK
                    activity.applicationContext.startActivity(intent)
                }
            }
            R.id.nav_appointments -> {
                if (activity is AppointmentListActivity) {
                    changed = false
                } else {
                    val intent =
                        Intent(activity.applicationContext, AppointmentListActivity::class.java)
                    intent.flags = FLAG_ACTIVITY_NEW_TASK
                    activity.applicationContext.startActivity(intent)
                }
            }

            R.id.nav_pharmacies -> {
                Utils.openMaps(activity, activity.applicationContext)
            }
            R.id.nav_logout -> {
                FirebaseAuthenticationManager.signOut()
                activity.applicationContext.startActivity(
                    Intent(
                        activity.applicationContext,
                        LoginActivity::class.java
                    )
                )
            }
        }
        if (changed) {
            if (activity !is HomepageActivity)
                activity.finish()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}