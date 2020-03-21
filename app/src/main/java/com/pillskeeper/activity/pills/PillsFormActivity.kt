package com.pillskeeper.activity.pills


import android.app.Activity
import android.content.Intent
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
    }

    private var reminderList: LinkedList<ReminderMedicine>? = null
    private lateinit var stdLayout: Drawable
    private var medicineClicked: RemoteMedicine? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pills_form)

        /*Se ho premuto il bottone per l'aggiunta di una medicina vado avanti con l'activity,
         *altrimenti ottengo la medicina remota che mi hanno passato nell'activity precedente
         */

        if (!intent.hasExtra("BUTTON_PRESSED")) {
            checkInput()
        }

        stdLayout = editTextNameMed.background
        buttonCamera.setOnClickListener {
            val intent = Intent(this, TextReaderActivity::class.java)
            startActivityForResult(intent, CAMERA_REQUEST)
        }

        initSpinner()



        buttonDenyMed.setOnClickListener {
            finish()
        }

        buttonAddReminder.setOnClickListener {
            val intent = Intent(this, ReminderActivity::class.java)
            startActivityForResult(intent, REMINDER_INSERT_ACTIVITY)
        }

        buttonConfirmMed.setOnClickListener {
            restoreAllBg()
            if (checkValuesValidity()) {
                if (UserInformation.addNewMedicine(
                        LocalMedicine(
                            editTextNameMed.text.toString().toLowerCase(Locale.ROOT),
                            MedicineTypeEnum.valueOf(spinnerMedicineType.selectedItem.toString()),
                            editTextTotQuantity.text.toString().toFloat(),
                            editTextRemQuantity.text.toString().toFloat(),
                            reminderList,
                            editTextNameMed.text.toString()
                                .toLowerCase(Locale.ROOT) + spinnerMedicineType.selectedItem.toString()
                                .toLowerCase(Locale.ROOT)
                        )
                    )
                ) {
                    LocalDatabase.saveMedicineList()
                    finish()
                } else {
                    Toast.makeText(this, "Attenzione, Medicina già presente!", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST -> {
                    val pillName: String = data!!.getStringExtra("pillName")
                    //edit_Text_Name.text = SpannableStringBuilder("")
                    editTextNameMed.text = SpannableStringBuilder(pillName)
                }
                REMINDER_INSERT_ACTIVITY -> {
                    reminderList //todo get list from data!!.get....()
                }
                else -> super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun initSpinner() {
        val medTypeValues: ArrayList<String> = ArrayList()
        MedicineTypeEnum.values()
            .forEach { medTypeEnum -> medTypeValues.add(medTypeEnum.toString()) }

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, medTypeValues)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerMedicineType.adapter = arrayAdapter
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

    private fun checkInput() {
        //Ottengo medicina remota da activity precedente
        medicineClicked = intent.extras?.getSerializable("REMOTE_MEDICINE") as RemoteMedicine
        if (medicineClicked != null) {
            editTextNameMed.setText(medicineClicked!!.name)
            editTextNameMed.setRawInputType(0)
        }
    }

}