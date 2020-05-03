package com.pillskeeper.activity.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView

import com.pillskeeper.R
import com.pillskeeper.activity.GenericDeleteDialog
import com.pillskeeper.activity.appointment.AppointmentFormActivity
import com.pillskeeper.activity.appointment.AppointmentListActivity.Companion.APPOINTMENT_VALUE
import com.pillskeeper.data.Appointment
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.enums.DialogModeEnum
import com.pillskeeper.utility.Utils
import java.util.*


class ReminderAppointmentListFragment : Fragment() {

    private lateinit var appointmentListSorted: LinkedList<Appointment>
    private lateinit var appointmentListMain: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_reminder_appointment_list, container, false)

        appointmentListMain = view.findViewById(R.id.appointmentListMain)

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
        initList()
    }

    private fun initList() {
        val filterDate = Date()
        filterDate.time = Utils.dataNormalizationLimit(filterDate)
        UserInformation.appointments.sortWith(compareBy { it.date })
        appointmentListSorted = UserInformation.appointments
        appointmentListSorted = LinkedList(appointmentListSorted.filter { it.date <= filterDate })
        val arrayAdapterAppointments = LinkedList<String>()
        appointmentListSorted.forEach { arrayAdapterAppointments.add(formatOutputString(it)) }
        appointmentListMain.adapter = context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_list_item_1,
                arrayAdapterAppointments
            )
        }
    }

    private fun formatOutputString(item: Appointment): String {
        val cal: Calendar = Calendar.getInstance()
        cal.time = item.date
        var text =
            "${item.name} - ${cal.get(Calendar.DAY_OF_MONTH)}/${cal.get(Calendar.MONTH) + 1}  "
        text += "${cal.get(Calendar.HOUR_OF_DAY)}:"
        text += if (cal.get(Calendar.MINUTE) < 10)
            "0${cal.get(Calendar.MINUTE)}"
        else
            cal.get(Calendar.MINUTE)
        return text
    }
}