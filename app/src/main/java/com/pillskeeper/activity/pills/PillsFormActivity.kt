package com.pillskeeper.activity.pills


import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.widget.ArrayAdapter
import com.pillskeeper.R
import com.pillskeeper.activity.MainActivity
import com.pillskeeper.data.LocalMedicine
import com.pillskeeper.datamanager.LocalDatabase
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.enums.MedicineTypeEnum
import kotlinx.android.synthetic.main.activity_new_friend.*
import kotlinx.android.synthetic.main.activity_pills_form.*
import java.util.*
import kotlin.collections.ArrayList

class PillsFormActivity : AppCompatActivity() {

    companion object {
        const val CAMERA_REQUEST = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pills_form)
        //TODO finire il form --- Manca bottone di aggiunta reminder in attivity a parte

        buttonCamera.setOnClickListener{
            val intent = Intent(this, TextReaderActivity::class.java)
            startActivityForResult(intent, CAMERA_REQUEST)
        }

        initSpinner()

        buttonDenyMed.setOnClickListener {
            finish()
        }

        buttonConfirmMed.setOnClickListener{

            if(checkValuesValidity()) {
                val intent = Intent(this, PillsListActivity::class.java)
                UserInformation.addNewMedicine(
                    LocalMedicine(
                        editTextNameMed.text.toString().toLowerCase(Locale.ROOT),
                        MedicineTypeEnum.valueOf(spinnerMedicineType.selectedItem.toString().toLowerCase(Locale.ROOT)),
                        editTextTotQuantity.text.toString().toFloat(),
                        editTextRemQuantity.text.toString().toFloat(),
                        null, //TODO aggiungere reminder list se inserita da utente
                        editTextName.text.toString().toLowerCase(Locale.ROOT) + spinnerMedicineType.selectedItem.toString().toLowerCase(Locale.ROOT)
                    )
                )
                LocalDatabase.saveMedicineList()
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val pillName: String = data!!.getStringExtra("pillName")
                //edit_Text_Name.text = SpannableStringBuilder("")
                editTextName.text = SpannableStringBuilder(pillName)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun initSpinner(){
        val medTypeValues : ArrayList<String> = ArrayList()
        MedicineTypeEnum.values().forEach { medTypeEnum -> medTypeValues.add(medTypeEnum.toString()) }

        val arrayAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item, medTypeValues)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerMedicineType.adapter = arrayAdapter
    }

    private fun checkValuesValidity(): Boolean{

        return true
        TODO("creare i check del caso")
    }

}