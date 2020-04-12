package com.pillskeeper.activity.medicine.reminder

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.pillskeeper.R
import com.pillskeeper.activity.medicine.ReminderListActivity
import com.pillskeeper.activity.medicine.reminder.reminderformfragments.ReminderActivity.Companion.hours
import com.pillskeeper.data.ReminderMedicine
import com.pillskeeper.enums.DaysEnum
import kotlinx.android.synthetic.main.fragment_form_seq_reminder_days.*
import java.util.*
import kotlin.collections.HashMap


class EditReminderActivity : AppCompatActivity() {

    private lateinit var reminder               : ReminderMedicine

    private lateinit var saveButton             : Button
    private lateinit var abortButton            : Button
    private lateinit var buttonDateReminder     : Button
    private lateinit var hourSpinner            : Spinner
    private lateinit var minuteSpinner          : Spinner
    private lateinit var reminderAddNotes       : EditText
    private lateinit var dosageSpinner          : Spinner
    private lateinit var checkBox               : HashMap<String,CheckBox>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        reminder = intent.getSerializableExtra(ReminderListActivity.REMINDER_MEDICINE) as ReminderMedicine

        if(reminder.isSingleDayRem()) {
            setContentView(R.layout.fragment_day_reminder)
            buttonDateReminder = findViewById(R.id.buttonDateReminder)
            saveButton = findViewById(R.id.saveButtonReminder)
            abortButton = findViewById(R.id.abortButtonReminder)
            hourSpinner = findViewById(R.id.hourReminderSpinner)
            minuteSpinner = findViewById(R.id.minutesReminderSpinner)
            reminderAddNotes = findViewById(R.id.reminderAddNotesEditT)
            dosageSpinner = findViewById(R.id.dosageQtyReminder)
        } else {
            setContentView(R.layout.fragment_form_seq_reminder_days)
            buttonDateReminder = findViewById(R.id.buttonDateStart)
            saveButton = findViewById(R.id.saveButtonReminderSeq)
            abortButton = findViewById(R.id.abortButtonReminderSeq)
            hourSpinner = findViewById(R.id.spinnerHoursRem2)
            minuteSpinner = findViewById(R.id.spinnerMinutesRem2)
            reminderAddNotes = findViewById(R.id.editTextAddNotesRem)
            dosageSpinner = findViewById(R.id.dosageSpinnerReminder)
            checkBox = buildCheckboxes()
            if(reminder.expireDate != null) {
                val calEnd = Calendar.getInstance()
                calEnd.time = reminder.expireDate!!
                buttonDateEnd.text = getString(R.string.dateButtonFormatted, calEnd.get(Calendar.DAY_OF_MONTH),calEnd.get(Calendar.MONTH) + 1,calEnd.get(Calendar.YEAR))
            }
        }

        populateCommonFields()

        saveButton.setOnClickListener {

            //edit, salvataggio e ripianificazione degli allarmi
        }


        abortButton.setOnClickListener {
            finish()
        }

    }

    private fun buildCheckboxes(): HashMap<String, CheckBox> {
        val checkBoxes: HashMap<String,CheckBox> = HashMap()
        DaysEnum.values().forEach {
            checkBoxes[it.name] = findViewById(
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
        return checkBoxes
    }

    private fun populateCommonFields(){
        val calStart = Calendar.getInstance()
        calStart.time = reminder.startingDay
        buttonDateReminder.text = getString(R.string.dateButtonFormatted, calStart.get(Calendar.DAY_OF_MONTH),calStart.get(Calendar.MONTH) + 1,calStart.get(Calendar.YEAR))
        reminderAddNotes.setText(reminder.additionNotes)
        initSpinner()
    }

    private fun initSpinner(){

        val arrayAdapterHours = ArrayAdapter(this,android.R.layout.simple_spinner_item, hours)
        arrayAdapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        hourSpinner.adapter = arrayAdapterHours
        hourSpinner.setSelection(reminder.hours)

        val minutesArray = ArrayList<String>()
        for (i in 0..12)
            minutesArray.add(if(i < 2) "0${i*5}" else "${i*5}")

        val arrayAdapterMinutes = ArrayAdapter(this,android.R.layout.simple_spinner_item, minutesArray)
        arrayAdapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        minuteSpinner.adapter = arrayAdapterMinutes
        minuteSpinner.setSelection(reminder.minutes/5)


        val qtyArray = ArrayList<String>()
        for (i in 0..10)
            qtyArray.add("${i/2F}")

        val arrayAdapterDosage = ArrayAdapter(this,android.R.layout.simple_spinner_item, qtyArray)
        arrayAdapterDosage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dosageSpinner.adapter = arrayAdapterDosage
        dosageSpinner.setSelection((reminder.dosage * 2F).toInt())

    }
}

