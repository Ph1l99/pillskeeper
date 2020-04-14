package com.pillskeeper.activity.medicine.reminder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pillskeeper.R
import com.pillskeeper.activity.medicine.reminder.reminderformfragments.FormReminderOneDayFrag
import com.pillskeeper.activity.medicine.reminder.reminderformfragments.FormReminderSeqFrag

class AddNewReminderFromListActivity : AppCompatActivity() {

    //actionType: TRUE=OneDay       FALSE=SeqDay

    var fragType: Boolean = true
    var medName: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_reminder_from_list)

        fragType = intent.getBooleanExtra(ReminderChooseDialog.FRAG_TYPE,false)
        medName = intent.getStringExtra(ReminderChooseDialog.FRAG_TYPE)

        val fragment =
            if(fragType)
                FormReminderOneDayFrag(null, medName)
            else
                FormReminderSeqFrag(null, medName)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frameContainer,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
