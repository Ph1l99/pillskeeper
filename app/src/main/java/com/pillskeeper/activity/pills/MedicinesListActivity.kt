package com.pillskeeper.activity.pills

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.pillskeeper.R
import com.pillskeeper.activity.pills.PillsFormActivity.Companion.REMOTE_MEDICINE
import com.pillskeeper.data.RemoteMedicine
import com.pillskeeper.datamanager.DatabaseManager
import kotlinx.android.synthetic.main.activity_medicines_list.*

class MedicinesListActivity : AppCompatActivity() {
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var medicinesListView: ListView
    private lateinit var arrayMedicines: ArrayList<String>
    private lateinit var databaseReference: DatabaseReference

    companion object {
        private const val LAUNCH_PILLS = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicines_list)

        databaseReference = DatabaseManager.obtainRemoteDatabase()

        fillMedicinesList()

        addMedicineButton.setOnClickListener {
            val intent = Intent(this, PillsFormActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun fillMedicinesList() {
        Log.i(Log.DEBUG.toString(), "fillMedicinesList()-Started")
        databaseReference.child(DatabaseManager.PATH_MEDICINES)
            .addValueEventListener(object : ValueEventListener {

                override fun onCancelled(p0: DatabaseError) {
                    Log.i(
                        Log.DEBUG.toString(),
                        "getDataFromDB()-ERROR-FIREBASE: " + p0.message + " (CODE " + p0.code + ")"
                    )
                }

                override fun onDataChange(p0: DataSnapshot) {
                    displayListMedicines(RemoteMedicine.getMedicineListFromMap(p0.value as Map<String, Map<String, String>>))

                    Log.i(Log.DEBUG.toString(), "fillMedicinesList()-Ended")
                }
            })
        Log.i(Log.DEBUG.toString(), "fillMedicinesList()-Ended")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LAUNCH_PILLS) {
            if (resultCode == Activity.RESULT_OK) {
                finish()
            }
        }
    }

    //Metodo per manipolare i dati e presentarli all'utente
    private fun displayListMedicines(medicinesList: List<RemoteMedicine>) {
        Log.i(Log.DEBUG.toString(), "displayListMedicines()-Started")
        arrayMedicines = ArrayList(medicinesList.size)
        medicinesListView = findViewById(R.id.medicinesList)
        for (medicine in medicinesList) {
            arrayMedicines.add(medicine.name)
        }
        adapter = ArrayAdapter(
            this@MedicinesListActivity,
            android.R.layout.simple_list_item_1,
            arrayMedicines
        )
        medicinesListView.adapter = adapter
        medicinesListView.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, PillsFormActivity::class.java)
            intent.putExtra(REMOTE_MEDICINE, medicinesList[position])
            startActivityForResult(intent, LAUNCH_PILLS)
        }
        Log.i(Log.DEBUG.toString(), "displayListMedicines()-Ended")
    }
}
