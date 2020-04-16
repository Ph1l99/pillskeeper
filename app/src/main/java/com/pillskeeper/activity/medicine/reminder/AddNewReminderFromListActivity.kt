package com.pillskeeper.activity.medicine.reminder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pillskeeper.R
import com.pillskeeper.activity.medicine.medicineformfragments.FormAdapter
import com.pillskeeper.activity.medicine.medicineformfragments.NoSlideViewPager
import com.pillskeeper.activity.medicine.reminder.reminderformfragments.FormReminderOneDayTimeFrag
import com.pillskeeper.activity.medicine.reminder.reminderformfragments.FormReminderSeqFrag
import com.pillskeeper.datamanager.UserInformation
import kotlinx.android.synthetic.main.activity_add_new_reminder_from_list.*

class AddNewReminderFromListActivity : AppCompatActivity() {

    //actionType: TRUE=OneDay       FALSE=SeqDay

    private var fragType: Boolean = true
    private var medName: String? = ""

    private lateinit var viewPager: NoSlideViewPager
    private val VIEW_PAGER_REMINDER_ID = 3030


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_reminder_from_list)

        viewPager = NoSlideViewPager(this)//TODO check viewPager
        viewPager.id = VIEW_PAGER_REMINDER_ID
        relativeLayoutReminder.addView(viewPager)

        fragType = intent.getBooleanExtra(ReminderChooseDialog.FRAG_TYPE,false)
        medName = intent.getStringExtra(ReminderChooseDialog.MED_NAME)
        val adapter = FormAdapter(supportFragmentManager, intent, viewPager)
        viewPager.adapter = adapter


        if(fragType)
            viewPager.currentItem = FormAdapter.FORM_ONE_DAY_REMINDER_TIME
        else
            FormReminderSeqFrag(viewPager, medName)//TODO check viewPager

        /*
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frameContainer,fragment)
        transaction.addToBackStack(null)
        transaction.commit()

         */
    }
}
