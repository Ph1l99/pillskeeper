package com.pillskeeper.activity.medicine.medicineformfragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.pillskeeper.R
import com.pillskeeper.activity.medicine.MedicineLocaleListActivity
import com.pillskeeper.activity.medicine.ReminderChooseDialog

class FormSaveOrReminderFragment(private val viewPager: MedicineViewPager) : Fragment() {

    private lateinit var textViewBack: TextView
    private lateinit var textViewConfirm: TextView
    private lateinit var buttonAddReminder: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_form_three, container, false)

        textViewBack = view.findViewById(R.id.textViewBack)
        textViewConfirm = view.findViewById(R.id.textViewConfirm)
        buttonAddReminder = view.findViewById(R.id.buttonAddReminder)

        textViewBack.setOnClickListener {
            viewPager.currentItem = FormAdapter.FORM_QUANTITY
        }

        textViewConfirm.setOnClickListener {
            FormAdapter.addNewMedicine()
            val intent = Intent(context, MedicineLocaleListActivity::class.java)
            startActivity(intent)
            FormAdapter.closeForm()
        }

        buttonAddReminder.setOnClickListener {
            ReminderChooseDialog(FormAdapter.formActivity,viewPager).show()
        }

        return view
    }
}



