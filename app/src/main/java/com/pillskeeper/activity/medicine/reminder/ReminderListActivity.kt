package com.pillskeeper.activity.medicine.reminder

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.pillskeeper.R
import com.pillskeeper.data.LocalMedicine
import com.pillskeeper.data.ReminderMedicine
import com.pillskeeper.utility.adapter.ReminderCardAdapter
import kotlinx.android.synthetic.main.activity_reminder_list.*

class ReminderListActivity : AppCompatActivity() {

    private lateinit var mAdapter: ReminderCardAdapter
    private lateinit var medicine: LocalMedicine

    companion object {
        const val MEDICINE_NAME = "medicineName"
        const val REMINDER_ID = 1
        const val REMINDER_MEDICINE = "reminderMedicine"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder_list)

        medicine = intent.getSerializableExtra(REMINDER_MEDICINE) as LocalMedicine

        addReminderFAB.setOnClickListener {

            //ReminderChooseDialog(this).show() todo, come importare i fragment... a questo punto apri uno dei fragment...

        }
    }

    override fun onResume() {
        super.onResume()
        if(medicine.reminders != null)
            displayListMedicines(medicine.reminders!!)
    }

    private fun displayListMedicines(reminderList: List<ReminderMedicine>) {
        mAdapter = ReminderCardAdapter(reminderList)
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        mAdapter.setOnItemClickListener {
            val intent = Intent(this, EditReminderActivity::class.java)
                .apply {
                    putExtra(REMINDER_MEDICINE, reminderList[it])
                    putExtra(MEDICINE_NAME, medicine.name)
                }
            startActivity(intent)
        }
    }


}
