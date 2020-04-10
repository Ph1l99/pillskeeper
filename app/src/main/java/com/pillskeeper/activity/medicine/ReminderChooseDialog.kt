package com.pillskeeper.activity.medicine

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.pillskeeper.R
import com.pillskeeper.activity.medicine.medicineformfragments.FormAdapter
import com.pillskeeper.activity.medicine.medicineformfragments.MedicineViewPager
import kotlinx.android.synthetic.main.dialog_choose_reminder.*

class ReminderChooseDialog(context: Context, private val viewPager: MedicineViewPager): Dialog(context) {

    init {
        setCancelable(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_choose_reminder)


        buttonDayFixed.setOnClickListener {
            viewPager.currentItem = FormAdapter.FORM_ONE_DAY_REMINDER
            dismiss()
        }

        buttonSequence.setOnClickListener {
            viewPager.currentItem = FormAdapter.FORM_SEQ_REMINDER
            dismiss()
        }

    }
}