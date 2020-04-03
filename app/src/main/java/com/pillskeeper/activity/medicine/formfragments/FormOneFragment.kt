package com.pillskeeper.activity.medicine.formfragments

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
import com.pillskeeper.activity.medicine.PillsFormActivity
import com.pillskeeper.activity.medicine.PillsFormActivity.Companion.CAMERA_REQUEST
import com.pillskeeper.activity.medicine.PillsFormActivity.Companion.REMINDER
import com.pillskeeper.activity.medicine.PillsFormActivity.Companion.REMINDER_INSERT_ACTIVITY
import com.pillskeeper.activity.medicine.TextReaderActivity
import com.pillskeeper.data.LocalMedicine
import com.pillskeeper.data.ReminderMedicine
import com.pillskeeper.data.RemoteMedicine
import com.pillskeeper.enums.MedicineTypeEnum
import java.util.*
import kotlin.collections.ArrayList


class FormOneFragment(private val intent: Intent, viewPager: PillsViewPager) : Fragment() {

    companion object {
        const val FORM_TWO = 1
    }

    private var remoteMedicine: RemoteMedicine? = null
    private var localMedicine: LocalMedicine? = null
    private var isEditing: Boolean = false
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

        if(intent.getSerializableExtra(PillsFormActivity.LOCAL_MEDICINE) != null){
            localMedicine = intent.getSerializableExtra(PillsFormActivity.LOCAL_MEDICINE) as LocalMedicine
            editTextNameMed.setText(localMedicine!!.name)
            editTextNameMed.setRawInputType(0)
            setAllEnable(false)
            reminderList = localMedicine!!.reminders
            //editTextTotQuantity.setText(if(localMedicine!!.totalPills != 0F) localMedicine!!.totalPills.toString() else "0")
            //editTextRemQuantity.setText(if(localMedicine!!.remainingPills != 0F) localMedicine!!.remainingPills.toString() else "0")
            //buttonDenyMed.text = getText(R.string.closeButton)
            //buttonConfirmMed.text = getText(R.string.editButton)
        } else if (intent.getSerializableExtra(PillsFormActivity.REMOTE_MEDICINE) != null) {
            remoteMedicine = intent.getSerializableExtra(PillsFormActivity.REMOTE_MEDICINE) as RemoteMedicine
            editTextNameMed.setText(remoteMedicine!!.name)
            editTextNameMed.setRawInputType(0)
            spinnerMedicineType.isEnabled = false
        }

        initSpinner()

        /*LISTENERS*/
        buttonCamera.setOnClickListener {
            if (context?.let { it1 -> checkSelfPermission(it1, Manifest.permission.CAMERA) } == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(context, TextReaderActivity::class.java)
                startActivityForResult(intent, PillsFormActivity.CAMERA_REQUEST)
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.CAMERA),
                    PillsFormActivity.REQUEST_CAMERA_PERMISSION_ID
                )
            }
        }

        editTextNameMed.addTextChangedListener {
            if(editTextNameMed.text.isNotEmpty()){
                textViewNext.visibility = View.VISIBLE
            }
        }

        textViewNext.setOnClickListener {
            FormAdapter.pillName = editTextNameMed.text.toString()
            FormAdapter.medicineType = getTypeFromText(spinner.selectedItem.toString())
            viewPager.currentItem = FORM_TWO
        }

        return view
    }


    private fun initSpinner() {
        val medTypeValues: ArrayList<String> = ArrayList()
        var pos: Short = 0
        when {
            remoteMedicine != null -> {
                medTypeValues.add(getText(remoteMedicine!!.medicineType.text).toString())
            }
            localMedicine == null || isEditing -> {
                var counter = 0
                MedicineTypeEnum.values().forEach { medTypeEnum ->
                    if (medTypeEnum != MedicineTypeEnum.UNDEFINED) {
                        medTypeValues.add(getText(medTypeEnum.text).toString())
                        if(isEditing && medTypeEnum.type == localMedicine?.medicineType?.type)
                            pos = counter.toShort()
                        counter++
                    }
                }
            }
            else -> {
                medTypeValues.add(getText(localMedicine!!.medicineType.text).toString())
            }
        }

        val arrayAdapter =
            context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, medTypeValues) }
        arrayAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerMedicineType.adapter = arrayAdapter

        if(isEditing){
            spinnerMedicineType.setSelection(pos.toInt())
        }
    }

    private fun setAllEnable(flag: Boolean){

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST -> {
                    val pillName: String? = data!!.getStringExtra("pillName")
                    editTextNameMed.text = SpannableStringBuilder(pillName)
                }
                REMINDER_INSERT_ACTIVITY -> {
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
