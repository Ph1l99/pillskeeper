package com.pillskeeper.activity.medicine.reminder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pillskeeper.R
import com.pillskeeper.activity.medicine.medicineformfragments.FormAdapter
import com.pillskeeper.activity.medicine.medicineformfragments.NoSlideViewPager
import com.pillskeeper.data.ReminderMedicine
import kotlinx.android.synthetic.main.activity_edit_reminder.*


class EditReminderActivity : AppCompatActivity() {

    private val VIEW_PAGER_REMINDER_ID = 4040

    private          var medName                : String? = null

    companion object {
        var oldReminder: ReminderMedicine? = null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit_reminder)

        oldReminder = intent.getSerializableExtra(ReminderListActivity.REMINDER_MEDICINE) as ReminderMedicine
        medName = intent.getStringExtra(ReminderListActivity.MEDICINE_NAME)

        val viewPager = NoSlideViewPager(this)
        viewPager.id = VIEW_PAGER_REMINDER_ID
        relativeLayout.addView(viewPager)

        val adapter = FormAdapter(supportFragmentManager, intent, viewPager)
        FormAdapter.isANewMedicine = false
        FormAdapter.resetReminder()
        FormAdapter.pillName = medName
        FormAdapter.startDay = oldReminder!!.startingDay
        FormAdapter.finishDay = oldReminder!!.expireDate
        FormAdapter.reminderHour = oldReminder!!.hours
        FormAdapter.reminderMinute = oldReminder!!.minutes
        FormAdapter.reminderQuantity = oldReminder!!.dosage
        FormAdapter.reminderNotes = oldReminder!!.additionNotes
        FormAdapter.days = oldReminder!!.days
        FormAdapter.isAReminderEditing = true
        viewPager.adapter = adapter

        if(oldReminder!!.isSingleDayRem()) {
            viewPager.currentItem = FormAdapter.FORM_ONE_DAY_REMINDER_TIME
        } else {
            viewPager.currentItem = FormAdapter.FORM_SEQ_DATE_REMINDER
        }
    }

}

