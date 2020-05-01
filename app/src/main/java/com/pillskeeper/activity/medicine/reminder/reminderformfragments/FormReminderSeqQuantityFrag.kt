package com.pillskeeper.activity.medicine.reminder.reminderformfragments

import android.app.AlarmManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.pillskeeper.R
import com.pillskeeper.activity.medicine.medicineformfragments.FormAdapter
import com.pillskeeper.activity.medicine.medicineformfragments.NoSlideViewPager
import com.pillskeeper.activity.medicine.reminder.EditReminderActivity
import com.pillskeeper.data.ReminderMedicine
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.notifier.NotifyPlanner
import com.pillskeeper.utility.InitSpinner
import com.pillskeeper.utility.Utils
import java.util.*

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
            oldReminder = EditReminderActivity.oldReminder
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

                if(FormAdapter.isANewMedicine){
                    FormAdapter.addReminder(newRem)
                    viewPager.currentItem = FormAdapter.FORM_SAVE_OR_REMINDER
                } else {
                    if (FormAdapter.isAReminderEditing) {
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
                            ).filter { it.reminder.startingDay < Date(Utils.dataNormalizationLimit()) }
                                .forEach {
                                    NotifyPlanner.planSingleAlarm(
                                        activity?.applicationContext!!,
                                        activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager,
                                        it
                                    )
                                }
                        }
                        activity?.finish()
                    } else if (UserInformation.addNewReminder(FormAdapter.pillName!!, newRem)) {
                        Utils.getSingleReminderListNormalized(
                            FormAdapter.pillName!!,
                            UserInformation.getSpecificMedicine(FormAdapter.pillName!!)!!.medicineType,
                            newRem
                        ).filter { it.reminder.startingDay < Date(Utils.dataNormalizationLimit()) }
                            .forEach {
                                NotifyPlanner.planSingleAlarm(
                                    requireActivity(),
                                    activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager,
                                    it
                                )
                        }
                        activity?.finish()
                    } else {
                        Utils.buildAlertDialog(
                            requireContext(),
                            "Errore nell'inserimento",
                            getString(R.string.message_title)
                        ).show()
                    }
                }

            } else {
                Toast.makeText(context, "Selezionare la quanità da assumere!", Toast.LENGTH_LONG).show()//todo change with modal
            }
        }

        return view
    }

    private fun  initSpinner(){
        dosageSpinnerReminder.adapter = InitSpinner.initSpinnerDosage(requireActivity())
        if(FormAdapter.isAReminderEditing)
            dosageSpinnerReminder.setSelection((FormAdapter.reminderQuantity / InitSpinner.DOSAGE_MULTI).toInt())
    }



}
