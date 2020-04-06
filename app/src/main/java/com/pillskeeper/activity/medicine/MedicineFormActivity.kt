package com.pillskeeper.activity.medicine


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pillskeeper.R
import com.pillskeeper.activity.medicine.medicineformfragments.FormAdapter
import com.pillskeeper.activity.medicine.medicineformfragments.PillsViewPager
import com.pillskeeper.data.LocalMedicine
import com.pillskeeper.data.RemoteMedicine
import kotlinx.android.synthetic.main.activity_pills_form.*

class MedicineFormActivity : AppCompatActivity() {

    companion object {
        const val CAMERA_REQUEST = 0
        const val REMINDER_INSERT_ACTIVITY = 1
        const val REMOTE_MEDICINE = "remoteMedicine"
        const val LOCAL_MEDICINE = "localMedicine"
        const val REMINDER = "reminder"
        const val REQUEST_CAMERA_PERMISSION_ID = 1
        const val VIEW_PAGER_ID = 2020
    }

    lateinit var viewPager: PillsViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pills_form)

        viewPager = PillsViewPager(this)
        viewPager.id = VIEW_PAGER_ID
        relativeLayout.addView(viewPager)
        val introAdapter = FormAdapter(supportFragmentManager, intent, viewPager)
        viewPager.adapter = introAdapter

        FormAdapter.formActivity = this
        if(intent.getSerializableExtra(LOCAL_MEDICINE) != null){
            FormAdapter.localMedicine = intent.getSerializableExtra(LOCAL_MEDICINE) as LocalMedicine
            FormAdapter.reminderList = FormAdapter.localMedicine!!.reminders
            viewPager.currentItem = FormAdapter.FORM_EDIT
        } else if (intent.getSerializableExtra(REMOTE_MEDICINE) != null) {
            FormAdapter.remoteMedicine = intent.getSerializableExtra(REMOTE_MEDICINE) as RemoteMedicine
        }
    }

    /*
    private fun addOrEditMedicine(){
        //TODO remote medicine
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

    private fun restoreAllBg() {
        editTextNameMed.background = stdLayout
        editTextTotQuantity.background = stdLayout
        editTextRemQuantity.background = stdLayout
    }

     */
}
