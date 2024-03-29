package com.pillskeeper.activity.medicine

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.pillskeeper.R
import com.pillskeeper.activity.medicine.MedicineFormActivity.Companion.REMOTE_MEDICINE
import com.pillskeeper.data.RemoteMedicine
import com.pillskeeper.datamanager.FirebaseDatabaseManager
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.interfaces.FirebaseMedicineCallback
import com.pillskeeper.utility.adapter.RemoteMedicineAdapter
import kotlinx.android.synthetic.main.activity_medicines_list.*

class MedicinesRemoteListActivity : AppCompatActivity() {
    private lateinit var adapter: RemoteMedicineAdapter

    companion object {
        private const val LAUNCH_PILLS = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicines_list)

        fillMedicinesList()

        addMedicineButton.setOnClickListener {
            startActivity(Intent(this, MedicineFormActivity::class.java))
            finish()
        }
    }

    private fun fillMedicinesList() {
        FirebaseDatabaseManager.getMedicines(object : FirebaseMedicineCallback {
            override fun onCallback(list: List<RemoteMedicine>?) {
                if (list != null) {
                    progressBarRemoteMed.visibility = View.GONE
                    displayListMedicines(list)
                } else {
                    progressBarRemoteMed.visibility = View.GONE
                    startActivity(Intent(UserInformation.context, MedicineFormActivity::class.java))
                    finish()
                }

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
        adapter = RemoteMedicineAdapter(medicinesList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        adapter.onItemClick = {
            val intent = Intent(this, MedicineFormActivity::class.java)
            intent.putExtra(REMOTE_MEDICINE, it)
            startActivityForResult(intent, LAUNCH_PILLS)
        }
    }
}
