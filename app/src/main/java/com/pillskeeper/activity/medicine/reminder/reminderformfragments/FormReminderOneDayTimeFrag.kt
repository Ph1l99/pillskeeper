package com.pillskeeper.activity.medicine.reminder.reminderformfragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.pillskeeper.R
import com.pillskeeper.activity.medicine.medicineformfragments.FormAdapter
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.utility.InitSpinner
import java.util.*

class FormReminderOneDayTimeFrag(private val viewPager: ViewPager?) : Fragment() {

    private var dateSelected: Date? = null

    private lateinit var buttonDateReminder: Button
    private lateinit var hourReminderSpinner: Spinner
    private lateinit var minutesReminderSpinner: Spinner
    private lateinit var textViewNext: TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_day_reminder_time, container, false)

        buttonDateReminder = view.findViewById(R.id.buttonDateReminder)
        hourReminderSpinner = view.findViewById(R.id.hourReminderSpinner)
        minutesReminderSpinner = view.findViewById(R.id.minutesReminderSpinner)
        textViewNext = view.findViewById(R.id.textViewNext)

        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        initSpinner()

        if (FormAdapter.isAReminderEditing) {
            val calStart = Calendar.getInstance()
            calStart.time = FormAdapter.startDay!!
            buttonDateReminder.text = getString(
                R.string.dateButtonFormatted,
                calStart.get(Calendar.DAY_OF_MONTH),
                calStart.get(Calendar.MONTH) + 1,
                calStart.get(Calendar.YEAR)
            )
            FormAdapter.startDay = calStart.time
            dateSelected = FormAdapter.startDay

            hourReminderSpinner.setSelection(FormAdapter.reminderHour)
            minutesReminderSpinner.setSelection((FormAdapter.reminderMinute / InitSpinner.MINUTE_MULTI).toInt())
        }

        buttonDateReminder.setOnClickListener {
            DatePickerDialog(
                requireActivity(),
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    buttonDateReminder.text =
                        getString(R.string.dateButtonFormatted, dayOfMonth, monthOfYear + 1, year)

                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    cal.set(Calendar.HOUR_OF_DAY, 0)
                    cal.set(Calendar.MINUTE, 0)
                    cal.set(Calendar.SECOND, 0)
                    cal.set(Calendar.MILLISECOND, 0)

                    dateSelected = cal.time

                },
                year,
                month,
                day
            ).show()
        }

        textViewNext.setOnClickListener {
            if (dateSelected != null) {

                cal.time = dateSelected!!
                cal.set(Calendar.HOUR_OF_DAY, hourReminderSpinner.selectedItem.toString().toInt())
                cal.set(Calendar.MINUTE, minutesReminderSpinner.selectedItem.toString().toInt())

                if (cal.time >= Date()) {
                    FormAdapter.startDay = cal.time
                    FormAdapter.finishDay = cal.time

                    FormAdapter.reminderHour = hourReminderSpinner.selectedItem.toString().toInt()
                    FormAdapter.reminderMinute =
                        minutesReminderSpinner.selectedItem.toString().toInt()

                    if (viewPager != null) {
                        viewPager.currentItem = FormAdapter.FORM_ONE_DAY_REMINDER_QUANTITY
                    }
                } else {
                    Toast.makeText(
                        UserInformation.context,
                        UserInformation.context.getString(R.string.genericInfoError),
                        Toast.LENGTH_LONG
                    ).show()
                }

            } else {
                Toast.makeText(
                    UserInformation.context,
                    UserInformation.context.getString(R.string.genericInfoError),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        return view
    }

    private fun initSpinner() {
        hourReminderSpinner.adapter = InitSpinner.initSpinnerHour(requireActivity())
        minutesReminderSpinner.adapter = InitSpinner.initSpinnerMinute(requireActivity())
    }
}