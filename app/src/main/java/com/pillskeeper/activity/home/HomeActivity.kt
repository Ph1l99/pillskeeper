package com.pillskeeper.activity.home


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.pillskeeper.R
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.utility.Menu
import com.pillskeeper.utility.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.view.*

class HomeActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var tabLayout: TabLayout

    private lateinit var viewPager: ViewPager

    private val VIEW_PAGER_ID = 2222

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        UserInformation.context = this
        Utils.stdLayout = EditText(this).background

        val menu = Menu(toolbar, drawerLayout, navView, this)
        menu.createMenu()
        Utils.insertNameMenu(findViewById(R.id.nav_view))

        /* fragment view */
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = ViewPager(this)
        viewPager.id = VIEW_PAGER_ID
        contentMainLayout.relativeLayout.addView(viewPager)
        val adapter = HomeAdapter(supportFragmentManager)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))


        /*LISTENERS*/
        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tab?.position == HomeAdapter.PILLS_PAGE){
                    viewPager.currentItem = HomeAdapter.PILLS_PAGE
                } else {
                    viewPager.currentItem = HomeAdapter.APPOINTMENTS_PAGE
                }
            }
        })

        //todo fare un activity per questa parte di codice qui(Strettamente necessario, semplice cn bottone si e no)
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        if(powerManager.isIgnoringBatteryOptimizations(packageName)){
            Toast.makeText(this, "NON OTTIMIZZATA (bene)" ,Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "OTTIMIZZATA (male)" ,Toast.LENGTH_LONG).show()
            val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
            startActivity(intent)
        }
    }
}