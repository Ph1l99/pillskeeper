package com.pillskeeper.activity.medicine.reminder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pillskeeper.R
import com.pillskeeper.activity.medicine.medicineformfragments.FormAdapter
import com.pillskeeper.activity.medicine.medicineformfragments.NoSlideViewPager
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

        viewPager = NoSlideViewPager(this)
        viewPager.id = VIEW_PAGER_REMINDER_ID
        relativeLayoutReminder.addView(viewPager)

        fragType = intent.getBooleanExtra(ReminderChooseDialog.FRAG_TYPE, false)
        medName = intent.getStringExtra(ReminderChooseDialog.MED_NAME)

        val adapter = FormAdapter(supportFragmentManager, intent, viewPager)
        /*Queste due istruzion i inizializzano il form con una medicina che è gia stata inserita*/
        FormAdapter.isANewMedicine = false
        FormAdapter.resetReminder()
        FormAdapter.pillName = medName
        viewPager.adapter = adapter


        if (fragType)
            viewPager.currentItem = FormAdapter.FORM_ONE_DAY_REMINDER_TIME
        else
            viewPager.currentItem = FormAdapter.FORM_SEQ_DATE_REMINDER

    }
}
