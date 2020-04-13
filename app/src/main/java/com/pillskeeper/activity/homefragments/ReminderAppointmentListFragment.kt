package com.pillskeeper.activity.homefragments

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.core.content.ContextCompat

import com.pillskeeper.R
import com.pillskeeper.activity.GenericDeleteDialog
import com.pillskeeper.activity.appointment.AppointmentFormActivity
import com.pillskeeper.activity.appointment.AppointmentListActivity.Companion.APPOINTMENT_VALUE
import com.pillskeeper.data.Appointment
import com.pillskeeper.data.LocalMedicine
import com.pillskeeper.data.ReminderMedicine
import com.pillskeeper.data.ReminderMedicineSort
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.enums.DaysEnum
import com.pillskeeper.enums.DialogModeEnum
import com.pillskeeper.enums.MedicineTypeEnum
import com.pillskeeper.utility.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.view.*
import kotlinx.android.synthetic.main.fragment_reminder_appointment_list.*
import java.util.*


class ReminderAppointmentListFragment : Fragment() {

    private lateinit var appointmentListSorted: LinkedList<Appointment>
    private lateinit var appointmentListMain: ListView



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_reminder_appointment_list, container, false)

        appointmentListMain = view.findViewById(R.id.appointmentListMain)

        //TODO da rimuovere
        funTest()

        initList()

        appointmentListMain.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(context, AppointmentFormActivity::class.java)
                .putExtra(APPOINTMENT_VALUE, appointmentListSorted[position])
            startActivity(intent)
        }

        appointmentListMain.setOnItemLongClickListener { _, _, position, _ ->
            context?.let {
                GenericDeleteDialog(
                    it,
                    appointmentListSorted[position].name,
                    DialogModeEnum.DELETE_APPOINTMENT
                ).show()
            }
            return@setOnItemLongClickListener true
        }

        return view
    }

    override fun onResume() {
        super.onResume()
    }

    private fun initList() {
        var filterDate = Date()
        filterDate.time = Utils.dataNormalizationLimit(filterDate)
        UserInformation.appointments.sortWith(compareBy { it.date })
        appointmentListSorted = UserInformation.appointments
        appointmentListSorted = LinkedList(appointmentListSorted.filter { it.date <= filterDate })
        val arrayAdapterAppointments = LinkedList<String>()
        appointmentListSorted.forEach { arrayAdapterAppointments.add(formatOutputString(it)) }
        appointmentListMain.adapter = context?.let { ArrayAdapter(it, android.R.layout.simple_list_item_1, arrayAdapterAppointments)}
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

    private fun funTest() {

        val days1: LinkedList<DaysEnum> = LinkedList()
        days1.add(DaysEnum.MON)
        days1.add(DaysEnum.FRI)
        days1.add(DaysEnum.SAT)
        days1.add(DaysEnum.TUE)
        days1.add(DaysEnum.THU)
        days1.add(DaysEnum.SUN)
        days1.add(DaysEnum.WED)


        val reminders = LinkedList<ReminderMedicine>()
        reminders.add(ReminderMedicine(2F, 39, 19, Date(), days1, null, null))
        reminders.add(ReminderMedicine(1F, 41, 19, Date(), days1, null, null))
        UserInformation.addNewMedicine(
            LocalMedicine(
                "Tachipirina",
                MedicineTypeEnum.PILLS,
                24F,
                7F,
                reminders,
                "Tachipirina"
            )
        )

        /*val reminders2 = LinkedList<ReminderMedicine>()
        reminders2.add(ReminderMedicine(1.5F, 0, 13, Date(), days1, null, null))
        reminders2.add(ReminderMedicine(1F, 0, 11, Date(), days1, null, null))
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
            Appointment("prelieo", Date(), "")
        )
        UserInformation.addNewAppointment(
            Appointment("Visita Urologo", Date(), "")
        )
        UserInformation.addNewAppointment(
            Appointment("Visita Urologo1", Date(), "")
        )
        UserInformation.addNewAppointment(
            Appointment("Visita Urologo2", Date(), "")
        )
        UserInformation.addNewAppointment(
            Appointment("Visita Urologo3", Date(), "")
        )
        UserInformation.addNewAppointment(
            Appointment("Visita Urologo4", Date(), "")
        )*/

    }
}