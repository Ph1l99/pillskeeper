package com.pillskeeper.activity.medicine.reminder.reminderformfragments

import android.app.AlarmManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.pillskeeper.R
import com.pillskeeper.activity.medicine.medicineformfragments.FormAdapter
import com.pillskeeper.data.ReminderMedicine
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.notifier.NotifyPlanner
import com.pillskeeper.utility.Utils
import java.util.*


class FormReminderOneDayQuantityFrag(private val viewPager: ViewPager?, private val medName: String?) : Fragment() {

    private lateinit var saveTextReminder: TextView
    private lateinit var textViewBack: TextView
    private lateinit var dosageQtyReminder: Spinner
    private lateinit var reminderAddNotesEdit: TextView
    private var oldReminder: ReminderMedicine? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_form_reminder_one_day_quantity, container, false)

        saveTextReminder = view.findViewById(R.id.textViewSave)
        textViewBack = view.findViewById(R.id.textViewBack)
        dosageQtyReminder = view.findViewById(R.id.dosageQtyReminder)
        reminderAddNotesEdit = view.findViewById(R.id.reminderAddNotesEdit)

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

            val qtyArray = ArrayList<String>()
            for (i in 0..10)
                qtyArray.add("${i / 2F}")

            val arrayAdapterDosage = ArrayAdapter(
                activity?.applicationContext!!,
                android.R.layout.simple_spinner_item,
                qtyArray
            )
            arrayAdapterDosage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            dosageQtyReminder.adapter = arrayAdapterDosage
            dosageQtyReminder.setSelection((FormAdapter.reminderQuantity * 2F).toInt())


            //TODO controllare che non si spacchi
            reminderAddNotesEdit.text = FormAdapter.reminderNotes
        }

        initSpinner()

        /*
        saveTextReminder.setOnClickListener {
            if(dateSelected != null && dosageQtyReminder.selectedItem.toString().toFloat() > 0){
                cal.time = dateSelected!!
                cal.set(Calendar.HOUR_OF_DAY, hourReminderSpinner.selectedItem.toString().toInt())
                cal.set(Calendar.MINUTE, minutesReminderSpinner.selectedItem.toString().toInt())
                if (cal.time > Date()){
                    val reminder = ReminderMedicine(
                        dosageQtyReminder.selectedItem.toString().toFloat(),
                        minutesReminderSpinner.selectedItem.toString().toInt(),
                        hourReminderSpinner.selectedItem.toString().toInt(),
                        cal.time,
                        null,
                        cal.time,
                        reminderAddNotesEditT.text.toString()
                    )
                    if(viewPager != null) {
                        FormAdapter.addReminder(reminder)
                        viewPager.currentItem = FormAdapter.FORM_SAVE_OR_REMINDER
                    } else {
                        if (UserInformation.addNewReminder(medName!!,reminder)) {
                            Utils.getSingleReminderListNormalized(
                                medName,
                                UserInformation.getSpecificMedicine(medName)!!.medicineType,
                                reminder
                            ).forEach {
                                NotifyPlanner.planSingleAlarm(
                                    activity!!,
                                    activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager,
                                    it
                                )
                            }
                            activity?.finish()
                        }
                    }
                } else {
                    Toast.makeText(UserInformation.context,"Perfavore inserire informazioni corrette!",Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(UserInformation.context,"Perfavore inserire informazioni corrette!",
                    Toast.LENGTH_LONG).show()
            }
        }

             */

        /*LISTENERS*/
        textViewBack.setOnClickListener {
            viewPager?.currentItem = FormAdapter.FORM_ONE_DAY_REMINDER_TIME
        }

        saveTextReminder.setOnClickListener {

            FormAdapter.reminderQuantity = dosageQtyReminder.selectedItem.toString().toFloat()
            FormAdapter.reminderNotes = reminderAddNotesEdit.toString()

            if (dosageQtyReminder.selectedItem.toString().toFloat() > 0) {
                val newReminder = ReminderMedicine(
                    FormAdapter.reminderQuantity,
                    FormAdapter.reminderMinute,
                    FormAdapter.reminderHour,
                    FormAdapter.startDay!!,
                    null,
                    FormAdapter.finishDay,
                    FormAdapter.reminderNotes
                )
                if (FormAdapter.isANewMedicine) {       //caso in cui la medicina è appena stata inserita
                    FormAdapter.addReminder(newReminder)
                    viewPager?.currentItem = FormAdapter.FORM_SAVE_OR_REMINDER
                } else {
                    if (FormAdapter.isAReminderEditing) {   //caso in cui la medicina è stata inserita in passato

                        val reminderListNormalizedOld = Utils.getSingleReminderListNormalized(
                            medName!!,
                            UserInformation.getSpecificMedicine(medName)!!.medicineType,
                            oldReminder?.copy()!!
                        )

                        if (UserInformation.editReminder(medName, oldReminder!!, newReminder)) {

                            reminderListNormalizedOld.forEach {
                                NotifyPlanner.remove(activity?.applicationContext!!, it)
                            }

                            Utils.getSingleReminderListNormalized(
                                medName!!,
                                UserInformation.getSpecificMedicine(medName)!!.medicineType,
                                newReminder
                            ).forEach {
                                NotifyPlanner.planSingleAlarm(
                                    activity?.applicationContext!!,
                                    activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager,
                                    it
                                )
                            }
                        } else if (UserInformation.addNewReminder(medName, newReminder)) {
                            Utils.getSingleReminderListNormalized(
                                medName,
                                UserInformation.getSpecificMedicine(medName)!!.medicineType,
                                newReminder
                            ).forEach {
                                NotifyPlanner.planSingleAlarm(
                                    requireActivity(),
                                    activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager,
                                    it
                                )
                            }
                            activity?.finish()
                        } else {
                            Toast.makeText(
                                UserInformation.context,
                                "Per favore inserire informazioni corrette!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
        return view
    }

    private fun  initSpinner(){
        val qtyArray = ArrayList<String>()
        for (i in 0..10)
            qtyArray.add("${i/2F}")

        val arrayAdapterDosage = ArrayAdapter(UserInformation.context,android.R.layout.simple_spinner_item, qtyArray)
        arrayAdapterDosage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dosageQtyReminder.adapter = arrayAdapterDosage
    }

}
