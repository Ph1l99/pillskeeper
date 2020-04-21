package com.pillskeeper.activity.medicine.reminder.reminderformfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.pillskeeper.R
import com.pillskeeper.activity.medicine.medicineformfragments.FormAdapter
import com.pillskeeper.activity.medicine.medicineformfragments.NoSlideViewPager

/**
 * A simple [Fragment] subclass.
 */
class FormReminderSeqQuantityFrag(private var viewPager: NoSlideViewPager) : Fragment() {

    lateinit var backTextView: TextView
    lateinit var saveTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_form_reminder_seq_quantity, container, false)

        backTextView = view.findViewById(R.id.textViewBack)
        saveTextView = view.findViewById(R.id.textViewSave)



        /*LISTENERS*/

        backTextView.setOnClickListener {
            viewPager.currentItem = FormAdapter.FORM_SEQ_TIME_REMINDER
        }

        saveTextView.setOnClickListener {

            activity?.finish()

            //TODO deve fare le cose che sono commentate
            /*
            val days = buildDaysArray()

            if(checkValue(days)) {
                if (startDateSelected == null)
                    startDateSelected = Date()

                val newRem = ReminderMedicine(
                    dosageSpinnerReminder.selectedItem.toString().toFloat(),
                    spinnerMinutesRem2.selectedItem.toString().toInt(),
                    spinnerHoursRem2.selectedItem.toString().toInt(),
                    startDateSelected!!,
                    days,
                    expDateSelected,
                    editTextAddNotesRem.text.toString()
                )

                if(viewPager != null) {
                    FormAdapter.addReminder(newRem)
                    viewPager.currentItem = FormAdapter.FORM_SAVE_OR_REMINDER
                } else {
                    if(UserInformation.addNewReminder(medName!!,newRem)) {

                        Utils.getSingleReminderListNormalized(
                            medName,
                            UserInformation.getSpecificMedicine(medName)!!.medicineType,
                            newRem
                        ).forEach {
                            NotifyPlanner.planSingleAlarm(
                                activity?.applicationContext!!,
                                activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager,
                                it
                            )
                        }
                        activity?.finish()
                    } else {
                        Utils.buildAlertDialog(
                            requireActivity(),
                            "Inserimento fallito!",
                            getString(R.string.message_title)
                        )
                    }
                }
            } else {
                Utils.buildAlertDialog(
                    requireActivity(),
                    "perfavore inserire valori corretti",
                    getString(R.string.message_title)
                )
            }

             */
        }

        return view
    }

}
