package com.pillskeeper.activity.medicine.reminder.reminderformfragments

import android.app.AlarmManager
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import com.pillskeeper.R
import com.pillskeeper.activity.medicine.medicineformfragments.FormAdapter
import com.pillskeeper.data.ReminderMedicine
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.notifier.NotifyPlanner
import com.pillskeeper.utility.Utils
import kotlinx.android.synthetic.main.fragment_day_reminder_time.*
import kotlinx.android.synthetic.main.fragment_form_reminder_one_day_quantity.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class FormReminderOneDayQuantityFrag : Fragment() {

    private lateinit var saveTextReminder: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_form_reminder_one_day_quantity, container, false)

        /*
        saveTextReminder.setOnClickListener {
            if(dateSelected != null && dosageQtyReminder.selectedItem.toString().toFloat() > 0){
                cal.time = dateSelected!!
                cal.set(Calendar.HOUR_OF_DAY, hourReminderSpinner.selectedItem.toString().toInt())
                cal.set(Calendar.MINUTE, minutesReminderSpinner.selectedItem.toString().toInt())
                if (cal.time > Date()){
                    val reminder = ReminderMedicine(
                        dosageQtyReminder.selectedItem.toString().toFloat(),
                        minutesReminderSpinner.selectedItem.toString().toInt(),
                        hourReminderSpinner.selectedItem.toString().toInt(),
                        cal.time,
                        null,
                        cal.time,
                        reminderAddNotesEditT.text.toString()
                    )
                    if(viewPager != null) {
                        FormAdapter.addReminder(reminder)
                        viewPager.currentItem = FormAdapter.FORM_SAVE_OR_REMINDER
                    } else {
                        if (UserInformation.addNewReminder(medName!!,reminder)) {
                            Utils.getSingleReminderListNormalized(
                                medName,
                                UserInformation.getSpecificMedicine(medName)!!.medicineType,
                                reminder
                            ).forEach {
                                NotifyPlanner.planSingleAlarm(
                                    activity!!,
                                    activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager,
                                    it
                                )
                            }
                            activity?.finish()
                        }
                    }
                } else {
                    Toast.makeText(UserInformation.context,"Perfavore inserire informazioni corrette!",Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(UserInformation.context,"Perfavore inserire informazioni corrette!",
                    Toast.LENGTH_LONG).show()
            }
        }

             */

        return view
    }

}
