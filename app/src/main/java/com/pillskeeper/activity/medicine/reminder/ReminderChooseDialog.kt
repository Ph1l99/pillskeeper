package com.pillskeeper.activity.medicine.reminder

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import com.pillskeeper.R
import com.pillskeeper.activity.medicine.medicineformfragments.FormAdapter
import com.pillskeeper.activity.medicine.medicineformfragments.NoSlideViewPager
import kotlinx.android.synthetic.main.dialog_choose_reminder.*

class ReminderChooseDialog(context: Context, private val viewPager: NoSlideViewPager? = null, private val medName: String? = null): Dialog(context) {

    //actionType: TRUE=OneDay       FALSE=SeqDay
    companion object{
        const val FRAG_TYPE = "fragType"
        const val MED_NAME = "medName"
    }

    init {
        setCancelable(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_choose_reminder)


        buttonDayFixed.setOnClickListener {
            if (viewPager != null)
                viewPager.currentItem = FormAdapter.FORM_ONE_DAY_REMINDER_TIME
            else
                context.startActivity(
                    Intent(context,AddNewReminderFromListActivity::class.java)
                        .apply {
                            putExtra(FRAG_TYPE,true)
                            putExtra(MED_NAME, medName)
                        }
                )
            dismiss()
        }

        buttonSequence.setOnClickListener {
            if (viewPager != null)
                viewPager.currentItem = FormAdapter.FORM_SEQ_REMINDER
            else
                context.startActivity(Intent(context,AddNewReminderFromListActivity::class.java).apply {
                    putExtra(FRAG_TYPE,false)
                    putExtra(MED_NAME,medName)
                })

            dismiss()
        }

    }
}