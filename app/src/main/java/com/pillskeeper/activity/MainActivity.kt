package com.pillskeeper.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.pillskeeper.R
import com.pillskeeper.activity.pills.PillsListActivity
import com.pillskeeper.data.Appointment
import com.pillskeeper.data.LocalMedicine
import com.pillskeeper.data.ReminderMedicine
import com.pillskeeper.data.RemoteMedicineSort
import com.pillskeeper.datamanager.DatabaseManager
import com.pillskeeper.datamanager.LocalDatabase
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.enums.DaysEnum
import com.pillskeeper.enums.MedicineTypeEnum
import com.pillskeeper.utility.Utils
import kotlinx.android.synthetic.main.activity_main.*
import java.time.DayOfWeek
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap

class MainActivity : AppCompatActivity() {

    companion object {
        const val START_FIRST_LOGIN_ACTIVITY_CODE = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        LocalDatabase.sharedPref = this.getPreferences(Context.MODE_PRIVATE)

        UserInformation //necessario per inizializzare i componenti interni
        UserInformation.context = this
        FirebaseApp.initializeApp(this)
        DatabaseManager.obtainRemoteDatabase()

        Utils.stdLayout = EditText(this).background

        //TODO DEBUG - to be removed
        funTest()

        readFirstLogin()


    }

    //TODO risolvere questa parte @Phil
    private fun readFirstLogin() {
        //val userN = LocalDatabase.readUsername()
        if (/*FirebaseAuth.getInstance().currentUser == null*/ false) {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
            finish()
        } else {
            initLists()
        }
    }

    private fun funTest() {

        val days1 :LinkedList<DaysEnum> = LinkedList()
        days1.add(DaysEnum.MON)
        days1.add(DaysEnum.THU)
        days1.add(DaysEnum.SUN)

        val days2 :LinkedList<DaysEnum> = LinkedList()
        days1.add(DaysEnum.MON)

        val reminders = LinkedList<ReminderMedicine>()
        reminders.add(ReminderMedicine(1.5F, 0, 19, Date(), days1, Date(), null))
        reminders.add(ReminderMedicine(1F, 0, 19, Date(), days2, Date(), null))
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


        val reminders2 = LinkedList<ReminderMedicine>()
        reminders2.add(ReminderMedicine(1.5F, 0, 19, Date() ,days1, Date(), null))
        reminders2.add(ReminderMedicine(1F, 0, 19, Date(), days2, Date(), null))
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


        LocalDatabase.saveMedicineList(UserInformation.medicines)

    }

    private fun initLists(){

        /*Appointment list*/
        UserInformation.appointments.sortWith(compareBy<Appointment> {it.date}.thenBy { it.hours }.thenBy { it.minutes })
        val arrayAdapterAppointments = LinkedList<String>()
        UserInformation.appointments.forEach { arrayAdapterAppointments.add(it.name) }
        appointmentList.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayAdapterAppointments)

        /*Reminder List*/
        /*val reminderListSorted: LinkedList<RemoteMedicineSort> = getSortedHashMapReminders()
        val arrayAdapterReminders = LinkedList<String>()
        reminderListSorted.forEach { arrayAdapterAppointments.add("${it.medName}  -  ${it.reminder.dosage}") }
        appointmentList.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayAdapterReminders)
        */
    }

    private fun getSortedHashMapReminders(filterDate: Date = Date(System.currentTimeMillis())): LinkedList<RemoteMedicineSort>{
        val list: HashMap<String,ReminderMedicine> = HashMap()
        UserInformation.medicines.forEach { it.reminders?.forEach { rem -> list[it.name] = rem } }

        //riduzione lista basandosi su startDate ed DAY OF WEEK!!!!!!!!
        //RENDENDOLO DINAMICO IN BASE AD UN GIORNO PASSATO COME PARAMENTRO

        val sortedList: LinkedList<RemoteMedicineSort> = LinkedList()

        for(entry in list){

            if(sortedList.isEmpty()) //se lista vuota
                sortedList[0] = RemoteMedicineSort(entry.key, entry.value)
            else {
                for (pos in 0..sortedList.size) {

                    //controllo che tipo di check devo fare!
                    //1. giorno specifico    <---->    giorno specifico (semplice compareTo con anche ora e minuti)   OK
                    //2. giorno specifico    <---->    sequence
                    //3. sequence            <---->    data
                    //4. sequence            <---->    sequence

                    if (entry.value.startingDay == entry.value.expireDate && sortedList[pos].reminder.startingDay == sortedList[pos].reminder.expireDate) {/*Date() to Date()*/

                        if(entry.value.startingDay.before(sortedList[pos].reminder.startingDay)) //insert Before[samePosition]

                            sortedList.add(pos, RemoteMedicineSort(entry.key,entry.value))

                        else //insert After + check if is last position

                           if(sortedList.lastIndex == pos)

                               sortedList.addLast(RemoteMedicineSort(entry.key,entry.value))

                    } else if (entry.value.startingDay == entry.value.expireDate && sortedList[pos].reminder.startingDay != sortedList[pos].reminder.expireDate) { /*data to sequence*/
                        var questoMomento = Date(System.currentTimeMillis())
                        //questoMomento >>>>>>>>>  prossimoReminder(ora,minuto lun)
                        /*lun-mar-sab, ora e min*/



                    } /*else if (/*data to sequence*/) {

                    } else { /*SEQ to SEQ*/

                    }*/
                }
            }



        }



        return sortedList
    }

    private fun getDateFromSequence(): Date{

        return Date()
    }


}
/*if (entry.value.startingDay == sortedList[elem].reminder.startingDay) {
//1. sono completamente uguali...
//2. A > B (controllo ora e minuti)
//3. A < b (controllo ora e minuti)
if(entry.value.hours == sortedList[elem].reminder.hours){
    if(entry.value.minutes == sortedList[elem].reminder.minutes) {
        //è uguale dove lo piazzo (se prima o dopo)
    } else if (entry.value.minutes < sortedList[elem].reminder.minutes){
        //entry va prima dell'elemento attuale nella sortedList
    } else {
        //1. - entry va dopo dell'elemento attuale nella sortedList
        //2. - controllo se sono nell'ultima posizione, se si inserisco subito after altrimenti non faccio nulla e proseguo con il ciclo
    }
} else if(entry.value.hours < sortedList[elem].reminder.hours){
    //entry va prima in sortedlist dell'elemento attuale
} else {
    //entry va dopo in sortedlist dell'elemento attuale
}





} else if (entry.value.startingDay < sortedList[elem].reminder.startingDay){
//metti prima
} else {
if(){}
}*/