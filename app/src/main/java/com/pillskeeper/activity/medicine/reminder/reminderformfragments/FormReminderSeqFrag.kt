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
import com.pillskeeper.activity.medicine.reminder.reminderformfragments.ReminderActivity.Companion.hours
import com.pillskeeper.data.ReminderMedicine
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.enums.DaysEnum
import com.pillskeeper.notifier.NotifyPlanner
import com.pillskeeper.utility.Utils
import java.util.*

class FormReminderSeqFrag(
    private val viewPager: ViewPager?,
    private val medName: String?
) : Fragment() {

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
        abortButtonReminder = view.findViewById(R.id.abortButtonReminderSeq)
        saveButtonReminder = view.findViewById(R.id.saveButtonReminderSeq)
        dosageSpinnerReminder = view.findViewById(R.id.dosageSpinnerReminder)


        val calExp = Calendar.getInstance()

        initSpinner()

        val yearExp = calExp.get(Calendar.YEAR)
        val monthExp = calExp.get(Calendar.MONTH)
        val dayExp = calExp.get(Calendar.DAY_OF_MONTH)

        buttonDateEnd.setOnClickListener {
            DatePickerDialog(activity!!, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                buttonDateEnd.text = getString(R.string.dateButtonFormatted,dayOfMonth,monthOfYear+1,year)

                calExp.set(Calendar.YEAR, year)
                calExp.set(Calendar.MONTH, monthOfYear)
                calExp.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                calExp.set(Calendar.HOUR_OF_DAY, 0)
                calExp.set(Calendar.MINUTE, 0)
                calExp.set(Calendar.SECOND, 0)
                calExp.set(Calendar.MILLISECOND, 0)

                expDateSelected = calExp.time

            }, yearExp, monthExp, dayExp).show()
        }

        val calStart = Calendar.getInstance()

        val yearStart = calExp.get(Calendar.YEAR)
        val monthStart = calExp.get(Calendar.MONTH)
        val dayStart = calExp.get(Calendar.DAY_OF_MONTH)

        buttonDateStart.setOnClickListener {
            DatePickerDialog(activity!!, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                buttonDateStart.text = getString(R.string.dateButtonFormatted,dayOfMonth,monthOfYear+1,year)

                calStart.set(Calendar.YEAR, year)
                calStart.set(Calendar.MONTH, monthOfYear)
                calStart.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                calStart.set(Calendar.HOUR_OF_DAY, 0)
                calStart.set(Calendar.MINUTE, 0)
                calStart.set(Calendar.SECOND, 0)
                calStart.set(Calendar.MILLISECOND, 0)

                startDateSelected = calStart.time

            }, yearStart, monthStart, dayStart).show()
        }

        abortButtonReminder.setOnClickListener {
            if(viewPager != null)
                viewPager.currentItem = FormAdapter.FORM_SAVE_OR_REMINDER
            else
                activity?.finish()
        }

        saveButtonReminder.setOnClickListener {

            val days = buildDaysArray()

            if(checkValue(days)) {
                if (startDateSelected == null)
                    startDateSelected = Date()

                val newRem = ReminderMedicine(
                    dosageSpinnerReminder.selectedItem.toString().toFloat(),
                    spinnerMinutesRem2.selectedItem.toString().toInt(),
                    spinnerHoursRem2.selectedItem.toString().toInt(),
                    startDateSelected!!,
                    days,
                    expDateSelected,
                    editTextAddNotesRem.text.toString()
                )

                if(viewPager != null) {
                    FormAdapter.addReminder(newRem)
                    viewPager.currentItem = FormAdapter.FORM_SAVE_OR_REMINDER
                } else {
                    if(UserInformation.addNewReminder(medName!!,newRem)) {

                        Utils.getSingleReminderListNormalized(
                            medName,
                            UserInformation.getSpecificMedicine(medName)!!.medicineType,
                            newRem
                        ).forEach {
                            NotifyPlanner.planSingleAlarm(
                                activity?.applicationContext!!,
                                activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager,
                                it
                            )
                        }
                        activity?.finish()
                    } else {
                        Utils.buildAlertDialog(
                            activity!!,
                            "Inserimento fallito!",
                            getString(R.string.message_title)
                        )
                    }
                }
            } else {
                Utils.buildAlertDialog(
                    activity!!,
                    "perfavore inserire valori corretti",
                    getString(R.string.message_title)
                )
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

        val arrayAdapterHours = ArrayAdapter(activity!!,android.R.layout.simple_spinner_item, hours)
        arrayAdapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerHoursRem2.adapter = arrayAdapterHours

        val minutesArray = ArrayList<String>()
        for (i in 0..12)
            minutesArray.add(if(i < 2) "0${i*5}" else "${i*5}")

        val arrayAdapterMinutes = ArrayAdapter(activity!!,android.R.layout.simple_spinner_item, minutesArray)
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
            if (!Utils.checkDate(expDateSelected!!, activity!!))
               return false

        if(dosageSpinnerReminder.selectedItem.toString().toFloat() == 0F)
            return false


        if (days.size == 0)
            return false

        return true
    }


}