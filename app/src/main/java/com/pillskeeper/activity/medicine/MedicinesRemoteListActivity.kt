package com.pillskeeper.activity.medicine

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pillskeeper.R
import com.pillskeeper.activity.medicine.MedicineFormActivity.Companion.REMOTE_MEDICINE
import com.pillskeeper.data.RemoteMedicine
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.utility.adapter.MedicineRemoteCardAdapter
import kotlinx.android.synthetic.main.activity_medicines_list.*

class MedicinesRemoteListActivity : AppCompatActivity() {
    private lateinit var mAdapter: MedicineRemoteCardAdapter
    private lateinit var databaseReference: DatabaseReference

    companion object {
        private const val LAUNCH_PILLS = 1
        private const val PATH_MEDICINES = "medicines"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicines_list)

        databaseReference = Firebase.database.reference

        fillMedicinesList()

        addMedicineButton.setOnClickListener {
            startActivity(Intent(this, MedicineFormActivity::class.java))
            finish()
        }
    }

    private fun fillMedicinesList() {
        Log.i(Log.DEBUG.toString(), "fillMedicinesList()-Started")
        databaseReference.child(PATH_MEDICINES)
            .addValueEventListener(object : ValueEventListener {

                override fun onCancelled(p0: DatabaseError) {
                    Log.i(
                        Log.ERROR.toString(),
                        "getDataFromDB()-ERROR-FIREBASE: " + p0.message + " (CODE " + p0.code + ")"
                    )
                    progressBarRemoteMed.visibility = View.GONE
                    startActivity(Intent(UserInformation.context, MedicineFormActivity::class.java))
                    finish()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    progressBarRemoteMed.visibility = View.GONE
                    @Suppress("UNCHECKED_CAST")
                    displayListMedicines(RemoteMedicine.getMedicineListFromMap(p0.value as Map<String, Map<String, String>>))
                    Log.i(Log.DEBUG.toString(), "fillMedicinesList()-Ended")
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LAUNCH_PILLS) {
            if (resultCode == Activity.RESULT_OK) {
                finish()
            }
        }
    }

    private fun displayListMedicines(medicinesList: List<RemoteMedicine>) {
        mAdapter = MedicineRemoteCardAdapter(
            medicinesList
        )
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        mAdapter.setOnItemClickListener {
            val intent = Intent(this, MedicineFormActivity::class.java)
            intent.putExtra(REMOTE_MEDICINE, medicinesList[it])
            startActivityForResult(intent, LAUNCH_PILLS)
        }
    }
}
