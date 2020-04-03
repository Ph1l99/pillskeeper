package com.pillskeeper.activity.medicine.formfragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.common.hash.Hashing
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

import com.pillskeeper.R
import com.pillskeeper.activity.medicine.PillsListActivity
import com.pillskeeper.data.LocalMedicine
import com.pillskeeper.data.RemoteMedicine
import com.pillskeeper.datamanager.LocalDatabase
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.enums.MedicineTypeEnum
import java.nio.charset.StandardCharsets
import java.text.Normalizer
import java.util.*

class FormThreeFragment(viewPager: PillsViewPager) : Fragment() {
    companion object {
        const val FORM_TWO = 1
    }

    private val viewPager = viewPager
    lateinit var textViewBack: TextView
    lateinit var textViewConfirm: TextView

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
            viewPager.setCurrentItem(FORM_TWO)
        }

        textViewConfirm.setOnClickListener {
            addOrEditMedicine()
            var intent = Intent(context, PillsListActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun addOrEditMedicine() {
        val result: Boolean
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
        LocalDatabase.saveMedicineList()
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



