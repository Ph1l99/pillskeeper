package com.pillskeeper.activity


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.pillskeeper.R
import com.pillskeeper.activity.appointment.AppointmentDialog
import com.pillskeeper.activity.appointment.AppointmentFormActivity
import com.pillskeeper.activity.appointment.AppointmentListActivity.Companion.APPOINTMENT_VALUE
import com.pillskeeper.data.*
import com.pillskeeper.datamanager.DatabaseManager
import com.pillskeeper.datamanager.LocalDatabase
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.enums.DaysEnum
import com.pillskeeper.enums.MedicineTypeEnum
import com.pillskeeper.enums.RelationEnum
import com.pillskeeper.notifier.WorkerStarter
import com.pillskeeper.utility.Mail
import com.pillskeeper.utility.Menu
import com.pillskeeper.utility.Utils
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header.*
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val START_FIRST_LOGIN_ACTIVITY_CODE = 0
    }

    private lateinit var appointmentListSorted: LinkedList<Appointment>
    private lateinit var reminderListSorted: LinkedList<ReminderMedicineSort>

    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        UserInformation.context = this
        FirebaseApp.initializeApp(this)

        Utils.stdLayout = EditText(this).background

        val menu = Menu(toolbar, drawerLayout, navView, this)
        menu.createMenu()
        val nav: NavigationView = findViewById(R.id.nav_view)
        val header: View = nav.getHeaderView(0)
        var username = header.findViewById<TextView>(R.id.username_text_view_menu)
        val user = LocalDatabase.readUser()
        //TODO FILIPPO SISTEMA QUESTO
        if (user == null) {
            username.text = "Utente non settato!!!"
        } else {
            username.text = user.name + " " + user.surname
        }

        //TODO DEBUG - to be removed
        funTest()
        sendMailTest()

        appointmentListMain.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, AppointmentFormActivity::class.java)
                .putExtra(APPOINTMENT_VALUE, appointmentListSorted[position])
            startActivity(intent)
        }

        appointmentListMain.setOnItemLongClickListener { _, _, position, _ ->
            AppointmentDialog(this, appointmentListSorted[position].name).show()
            return@setOnItemLongClickListener true
        }

        WorkerStarter.startNotifierThread(this)
    }

    private fun funTest() {

        val days1: LinkedList<DaysEnum> = LinkedList()
        days1.add(DaysEnum.MON)
        days1.add(DaysEnum.FRI)
        days1.add(DaysEnum.SAT)
        days1.add(DaysEnum.TUE)
        days1.add(DaysEnum.THU)
        days1.add(DaysEnum.SUN)
        days1.add(DaysEnum.WED)

        val days2: LinkedList<DaysEnum> = LinkedList()
        days2.add(DaysEnum.MON)
        days2.add(DaysEnum.FRI)
        days2.add(DaysEnum.SAT)
        days2.add(DaysEnum.TUE)
        days2.add(DaysEnum.THU)
        days2.add(DaysEnum.SUN)
        days2.add(DaysEnum.WED)

        val reminders = LinkedList<ReminderMedicine>()
        reminders.add(ReminderMedicine(1.5F, 0, 20, Date(), days1, null, null))
        reminders.add(ReminderMedicine(1F, 0, 19, Date(), days2, null, null))
        UserInformation.addNewMedicine(
            LocalMedicine(
                "Tachipirina",
                MedicineTypeEnum.PILLS,
                24F,
                24F,
                reminders,
                "Tachipirina"
            )
        )

        reminders.add(ReminderMedicine(1.5F, 0, 20, Date(), days1, null, null))
        reminders.add(ReminderMedicine(1F, 0, 19, Date(), days2, null, null))
        UserInformation.addNewMedicine(
            LocalMedicine(
                "Tachipirina2",
                MedicineTypeEnum.PILLS,
                24F,
                24F,
                reminders,
                "Tachipirina2"
            )
        )


        val reminders2 = LinkedList<ReminderMedicine>()
        reminders2.add(ReminderMedicine(1.5F, 0, 13, Date(), days1, null, null))
        reminders2.add(ReminderMedicine(1F, 0, 11, Date(), days2, null, null))
        UserInformation.addNewMedicine(
            LocalMedicine(
                "Aulin",
                MedicineTypeEnum.PILLS,
                24F,
                24F,
                null,
                "Aulin"
            )
        )
        UserInformation.addNewReminderList("Aulin", reminders2)

        UserInformation.addNewAppointment(
            Appointment("prelieo", 30, 20, Date(), "")
        )
        UserInformation.addNewAppointment(
            Appointment("Visita Urologo", 15, 8, Date(), "")
        )
        UserInformation.addNewAppointment(
            Appointment("Visita Urologo1", 15, 8, Date(), "")
        )
        UserInformation.addNewAppointment(
            Appointment("Visita Urologo2", 15, 8, Date(), "")
        )
        UserInformation.addNewAppointment(
            Appointment("Visita Urologo3", 15, 8, Date(), "")
        )
        UserInformation.addNewAppointment(
            Appointment("Visita Urologo4", 15, 8, Date(), "")
        )

        LocalDatabase.saveMedicineList(UserInformation.medicines)

    }

    override fun onResume() {
        super.onResume()
        initLists()
    }

    //TODO funzione di test per invio mail
    private fun sendMailTest() {
        val completeMail = Mail.composeMail(
            RemoteMedicine("Tacchipirina", "1234", MedicineTypeEnum.PILLS),
            User("1234", "filippo", "ciao", "filippo.ciao@ciao.com"),
            Friend("friend01", "friend02", null, "pippo.coglio@gmail.com", RelationEnum.Doctor)
        )
        var i = Intent(Intent.ACTION_SEND)

        if (completeMail != null) {
            i.type = "message/rfc822"
            i.putExtra(Intent.EXTRA_EMAIL, completeMail.mailto)
            i.putExtra(Intent.EXTRA_SUBJECT, completeMail.mailsubject)
            i.putExtra(Intent.EXTRA_TEXT, completeMail.mailBody)
        }
        startActivity(Intent.createChooser(i, "Invia mail..."))
    }

    private fun initLists(filterDate: Date = Date()) {
        filterDate.time = Utils.dataNormalizationLimit(filterDate)

        /*Appointment list*/
        UserInformation.appointments.sortWith(compareBy<Appointment> { it.date }.thenBy { it.hours }
            .thenBy { it.minutes })
        appointmentListSorted = UserInformation.appointments
        appointmentListSorted = LinkedList(appointmentListSorted.filter { it.date <= filterDate })
        val arrayAdapterAppointments = LinkedList<String>()
        appointmentListSorted.forEach { arrayAdapterAppointments.add(formatOutputString(it)) }
        //appointmentListMain.adapter =           ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayAdapterAppointments)

        /*Reminder List*/
        reminderListSorted = getSortedListReminders(filterDate)
        val arrayAdapterReminders = LinkedList<String>()
        reminderListSorted.forEach { arrayAdapterReminders.add(formatOutputString(it)) }
        reminderListMain.adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayAdapterReminders)

    }

    private fun getSortedListReminders(filterDate: Date): LinkedList<ReminderMedicineSort> {

        var randomList: LinkedList<ReminderMedicineSort> = LinkedList()
        UserInformation.medicines.forEach {
            it.reminders?.forEach { reminder ->
                randomList.add(ReminderMedicineSort(it.name, it.medicineType, reminder))
            }
        }
        randomList = convertSeqToDate(randomList)

        randomList = LinkedList(randomList.filter { it.reminder.startingDay <= filterDate })
        randomList.sortBy { it.reminder.startingDay }

        return randomList
    }

    private fun convertSeqToDate(list: LinkedList<ReminderMedicineSort>): LinkedList<ReminderMedicineSort> {
        val convertedList: LinkedList<ReminderMedicineSort> = LinkedList()

        list.forEach {
            if (it.reminder.startingDay == it.reminder.expireDate)
                convertedList.add(it)
            else
                convertedList.addAll(getDataListFromDays(it))
        }

        return convertedList
    }

    private fun getDataListFromDays(entry: ReminderMedicineSort): Collection<ReminderMedicineSort> {
        val returnedList: LinkedList<ReminderMedicineSort> = LinkedList()

        entry.reminder.days?.forEach {
            val calendar = Calendar.getInstance()
            calendar.time = Date(System.currentTimeMillis())

            while (calendar[Calendar.DAY_OF_WEEK] != it.dayNumber) {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }
            returnedList.add(
                ReminderMedicineSort(
                    entry.medName,
                    entry.medType,
                    ReminderMedicine(
                        entry.reminder.dosage,
                        entry.reminder.minutes,
                        entry.reminder.hours,
                        calendar.time,
                        null,
                        calendar.time,
                        entry.reminder.additionNotes
                    )
                )
            )
        }

        return returnedList
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
                text += "${item.hours}:"
                text += if (item.minutes < 10) "0${item.minutes}" else item.minutes
                return text
            }
            else -> return ""
        }

    }
}