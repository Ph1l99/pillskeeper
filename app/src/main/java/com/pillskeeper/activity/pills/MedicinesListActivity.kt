package com.pillskeeper.activity.pills

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.pillskeeper.R
import com.pillskeeper.data.RemoteMedicine
import com.pillskeeper.datamanager.DatabaseManager
import com.pillskeeper.enums.MedicineTypeEnum
import kotlinx.android.synthetic.main.activity_medicines_list.*

class MedicinesListActivity : AppCompatActivity() {
    private lateinit var medicinesListView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var listMedicines: List<RemoteMedicine>
    private lateinit var arrayMedicines: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicines_list)

        fillMedicinesList()

        addMedicineButton.setOnClickListener {
            val intent = Intent(this, PillsFormActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fillMedicinesList() {
        Log.i(Log.DEBUG.toString(), "fillMedicinesList()-Started")

        arrayMedicines = ArrayList(listMedicines.size)
        if (listMedicines != null) {
            for (medicine in listMedicines) {
                arrayMedicines.add(medicine.name)
            }
            adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayMedicines)
            medicinesListView.adapter = adapter
        } else {
            //TODO cliccare il bottone
        }
        Log.i(Log.DEBUG.toString(), "fillMedicinesList()-Ended")
    }
}
