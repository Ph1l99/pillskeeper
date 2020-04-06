package com.pillskeeper.activity.medicine.medicineformfragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.common.hash.Hashing
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pillskeeper.R
import com.pillskeeper.activity.medicine.MedicineLocaleListActivity
import com.pillskeeper.data.LocalMedicine
import com.pillskeeper.data.RemoteMedicine
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.enums.MedicineTypeEnum
import java.nio.charset.StandardCharsets
import java.util.*

class FormThreeFragment(private val viewPager: PillsViewPager) : Fragment() {

    private lateinit var textViewBack: TextView
    private lateinit var textViewConfirm: TextView

    private val PATH_MEDICINES = "medicines"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_form_three, container, false)

        textViewBack = view.findViewById(R.id.textViewBack)
        textViewConfirm = view.findViewById(R.id.textViewConfirm)

        textViewBack.setOnClickListener {
            viewPager.currentItem = FormAdapter.FORM_TWO
        }

        textViewConfirm.setOnClickListener {
            addOrEditMedicine()
            val intent = Intent(context, MedicineLocaleListActivity::class.java)
            startActivity(intent)
            FormAdapter.closeForm()
        }

        /*
        buttonAddReminder.setOnClickListener {
            val intent = Intent(this, ReminderActivity::class.java)
            val intentReturn = Intent()
            setResult(Activity.RESULT_OK, intentReturn)
            startActivityForResult(intent, REMINDER_INSERT_ACTIVITY)
        }
        */

        return view
    }

    //todo vedere se mettere nell'adapter
    private fun addOrEditMedicine() {
        val newMed = LocalMedicine(
            FormAdapter.pillName.toLowerCase(Locale.ROOT),
            FormAdapter.medicineType,
            FormAdapter.totalQuantity,
            FormAdapter.remainingQuantity,
            null,
            hashValue(FormAdapter.pillName, FormAdapter.medicineType)
        )
        UserInformation.addNewMedicine(newMed)

        writeMedOnDB(
            RemoteMedicine(
                FormAdapter.pillName,
                hashValue(FormAdapter.pillName, FormAdapter.medicineType),
                FormAdapter.medicineType
            )
        )
        //TODO gestire l'inserimento di reminder e di ripianificazione alarm
    }


    private fun hashValue(name: String, typeEnum: MedicineTypeEnum): String {
        return (Hashing.goodFastHash(64)).newHasher()
            .putString(name + typeEnum, StandardCharsets.UTF_8).hash().toString()
    }

    private fun writeMedOnDB(remoteMedicine: RemoteMedicine) {
        val databaseReference = Firebase.database.reference
        databaseReference.child(PATH_MEDICINES).child(remoteMedicine.id)
            .setValue(remoteMedicine)
    }
}



