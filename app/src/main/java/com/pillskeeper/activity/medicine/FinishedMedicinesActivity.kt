package com.pillskeeper.activity.medicine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.pillskeeper.R
import com.pillskeeper.data.LocalMedicine
import com.pillskeeper.enums.MedicineTypeEnum
import com.pillskeeper.utility.MedCardAdapter
import kotlinx.android.synthetic.main.activity_finished_medicines.*
import java.util.*

class FinishedMedicinesActivity : AppCompatActivity() {

    companion object {
        const val MINIMUM_PILLS = 4
    }

    private lateinit var mAdapter: MedCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finished_medicines)
        checkAlmostFinishedMedicines()
    }


    private fun checkAlmostFinishedMedicines() {
        //val list = LocalDatabase.readMedicineList()
        /*val outputList = LinkedList(list.filter {
            it.remainingPills <= MINIMUM_PILLS
        })*/
        var outputList: LinkedList<LocalMedicine> = LinkedList()
        outputList.add(LocalMedicine("Ciao", MedicineTypeEnum.PILLS, 20f, 20f, null, "1234"))
        outputList.add(
            LocalMedicine(
                "Bella la Menta",
                MedicineTypeEnum.PILLS,
                20f,
                20f,
                null,
                "1256"
            )
        )

        if (outputList.isEmpty()) {
            //TODO fare qualcosa
        } else {
            displayMedicines(outputList)
        }
    }

    private fun displayMedicines(list: LinkedList<LocalMedicine>) {
        mAdapter = MedCardAdapter(list)
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        mAdapter.setOnItemClickListener {
            Toast.makeText(this, "Hai premuto la posizione+$it", Toast.LENGTH_LONG).show()
        }
    }
}
