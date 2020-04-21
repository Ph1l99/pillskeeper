package com.pillskeeper.activity.medicine.reminder.reminderformfragments

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import com.pillskeeper.R
import com.pillskeeper.activity.medicine.medicineformfragments.FormAdapter
import com.pillskeeper.activity.medicine.medicineformfragments.NoSlideViewPager
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class FormReminderSeqDateFrag(private var viewPager: NoSlideViewPager) : Fragment() {

    private lateinit var nextTextView: TextView
    private lateinit var buttonDateStart: Button
    private lateinit var buttonDateEnd: Button


    private var expDateSelected: Date? = null
    private var startDateSelected: Date? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_form_reminder_seq_date, container, false)

        buttonDateEnd = view.findViewById(R.id.buttonDateEnd)
        buttonDateStart = view.findViewById(R.id.buttonDateStart)
        nextTextView = view.findViewById(R.id.textViewNext)



        val calExp = Calendar.getInstance()


        val yearExp = calExp.get(Calendar.YEAR)
        val monthExp = calExp.get(Calendar.MONTH)
        val dayExp = calExp.get(Calendar.DAY_OF_MONTH)

        val calStart = Calendar.getInstance()

        val yearStart = calExp.get(Calendar.YEAR)
        val monthStart = calExp.get(Calendar.MONTH)
        val dayStart = calExp.get(Calendar.DAY_OF_MONTH)




        /*LISTENERS*/
        nextTextView.setOnClickListener {
            viewPager.currentItem = FormAdapter.FORM_SEQ_TIME_REMINDER
        }


        buttonDateStart.setOnClickListener {
            DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                buttonDateStart.text = getString(R.string.dateButtonFormatted,dayOfMonth,monthOfYear+1,year)

                calStart.set(Calendar.YEAR, year)
                calStart.set(Calendar.MONTH, monthOfYear)
                calStart.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                calStart.set(Calendar.HOUR_OF_DAY, 0)
                calStart.set(Calendar.MINUTE, 0)
                calStart.set(Calendar.SECOND, 0)
                calStart.set(Calendar.MILLISECOND, 0)

                startDateSelected = calStart.time

            }, yearStart, monthStart, dayStart).show()
        }


         buttonDateEnd.setOnClickListener {
            DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                buttonDateEnd.text = getString(R.string.dateButtonFormatted,dayOfMonth,monthOfYear+1,year)

                calExp.set(Calendar.YEAR, year)
                calExp.set(Calendar.MONTH, monthOfYear)
                calExp.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                calExp.set(Calendar.HOUR_OF_DAY, 0)
                calExp.set(Calendar.MINUTE, 0)
                calExp.set(Calendar.SECOND, 0)
                calExp.set(Calendar.MILLISECOND, 0)

                expDateSelected = calExp.time

            }, yearExp, monthExp, dayExp).show()
        }


        return view
    }

}
