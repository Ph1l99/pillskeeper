package com.pillskeeper.activity.medicine.reminder.reminderformfragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.pillskeeper.R
import com.pillskeeper.activity.medicine.medicineformfragments.FormAdapter
import com.pillskeeper.activity.medicine.reminder.reminderformfragments.ReminderActivity.Companion.hours
import com.pillskeeper.data.ReminderMedicine
import com.pillskeeper.enums.DaysEnum
import com.pillskeeper.utility.Utils
import java.util.*

class FormReminderSeqFrag(private val viewPager: ViewPager) : Fragment() {

    private lateinit var checkBoxes             : HashMap<String,CheckBox>
    private lateinit var buttonDateStart        : Button
    private lateinit var buttonDateEnd          : Button
    private lateinit var spinnerHoursRem2       : Spinner
    private lateinit var spinnerMinutesRem2     : Spinner
    private lateinit var editTextAddNotesRem    : EditText
    private lateinit var abortButtonReminder    : Button
    private lateinit var saveButtonReminder     : Button
    private lateinit var dosageSpinnerReminder  : Spinner


    private var expDateSelected: Date? = null
    private var startDateSelected: Date? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_form_seq_reminder_days, container, false)

        checkBoxes = HashMap()

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

        buttonDateStart = view.findViewById(R.id.buttonDateStart)
        buttonDateEnd = view.findViewById(R.id.buttonDateEnd)
        spinnerHoursRem2 = view.findViewById(R.id.spinnerHoursRem2)
        spinnerMinutesRem2 = view.findViewById(R.id.spinnerMinutesRem2)
        editTextAddNotesRem = view.findViewById(R.id.editTextAddNotesRem)
        abortButtonReminder = view.findViewById(R.id.abortButtonReminder)
        saveButtonReminder = view.findViewById(R.id.saveButtonReminder)
        dosageSpinnerReminder = view.findViewById(R.id.dosageSpinnerReminder)


        val calExp = Calendar.getInstance()

        initSpinner()

        val yearExp = calExp.get(Calendar.YEAR)
        val monthExp = calExp.get(Calendar.MONTH)
        val dayExp = calExp.get(Calendar.DAY_OF_MONTH)

        buttonDateEnd.setOnClickListener {
            DatePickerDialog(FormAdapter.formActivity!!, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                buttonDateEnd.text = getString(R.string.dateButtonFormatted,dayOfMonth,monthOfYear+1,year)

                calExp.set(Calendar.YEAR, year)
                calExp.set(Calendar.MONTH, monthOfYear)
                calExp.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                calExp.set(Calendar.HOUR_OF_DAY, 0)
                calExp.set(Calendar.MINUTE, 0)
                calExp.set(Calendar.SECOND, 0)
                calExp.set(Calendar.MILLISECOND, 0)

                startDateSelected = calExp.time

            }, yearExp, monthExp, dayExp).show()
        }

        val calStart = Calendar.getInstance()

        val yearStart = calExp.get(Calendar.YEAR)
        val monthStart = calExp.get(Calendar.MONTH)
        val dayStart = calExp.get(Calendar.DAY_OF_MONTH)

        buttonDateStart.setOnClickListener {
            DatePickerDialog(FormAdapter.formActivity!!, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                buttonDateStart.text = getString(R.string.dateButtonFormatted,dayOfMonth,monthOfYear+1,year)

                calStart.set(Calendar.YEAR, year)
                calStart.set(Calendar.MONTH, monthOfYear)
                calStart.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                calStart.set(Calendar.HOUR_OF_DAY, 0)
                calStart.set(Calendar.MINUTE, 0)
                calStart.set(Calendar.SECOND, 0)
                calStart.set(Calendar.MILLISECOND, 0)

                expDateSelected = calStart.time

            }, yearStart, monthStart, dayStart).show()
        }

        abortButtonReminder.setOnClickListener {
            viewPager.currentItem = FormAdapter.FORM_SAVE_OR_REMINDER
        }

        saveButtonReminder.setOnClickListener {

            val days = buildDaysArray()

            if(checkValue(days)) {
                if (startDateSelected == null)
                    startDateSelected = Date()
                FormAdapter.addReminder(
                    ReminderMedicine(
                        dosageSpinnerReminder.selectedItem.toString().toFloat(),
                        spinnerMinutesRem2.selectedItem.toString().toInt(),
                        spinnerHoursRem2.selectedItem.toString().toInt(),
                        startDateSelected!!,
                        days,
                        expDateSelected,
                        editTextAddNotesRem.text.toString()
                    )
                )
                viewPager.currentItem = FormAdapter.FORM_SAVE_OR_REMINDER
            } else {
                Toast.makeText(FormAdapter.formActivity,"perfavore inserire valori corretti",Toast.LENGTH_LONG).show()
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

        val arrayAdapterHours = ArrayAdapter(FormAdapter.formActivity!!,android.R.layout.simple_spinner_item, hours)
        arrayAdapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerHoursRem2.adapter = arrayAdapterHours

        val minutesArray = ArrayList<String>()
        for (i in 0..12)
            minutesArray.add(if(i < 2) "0${i*5}" else "${i*5}")

        val arrayAdapterMinutes = ArrayAdapter(FormAdapter.formActivity!!,android.R.layout.simple_spinner_item, minutesArray)
        arrayAdapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerMinutesRem2.adapter = arrayAdapterMinutes


        val qtyArray = ArrayList<String>()
        for (i in 0..10)
            qtyArray.add("${i/2F}")

        val arrayAdapterDosage = ArrayAdapter(dosageSpinnerReminder.context,android.R.layout.simple_spinner_item, qtyArray)
        arrayAdapterDosage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dosageSpinnerReminder.adapter = arrayAdapterDosage

    }

    private fun checkValue(days: LinkedList<DaysEnum>): Boolean{

        if(expDateSelected != null)
            if (!Utils.checkDate(expDateSelected!!, FormAdapter.formActivity!!))
               return false

        if(dosageSpinnerReminder.selectedItem.toString().toFloat() == 0F)
            return false


        if (days.size == 0)
            return false

        return true
    }


}