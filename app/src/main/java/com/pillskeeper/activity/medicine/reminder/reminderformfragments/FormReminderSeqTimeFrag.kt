package com.pillskeeper.activity.medicine.reminder.reminderformfragments

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.pillskeeper.R
import com.pillskeeper.activity.medicine.medicineformfragments.FormAdapter
import com.pillskeeper.data.ReminderMedicine
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.enums.DaysEnum
import com.pillskeeper.notifier.NotifyPlanner
import com.pillskeeper.utility.Utils
import com.pillskeeper.utility.Utils.hours
import java.util.*

class FormReminderSeqTimeFrag(private val viewPager: ViewPager, private val medName: String?) : Fragment() {

    private lateinit var checkBoxes             : HashMap<String,CheckBox>
    private lateinit var spinnerHoursRem2       : Spinner
    private lateinit var spinnerMinutesRem2     : Spinner
    private lateinit var nextTextView           : TextView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_form_seq_reminder_time, container, false)

        spinnerHoursRem2 = view.findViewById(R.id.hourReminderSpinner)
        spinnerMinutesRem2 = view.findViewById(R.id.minutesReminderSpinner)
        nextTextView = view.findViewById(R.id.textViewNext)


        checkBoxes = HashMap()

        initSpinner()

        if(FormAdapter.isAReminderEditing){
            spinnerHoursRem2.setSelection(FormAdapter.reminderHour)
            spinnerMinutesRem2.setSelection(FormAdapter.reminderMinute/5)
            checkBoxes = buildCheckboxes(view)
        } else {
            DaysEnum.values().forEach {
                checkBoxes[it.name] = view.findViewById(
                    when(it){
                        DaysEnum.MON -> R.id.checkBoxMonday
                        DaysEnum.TUE -> R.id.checkBoxTuesday
                        DaysEnum.WED -> R.id.checkBoxWednesday
                        DaysEnum.THU -> R.id.checkBoxThursday
                        DaysEnum.FRI -> R.id.checkBoxFriday
                        DaysEnum.SAT -> R.id.checkBoxSaturday
                        DaysEnum.SUN -> R.id.checkBoxSunday
                    }
                )
            }
        }

        nextTextView.setOnClickListener {
            if(spinnerHoursRem2.selectedItem != null && spinnerMinutesRem2.selectedItem != null) {
                FormAdapter.reminderHour = spinnerHoursRem2.selectedItem.toString().toInt()
                FormAdapter.reminderMinute = spinnerMinutesRem2.selectedItem.toString().toInt()

                val days = buildDaysArray()
                if(days.size > 0){
                    FormAdapter.days = days
                    viewPager.currentItem = FormAdapter.FORM_SEQ_QUANTITY_REMINDER
                } else {
                    Toast.makeText(context, "Selezionare uno o più giorni!", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(context, "Inserire un orario corretto!", Toast.LENGTH_LONG).show()
            }
        }

        return view
    }

    private fun buildDaysArray(): LinkedList<DaysEnum>{
        val days: LinkedList<DaysEnum> = LinkedList()

        checkBoxes.forEach {    if(it.value.isChecked)  days.add(DaysEnum.valueOf(it.key))  }

        return days
    }

    private fun initSpinner(){

        val arrayAdapterHours = ArrayAdapter(requireActivity(),android.R.layout.simple_spinner_item, hours)
        arrayAdapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerHoursRem2.adapter = arrayAdapterHours

        val minutesArray = ArrayList<String>()
        for (i in 0..12)
            minutesArray.add(if(i < 2) "0${i*5}" else "${i*5}")

        val arrayAdapterMinutes = ArrayAdapter(requireActivity(),android.R.layout.simple_spinner_item, minutesArray)
        arrayAdapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerMinutesRem2.adapter = arrayAdapterMinutes


        val qtyArray = ArrayList<String>()
        for (i in 0..10)
            qtyArray.add("${i/2F}")
    }

    private fun buildCheckboxes(view: View): HashMap<String, CheckBox> {
        val checkBoxes: HashMap<String,CheckBox> = HashMap()
        DaysEnum.values().forEach {
            checkBoxes[it.name] = view.findViewById(
                when(it){
                    DaysEnum.MON -> R.id.checkBoxMonday
                    DaysEnum.TUE -> R.id.checkBoxTuesday
                    DaysEnum.WED -> R.id.checkBoxWednesday
                    DaysEnum.THU -> R.id.checkBoxThursday
                    DaysEnum.FRI -> R.id.checkBoxFriday
                    DaysEnum.SAT -> R.id.checkBoxSaturday
                    DaysEnum.SUN -> R.id.checkBoxSunday
                }
            )
        }

        FormAdapter.days?.forEach {
            when(it){
                DaysEnum.MON -> checkBoxes[DaysEnum.MON.name]?.isChecked = true
                DaysEnum.TUE -> checkBoxes[DaysEnum.TUE.name]?.isChecked = true
                DaysEnum.WED -> checkBoxes[DaysEnum.WED.name]?.isChecked = true
                DaysEnum.THU -> checkBoxes[DaysEnum.THU.name]?.isChecked = true
                DaysEnum.FRI -> checkBoxes[DaysEnum.FRI.name]?.isChecked = true
                DaysEnum.SAT -> checkBoxes[DaysEnum.SAT.name]?.isChecked = true
                DaysEnum.SUN -> checkBoxes[DaysEnum.SUN.name]?.isChecked = true
            }
        }


        return checkBoxes
    }


    /*
    private fun checkValue(days: LinkedList<DaysEnum>): Boolean{

        if(expDateSelected != null)
            if (!Utils.checkDate(expDateSelected!!, requireActivity()))
               return false

        if(dosageSpinnerReminder.selectedItem.toString().toFloat() == 0F)
            return false


        if (days.size == 0)
            return false

        return true
    }
     */
}