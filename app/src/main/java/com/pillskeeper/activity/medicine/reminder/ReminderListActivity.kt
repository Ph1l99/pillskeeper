package com.pillskeeper.activity.medicine.reminder

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.pillskeeper.R
import com.pillskeeper.data.LocalMedicine
import com.pillskeeper.data.ReminderMedicine
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.interfaces.Callback
import com.pillskeeper.utility.Utils
import com.pillskeeper.utility.adapter.ReminderAdapter
import kotlinx.android.synthetic.main.activity_reminder_list.*

class ReminderListActivity : AppCompatActivity() {

    private lateinit var rAdapter: ReminderAdapter
    private var medicineName: String? = ""
    private var medicine: LocalMedicine? = null

    companion object {
        const val MEDICINE_NAME = "medicineName"
        const val REMINDER_MEDICINE = "reminderMedicine"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder_list)

        medicineName = intent.getStringExtra(MEDICINE_NAME)

        addReminderFAB.setOnClickListener {
            ReminderChooseDialog(this, null, medicineName).show()
        }
    }


    override fun onResume() {
        super.onResume()

        if (medicineName.isNullOrEmpty()) {
            Utils.buildAlertDialog(this,
                getString(R.string.oops),
                getString(R.string.message_title),
                object : Callback {
                    override fun onSuccess(res: Boolean) {
                        finish()
                    }

                    override fun onError() {}
                }
            ).show()
        } else {
            medicine = UserInformation.getSpecificMedicine(medicineName!!)

            if (medicine?.reminders != null)
                displayListReminders(medicine?.reminders!!)
        }
    }

    private fun displayListReminders(reminderList: List<ReminderMedicine>) {
        rAdapter = ReminderAdapter(reminderList)
        recyclerView.adapter = rAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        rAdapter.onItemClick = { reminder ->
            val intent = Intent(this, EditReminderActivity::class.java)
                .apply {
                    putExtra(REMINDER_MEDICINE, reminder)
                    putExtra(MEDICINE_NAME, medicineName)
                }
            startActivity(intent)
        }
    }


}
