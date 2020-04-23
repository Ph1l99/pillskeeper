package com.pillskeeper.activity.medicine.reminder.reminderformfragments

import android.app.AlarmManager
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.pillskeeper.R
import com.pillskeeper.activity.medicine.medicineformfragments.FormAdapter
import com.pillskeeper.activity.medicine.medicineformfragments.NoSlideViewPager
import com.pillskeeper.data.ReminderMedicine
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.enums.DaysEnum
import com.pillskeeper.notifier.NotifyPlanner
import com.pillskeeper.utility.Utils
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class FormReminderSeqQuantityFrag(private var viewPager: NoSlideViewPager) : Fragment() {

    private lateinit var backTextView: TextView
    private lateinit var saveTextView: TextView
    private lateinit var dosageSpinnerReminder: Spinner
    private lateinit var notesReminder: EditText
    private var oldReminder: ReminderMedicine? = null
    private var medName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_form_reminder_seq_quantity, container, false)

        backTextView = view.findViewById(R.id.textViewBack)
        saveTextView = view.findViewById(R.id.textViewSave)
        dosageSpinnerReminder = view.findViewById(R.id.dosageSpinnerReminder)
        notesReminder = view.findViewById(R.id.reminderAddNotesEdit)

        initSpinner()

        if(FormAdapter.isAReminderEditing) {
            oldReminder = ReminderMedicine(
                FormAdapter.reminderQuantity,
                FormAdapter.reminderMinute,
                FormAdapter.reminderHour,
                FormAdapter.startDay!!,
                null,
                FormAdapter.finishDay,
                FormAdapter.reminderNotes
            )

            medName = FormAdapter.pillName
            notesReminder.setText(FormAdapter.reminderNotes)
        }


        /*LISTENERS*/
        backTextView.setOnClickListener {
            viewPager.currentItem = FormAdapter.FORM_SEQ_TIME_REMINDER
        }

        saveTextView.setOnClickListener {
            if(dosageSpinnerReminder.selectedItem != null){
                FormAdapter.reminderQuantity = dosageSpinnerReminder.selectedItem.toString().toFloat()

                if(notesReminder.text != null){
                    FormAdapter.reminderNotes = notesReminder.text.toString()
                }

                val newRem = ReminderMedicine(
                    FormAdapter.reminderQuantity,
                    FormAdapter.reminderMinute,
                    FormAdapter.reminderHour,
                    FormAdapter.startDay!!,
                    FormAdapter.days,
                    FormAdapter.finishDay,
                    FormAdapter.reminderNotes
                )

                if(FormAdapter.isAReminderEditing){
                   //TODO editing
                    val reminderListNormalizedOld = Utils.getSingleReminderListNormalized(
                        medName!!,
                        UserInformation.getSpecificMedicine(medName!!)!!.medicineType,
                        oldReminder?.copy()!!
                    )

                    if (UserInformation.editReminder(medName!!, oldReminder!!, newRem)) {

                        reminderListNormalizedOld.forEach {
                            NotifyPlanner.remove(activity?.applicationContext!!, it)
                        }

                        Utils.getSingleReminderListNormalized(
                            medName!!,
                            UserInformation.getSpecificMedicine(medName!!)!!.medicineType,
                            newRem
                        ).forEach {
                            NotifyPlanner.planSingleAlarm(
                                activity?.applicationContext!!,
                                activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager,
                                it
                            )
                        }
                    }
                } else { if(FormAdapter.isANewMedicine){
                        //TODO new medicine
                        viewPager.currentItem = FormAdapter.FORM_SAVE_OR_REMINDER
                    } else  if (UserInformation.addNewReminder(FormAdapter.pillName!!, newRem)) {
                        Utils.getSingleReminderListNormalized(
                            FormAdapter.pillName!!,
                            UserInformation.getSpecificMedicine(FormAdapter.pillName!!)!!.medicineType,
                            newRem
                        ).forEach {
                            NotifyPlanner.planSingleAlarm(
                                requireActivity(),
                                activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager,
                                it
                            )
                        }
                        activity?.finish()
                    }
                }

            } else {
                Toast.makeText(context, "Selezionare la quanittà da assumere!", Toast.LENGTH_LONG).show()
            }

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

    private fun  initSpinner(){
        val qtyArray = ArrayList<String>()
        for (i in 0..10)
            qtyArray.add("${i/2F}")

        val arrayAdapterDosage = ArrayAdapter(UserInformation.context,android.R.layout.simple_spinner_item, qtyArray)
        arrayAdapterDosage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dosageSpinnerReminder.adapter = arrayAdapterDosage
        if(FormAdapter.isAReminderEditing)
            dosageSpinnerReminder.setSelection((FormAdapter.reminderQuantity * 2F).toInt())
    }



}
