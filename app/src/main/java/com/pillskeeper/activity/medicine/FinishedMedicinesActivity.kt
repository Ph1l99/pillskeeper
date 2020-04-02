package com.pillskeeper.activity.medicine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import com.pillskeeper.R
import com.pillskeeper.data.LocalMedicine
import com.pillskeeper.datamanager.LocalDatabase
import kotlinx.android.synthetic.main.activity_finished_medicines.*
import java.util.*

class FinishedMedicinesActivity : AppCompatActivity() {

    companion object {
        const val MINIMUM_PILLS = 4
    }

    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var medicinesListView: ListView
    private lateinit var arrayMedicines: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finished_medicines)
        //checkAlmostFinishedMedicines()
    }

    /*
    private fun checkAlmostFinishedMedicines(): LinkedList<LocalMedicine> {
        val list = LocalDatabase.readMedicineList()
        val outputList = LinkedList(list.filter {
            it.remainingPills <= MINIMUM_PILLS
        })
        if (outputList.isEmpty()) {
            medicinesListView = findViewById(R.id.finishedMedicinesList)
            medicinesListView.emptyView = findViewById(R.id.nothing_box)
            nothing_box.text = R.string.nothingToDoHere.toString()
        } else {
            displayMedicines(outputList)
        }
        return outputList
    }

    private fun displayMedicines(list: LinkedList<LocalMedicine>) {
        arrayMedicines = ArrayList(list.size)
        medicinesListView = findViewById(R.id.finishedMedicinesList)
        for (medicine in list) {
            arrayMedicines.add(medicine.name)
        }
        adapter = ArrayAdapter(
            this@FinishedMedicinesActivity,
            android.R.layout.simple_list_item_1,
            arrayMedicines
        )
        medicinesListView.adapter = adapter
    }*/
}
