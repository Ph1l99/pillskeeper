package com.pillskeeper.activity.medicine.medicineformfragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.pillskeeper.R
import com.pillskeeper.activity.medicine.MedicineFormActivity
import com.pillskeeper.activity.medicine.MedicineFormActivity.Companion.CAMERA_REQUEST
import com.pillskeeper.activity.medicine.MedicineFormActivity.Companion.REMINDER
import com.pillskeeper.activity.medicine.MedicineFormActivity.Companion.REMINDER_INSERT_ACTIVITY
import com.pillskeeper.activity.medicine.TextReaderActivity
import com.pillskeeper.data.ReminderMedicine
import com.pillskeeper.enums.MedicineTypeEnum
import java.util.*
import kotlin.collections.ArrayList


class FormNameTypeFragment(private val intent: Intent, viewPager: PillsViewPager) : Fragment() {

    private val viewPager: ViewPager = viewPager

    private var reminderList: LinkedList<ReminderMedicine>? = null

    private lateinit var spinnerMedicineType: Spinner
    private lateinit var buttonCamera: Button
    private lateinit var textViewNext: TextView
    private lateinit var editTextNameMed: EditText
    private lateinit var spinner: Spinner

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_form_one, container, false)

        spinnerMedicineType = view.findViewById(R.id.spinnerMedicineType)
        buttonCamera = view.findViewById(R.id.buttonCamera)
        textViewNext = view.findViewById(R.id.textViewNext)
        editTextNameMed = view.findViewById(R.id.editTextNameMed)
        spinner = view.findViewById(R.id.spinnerMedicineType)

        if (intent.getSerializableExtra(MedicineFormActivity.REMOTE_MEDICINE) != null) {
            editTextNameMed.setText(FormAdapter.remoteMedicine!!.name)
            editTextNameMed.setRawInputType(0)
            spinnerMedicineType.isEnabled = false
            textViewNext.visibility = View.VISIBLE
        }

        initSpinner()

        /*LISTENERS*/
        buttonCamera.setOnClickListener {
            if (context?.let { it1 -> checkSelfPermission(it1, Manifest.permission.CAMERA) } == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(context, TextReaderActivity::class.java)
                startActivityForResult(intent, CAMERA_REQUEST)
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.CAMERA),
                    MedicineFormActivity.REQUEST_CAMERA_PERMISSION_ID
                )
            }
        }

        editTextNameMed.addTextChangedListener {
            if(editTextNameMed.text.isNotEmpty())
                textViewNext.visibility = View.VISIBLE

        }

        textViewNext.setOnClickListener {
            FormAdapter.pillName = editTextNameMed.text.toString()
            FormAdapter.medicineType = getTypeFromText(spinner.selectedItem.toString())
            viewPager.currentItem = FormAdapter.FORM_QUANTITY
        }

        return view
    }


    private fun initSpinner() {
        val medTypeValues: ArrayList<String> = ArrayList()
        when {
            FormAdapter.remoteMedicine != null -> {
                medTypeValues.add(getText(FormAdapter.remoteMedicine!!.medicineType.text).toString())
            }
            else -> {
                MedicineTypeEnum.values().forEach { medTypeEnum ->
                    if (medTypeEnum != MedicineTypeEnum.UNDEFINED)
                        medTypeValues.add(getText(medTypeEnum.text).toString())
                }
            }
        }

        val arrayAdapter =
            context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, medTypeValues) }
        arrayAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerMedicineType.adapter = arrayAdapter

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST -> {
                    val pillName: String? = data!!.getStringExtra("pillName")
                    editTextNameMed.text = SpannableStringBuilder(pillName)
                }
                REMINDER_INSERT_ACTIVITY -> { //todo spostare nel fragment 3
                    if(reminderList == null)
                        reminderList = LinkedList()
                    reminderList!!.add(data!!.getSerializableExtra(REMINDER) as ReminderMedicine)
                }
                else -> super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun getTypeFromText(text: String): MedicineTypeEnum {
        return when (text) {
            getText(MedicineTypeEnum.PILLS.text) -> {
                MedicineTypeEnum.PILLS
            }
            getText(MedicineTypeEnum.SYRUP.text) -> {
                MedicineTypeEnum.SYRUP
            }
            getText(MedicineTypeEnum.SUPPOSITORY.text) -> {
                MedicineTypeEnum.SUPPOSITORY
            }
            else -> MedicineTypeEnum.UNDEFINED
        }
    }


}
