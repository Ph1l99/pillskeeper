package com.pillskeeper.activity.homefragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView

import com.pillskeeper.R
import com.pillskeeper.data.Appointment
import com.pillskeeper.data.ReminderMedicineSort
import com.pillskeeper.utility.Utils
import java.util.*


class ReminderMedicineListFragment : Fragment() {

    private lateinit var reminderListSorted: LinkedList<ReminderMedicineSort>

    lateinit var reminderListMain: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_reminder_medicine_list, container, false)

        reminderListMain = view.findViewById(R.id.reminderListMain)

        initList()

        return view
    }

    private fun initList() {
        var filterDate = Date()
        filterDate.time = Utils.dataNormalizationLimit(filterDate)
        reminderListSorted = Utils.getSortedListReminders(filterDate)
        val arrayAdapterReminders = LinkedList<String>()
        reminderListSorted.forEach { arrayAdapterReminders.add(formatOutputString(it)) }
        reminderListMain.adapter = context?.let { ArrayAdapter(it, android.R.layout.simple_list_item_1, arrayAdapterReminders)}
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
}
