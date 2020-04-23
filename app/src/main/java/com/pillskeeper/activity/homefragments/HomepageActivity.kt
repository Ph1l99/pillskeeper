package com.pillskeeper.activity.homefragments


import android.app.AlarmManager
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.pillskeeper.R
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.notifier.NotifyPlanner
import com.pillskeeper.utility.Menu
import com.pillskeeper.utility.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.view.*

class HomepageActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {

    //private lateinit var appointmentListSorted: LinkedList<Appointment>
    //private lateinit var reminderListSorted: LinkedList<ReminderMedicineSort>

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
        val adapter = Adapter(supportFragmentManager)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))


        /*LISTENERS*/
        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tab?.position == Adapter.PILLS_PAGE){
                    viewPager.currentItem = Adapter.PILLS_PAGE
                } else {
                    viewPager.currentItem = Adapter.APPOINTMENTS_PAGE
                }
            }
        })

        NotifyPlanner.testPlanner(
            this,
            getSystemService(Context.ALARM_SERVICE) as AlarmManager
        )


        /*
        appointmentListMain.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, AppointmentFormActivity::class.java)
                .putExtra(APPOINTMENT_VALUE, appointmentListSorted[position])
            startActivity(intent)
        }

        appointmentListMain.setOnItemLongClickListener { _, _, position, _ ->
            GenericDeleteDialog(
                this,
                appointmentListSorted[position].name,
                DialogModeEnum.DELETE_APPOINTMENT
            ).show()
            return@setOnItemLongClickListener true
        }
       */
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        TODO("Not yet implemented")
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        TODO("Not yet implemented")
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        if(tab?.position == Adapter.PILLS_PAGE){
            viewPager.currentItem = Adapter.PILLS_PAGE
        } else {
            viewPager.currentItem = Adapter.APPOINTMENTS_PAGE
        }
    }


    /*
    override fun onResume() {
        super.onResume()
        initLists()
    }




    private fun initLists(filterDate: Date = Date()) {
        filterDate.time = Utils.dataNormalizationLimit(filterDate)

        /*Appointment list*/
        UserInformation.appointments.sortWith(compareBy { it.date })
        appointmentListSorted = UserInformation.appointments
        appointmentListSorted = LinkedList(appointmentListSorted.filter { it.date <= filterDate })
        val arrayAdapterAppointments = LinkedList<String>()
        appointmentListSorted.forEach { arrayAdapterAppointments.add(formatOutputString(it)) }
        appointmentListMain.adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayAdapterAppointments)

        /*Reminder List*/
        reminderListSorted = Utils.getSortedListReminders(filterDate)
        val arrayAdapterReminders = LinkedList<String>()
        reminderListSorted.forEach { arrayAdapterReminders.add(formatOutputString(it)) }
        reminderListMain.adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayAdapterReminders)

    }

    private fun formatOutputString(item: Any): String {
        val cal: Calendar = Calendar.getInstance()
        when (item) {
            is ReminderMedicineSort -> {
                cal.time = item.reminder.startingDay
                var text =
                    "${item.medName}  -  ${item.reminder.dosage} ${getText(item.medType.text)} - "
                text += if (item.reminder.hours < 10) "0${item.reminder.hours}" else item.reminder.hours
                text += ":"
                text += if (item.reminder.minutes < 10) "0${item.reminder.minutes}" else item.reminder.minutes
                text += "  ${cal.get(Calendar.DAY_OF_MONTH)}/${cal.get(Calendar.MONTH) + 1}"
                return text
            }
            is Appointment -> {
                cal.time = item.date
                var text =
                    "${item.name} - ${cal.get(Calendar.DAY_OF_MONTH)}/${cal.get(Calendar.MONTH) + 1}  "
                text += "${cal.get(Calendar.HOUR_OF_DAY)}:"
                text += if (cal.get(Calendar.MINUTE) < 10) "0${cal.get(Calendar.MINUTE)}" else cal.get(
                    Calendar.MINUTE
                )
                return text
            }
            else -> return ""
        }

    }

     */
}