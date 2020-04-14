package com.pillskeeper.activity.medicine.reminder

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.pillskeeper.R
import com.pillskeeper.activity.medicine.reminder.reminderformfragments.ReminderActivity.Companion.hours
import com.pillskeeper.data.ReminderMedicine
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.enums.DaysEnum
import com.pillskeeper.utility.Utils
import kotlinx.android.synthetic.main.fragment_form_seq_reminder_days.*
import java.util.*
import kotlin.collections.HashMap


class EditReminderActivity : AppCompatActivity() {

    private lateinit var reminder               : ReminderMedicine
    private          var medName                : String? = null
    private lateinit var startingDate           : Date
    private          var expireDate             : Date? = null


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
        medName = intent.getStringExtra(ReminderListActivity.MEDICINE_NAME)

        initializeForm()
        populateCommonFields()
        registerDateButtonAction()


        saveButton.setOnClickListener {
            val days: LinkedList<DaysEnum>? =
                if(!reminder.isSingleDayRem())
                    buildDaysArray()
                else
                    null

            if(checkValue(days)) {

                val newReminder = ReminderMedicine(
                    dosageSpinner.selectedItem.toString().toFloat(),
                    minuteSpinner.selectedItem.toString().toInt(),
                    hourSpinner.selectedItem.toString().toInt(),
                    startingDate,
                    days,
                    expireDate,
                    reminderAddNotes.text.toString()
                )

                if(UserInformation.editReminder(medName!!,reminder,newReminder)){

                    //todo ripianificazione degli allarmi



                    finish()

                } else {
                    Utils.buildAlertDialog(
                        this,
                        "Perfavore inserire valori corretti",
                        getString(R.string.message_title)
                    )
                }

            } else {
                Utils.buildAlertDialog(
                    this,
                    "Perfavore inserire valori corretti",
                    getString(R.string.message_title)
                )
            }
        }


        abortButton.setOnClickListener {
            finish()
        }

    }

    private fun registerDateButtonAction(){
        val calExp = Calendar.getInstance()

        val yearExp = calExp.get(Calendar.YEAR)
        val monthExp = calExp.get(Calendar.MONTH)
        val dayExp = calExp.get(Calendar.DAY_OF_MONTH)

        expireDate = reminder.expireDate

        if(!reminder.isSingleDayRem()) {
            buttonDateEnd.setOnClickListener {
                DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                        buttonDateEnd.text = getString(
                            R.string.dateButtonFormatted,
                            dayOfMonth,
                            monthOfYear + 1,
                            year
                        )

                        calExp.set(Calendar.YEAR, year)
                        calExp.set(Calendar.MONTH, monthOfYear)
                        calExp.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        calExp.set(Calendar.HOUR_OF_DAY, 0)
                        calExp.set(Calendar.MINUTE, 0)
                        calExp.set(Calendar.SECOND, 0)
                        calExp.set(Calendar.MILLISECOND, 0)

                        expireDate = calExp.time

                    },
                    yearExp,
                    monthExp,
                    dayExp
                ).show()
            }
        }

        val calStart = Calendar.getInstance()

        val yearStart = calExp.get(Calendar.YEAR)
        val monthStart = calExp.get(Calendar.MONTH)
        val dayStart = calExp.get(Calendar.DAY_OF_MONTH)

        startingDate = reminder.startingDay

        buttonDateReminder.setOnClickListener {
            DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                buttonDateReminder.text = getString(R.string.dateButtonFormatted,dayOfMonth,monthOfYear+1,year)

                calStart.set(Calendar.YEAR, year)
                calStart.set(Calendar.MONTH, monthOfYear)
                calStart.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                calStart.set(Calendar.HOUR_OF_DAY, 0)
                calStart.set(Calendar.MINUTE, 0)
                calStart.set(Calendar.SECOND, 0)
                calStart.set(Calendar.MILLISECOND, 0)

                startingDate = calStart.time

            }, yearStart, monthStart, dayStart).show()
        }
    }

    private fun checkValue(days: LinkedList<DaysEnum>?): Boolean{

        if(expireDate != null)
            if (!Utils.checkDate(expireDate!!, this))
                return false

        if(dosageSpinner.selectedItem.toString().toFloat() == 0F)
            return false

        if(days != null)
            if (days.size == 0)
                return false

        return true
    }

    private fun initializeForm(){
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
                expireDate = calEnd.time
            }
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

        reminder.days?.forEach {
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

    private fun buildDaysArray(): LinkedList<DaysEnum>{
        val days = LinkedList<DaysEnum>()

        checkBox.forEach {    if(it.value.isChecked)  days.add(DaysEnum.valueOf(it.key))  }

        return days
    }

}

