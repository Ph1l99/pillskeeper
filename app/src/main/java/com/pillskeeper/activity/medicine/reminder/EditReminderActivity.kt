package com.pillskeeper.activity.medicine.reminder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pillskeeper.R
import com.pillskeeper.activity.medicine.medicineformfragments.FormAdapter
import com.pillskeeper.activity.medicine.medicineformfragments.NoSlideViewPager
import com.pillskeeper.data.ReminderMedicine
import kotlinx.android.synthetic.main.activity_edit_reminder.*


class EditReminderActivity : AppCompatActivity() {

    val VIEW_PAGER_REMINDER_ID = 4040

    private lateinit var reminder               : ReminderMedicine
    private          var medName                : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit_reminder)

        reminder = intent.getSerializableExtra(ReminderListActivity.REMINDER_MEDICINE) as ReminderMedicine
        medName = intent.getStringExtra(ReminderListActivity.MEDICINE_NAME)

        val viewPager = NoSlideViewPager(this)//TODO check viewPager
        viewPager.id = VIEW_PAGER_REMINDER_ID
        relativeLayout.addView(viewPager)

        val adapter = FormAdapter(supportFragmentManager, intent, viewPager)
        FormAdapter.isANewMedicine = false
        FormAdapter.resetReminder()
        FormAdapter.pillName = medName
        FormAdapter.startDay = reminder.startingDay
        FormAdapter.finishDay = reminder.expireDate
        FormAdapter.reminderHour = reminder.hours
        FormAdapter.reminderMinute = reminder.minutes
        FormAdapter.reminderQuantity = reminder.dosage
        FormAdapter.reminderNotes = reminder.additionNotes
        FormAdapter.isAReminderEditing = true
        viewPager.adapter = adapter
        viewPager.currentItem = FormAdapter.FORM_ONE_DAY_REMINDER_TIME

        /*
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

                if(reminder.isSingleDayRem()){
                    val cal = Calendar.getInstance()
                    cal.time= startingDate
                    cal.set(Calendar.HOUR_OF_DAY, hourSpinner.selectedItem.toString().toInt())
                    cal.set(Calendar.MINUTE, minuteSpinner.selectedItem.toString().toInt())
                    expireDate = cal.time
                    startingDate = cal.time
                }

                val newReminder = ReminderMedicine(
                    dosageSpinner.selectedItem.toString().toFloat(),
                    minuteSpinner.selectedItem.toString().toInt(),
                    hourSpinner.selectedItem.toString().toInt(),
                    startingDate,
                    days,
                    expireDate,
                    reminderAddNotes.text.toString()
                )

                val reminderListNormalizedOld = Utils.getSingleReminderListNormalized(
                    medName!!,
                    UserInformation.getSpecificMedicine(medName!!)!!.medicineType,
                    reminder.copy()
                )

                if(UserInformation.editReminder(medName!!,reminder,newReminder)){

                    reminderListNormalizedOld.forEach {
                        NotifyPlanner.remove(this,it)
                    }

                    Utils.getSingleReminderListNormalized(
                        medName!!,
                        UserInformation.getSpecificMedicine(medName!!)!!.medicineType,
                        newReminder
                    ).forEach{
                        NotifyPlanner.planSingleAlarm(
                            this,
                            getSystemService(Context.ALARM_SERVICE) as AlarmManager,
                            it
                        )
                    }

                    finish()

                } else {
                    Utils.buildAlertDialog(
                        this,
                        getString(R.string.genericInfoError),
                        getString(R.string.message_title)
                    )
                }

            } else {
                Utils.buildAlertDialog(
                    this,
                    getString(R.string.genericInfoError),
                    getString(R.string.message_title)
                )
            }
        }

        nextTextView.setOnClickListener {

        }

        backTextView.setOnClickListener {
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
                if(reminder.isSingleDayRem())
                    expireDate = calStart.time

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
            setContentView(R.layout.fragment_day_reminder_time)
            buttonDateReminder = findViewById(R.id.buttonDateReminder)
            hourSpinner = findViewById(R.id.hourReminderSpinner)
            minuteSpinner = findViewById(R.id.minutesReminderSpinner)

            setContentView(R.layout.fragment_form_reminder_one_day_quantity)
            saveTextView = findViewById(R.id.textViewSave)
            backTextView = findViewById(R.id.textViewBack)
            reminderAddNotes = findViewById(R.id.reminderAddNotesEdit)
            dosageSpinner = findViewById(R.id.dosageQtyReminder)
        } else {
            setContentView(R.layout.fragment_form_seq_reminder_time)
            buttonDateReminder = findViewById(R.id.buttonDateStart)
            saveButton = findViewById(R.id.saveButtonReminderSeq)
            backTextView = findViewById(R.id.abortButtonReminderSeq)
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

         */
    }

    /*
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
        for (i in 0..11)
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

     */

}

