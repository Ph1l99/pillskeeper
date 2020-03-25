package com.pillskeeper.activity.pills


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pillskeeper.R
import com.pillskeeper.activity.pills.reminder.ReminderActivity
import com.pillskeeper.data.LocalMedicine
import com.pillskeeper.data.ReminderMedicine
import com.pillskeeper.data.RemoteMedicine
import com.pillskeeper.datamanager.LocalDatabase
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.enums.MedicineTypeEnum
import com.pillskeeper.utility.Utils
import kotlinx.android.synthetic.main.activity_pills_form.*
import java.util.*
import kotlin.collections.ArrayList

class PillsFormActivity : AppCompatActivity() {

    companion object {
        const val CAMERA_REQUEST = 0
        const val REMINDER_INSERT_ACTIVITY = 1
        const val REMOTE_MEDICINE = "remoteMedicine"
        const val LOCAL_MEDICINE = "localMedicine"
        const val REMINDER = "reminder"
        const val REQUEST_CAMERA_PERMISSION_ID = 1
    }

    private var isEditing: Boolean = false
    private var reminderList: LinkedList<ReminderMedicine>? = null
    private var remoteMedicine: RemoteMedicine? = null
    private var localMedicine: LocalMedicine? = null
    private lateinit var stdLayout: Drawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pills_form)

        if(intent.getSerializableExtra(LOCAL_MEDICINE) != null){
            localMedicine = intent.getSerializableExtra(LOCAL_MEDICINE) as LocalMedicine
            editTextNameMed.setText(localMedicine!!.name)
            editTextNameMed.setRawInputType(0)
            setAllEnable(false)
            reminderList = localMedicine!!.reminders
            editTextTotQuantity.setText(if(localMedicine!!.totalPills != 0F) localMedicine!!.totalPills.toString() else "0")
            editTextRemQuantity.setText(if(localMedicine!!.remainingPills != 0F) localMedicine!!.remainingPills.toString() else "0")
            buttonDenyMed.text = getText(R.string.closeButton)
            buttonConfirmMed.text = getText(R.string.editButton)
        } else if (intent.getSerializableExtra(REMOTE_MEDICINE) != null) {
            remoteMedicine = intent.getSerializableExtra(REMOTE_MEDICINE) as RemoteMedicine
            editTextNameMed.setText(remoteMedicine!!.name)
            editTextNameMed.setRawInputType(0)
            spinnerMedicineType.isEnabled = false
        }

        initSpinner()

        stdLayout = editTextNameMed.background
        buttonCamera.setOnClickListener {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(this, TextReaderActivity::class.java)
                startActivityForResult(intent, CAMERA_REQUEST)
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CAMERA_PERMISSION_ID
                )
            }
            //TODO credo sia da cancellare visto che già scritto prima uguale
            /*buttonCamera.setOnClickListener {
                val intent = Intent(this, TextReaderActivity::class.java)
                startActivityForResult(intent, CAMERA_REQUEST)
            }*/
        }

        buttonDenyMed.setOnClickListener {
            finish()
        }

        buttonAddReminder.setOnClickListener {
            val intent = Intent(this, ReminderActivity::class.java)
            val intentReturn = Intent()
            setResult(Activity.RESULT_OK, intentReturn)
            startActivityForResult(intent, REMINDER_INSERT_ACTIVITY)
        }

        buttonConfirmMed.setOnClickListener {
            restoreAllBg()

            if(localMedicine == null) {
                addOrEditMedicine()
            } else {
                if (isEditing)
                    addOrEditMedicine()
                else{
                    isEditing = !isEditing
                    setAllEnable(true)
                    initSpinner()
                    buttonDenyMed.text = "Annulla"
                    buttonConfirmMed.text = "Salva"
                }
            }
        }

    }

    private fun addOrEditMedicine(){
        if (checkValuesValidity()) {
            val result: Boolean
            val newMed = LocalMedicine(
                editTextNameMed.text.toString().toLowerCase(Locale.ROOT),
                getTypeFromText(spinnerMedicineType.selectedItem.toString()),
                editTextTotQuantity.text.toString().toFloat(),
                editTextRemQuantity.text.toString().toFloat(),
                reminderList,
                editTextNameMed.text.toString()
                    .toLowerCase(Locale.ROOT) + spinnerMedicineType.selectedItem.toString()
                    .toLowerCase(Locale.ROOT)
            )
            result = if(localMedicine == null) {
                UserInformation.addNewMedicine(newMed)
            } else {
                UserInformation.editMedicine(localMedicine!!.name,newMed)
            }

            if (result){
                LocalDatabase.saveMedicineList()
                finish()
            } else {
                Toast.makeText(
                    this,
                    "Attenzione, Medicina già presente!",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST -> {
                    val pillName: String = data!!.getStringExtra("pillName")
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
            ArrayAdapter(this, android.R.layout.simple_spinner_item, medTypeValues)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerMedicineType.adapter = arrayAdapter

        if(isEditing){
            spinnerMedicineType.setSelection(pos.toInt())
        }
    }

    private fun checkValuesValidity(): Boolean {
        var validity = true

        if (editTextNameMed.text.toString().toLowerCase(Locale.ROOT) == "") {
            validity = false
            Utils.colorEditText(editTextNameMed)
        }

        if (editTextTotQuantity.text.toString().toFloatOrNull() == null) {
            validity = false
            Utils.colorEditText(editTextTotQuantity)
        }

        if (editTextRemQuantity.text.toString().toFloatOrNull() == null) {
            validity = false
            Utils.colorEditText(editTextRemQuantity)
        }

        return validity
    }

    private fun restoreAllBg() {
        editTextNameMed.background = stdLayout
        editTextTotQuantity.background = stdLayout
        editTextRemQuantity.background = stdLayout
    }

    private fun setAllEnable(flag: Boolean){
        editTextNameMed.isEnabled = flag
        editTextTotQuantity.isEnabled = flag
        editTextRemQuantity.isEnabled = flag
        editTextNameMed.isEnabled = flag
        buttonAddReminder.isEnabled = flag
        buttonCamera.isEnabled = flag
        spinnerMedicineType.isEnabled = flag
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val intent = Intent(this, TextReaderActivity::class.java)
        startActivityForResult(intent, CAMERA_REQUEST)
    }


}
