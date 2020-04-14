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
import com.pillskeeper.utility.adapter.ReminderCardAdapter
import kotlinx.android.synthetic.main.activity_reminder_list.*

class ReminderListActivity : AppCompatActivity() {

    private lateinit var mAdapter: ReminderCardAdapter
    private          var medicinePosition: Int = -1
    private lateinit var medicine: LocalMedicine

    companion object {
        const val MEDICINE_NAME = "medicineName"
        const val REMINDER_ID = 1
        const val MEDICINE_POSITION = "medicinePosition"
        const val REMINDER_MEDICINE = "reminderMedicine"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder_list)

        medicinePosition = intent.getIntExtra(MEDICINE_POSITION,-1)

        addReminderFAB.setOnClickListener {
            ReminderChooseDialog(this,null,UserInformation.medicines[medicinePosition].name).show()
        }
    }

    override fun onResume() {
        super.onResume()

        if(medicinePosition == -1){
            Utils.buildAlertDialog(this,
                "Attenzione un errore è avvenuto durante la visualizzazione dei promemoria",
                getString(R.string.message_title),
                object: Callback {
                    override fun onSuccess(res: Boolean) { finish() }
                    override fun onError() {}
                }
            )
        } else {
            medicine = UserInformation.medicines[medicinePosition]

            if (medicine.reminders != null)
                displayListReminders(medicine.reminders!!)
        }
    }

    private fun displayListReminders(reminderList: List<ReminderMedicine>) {
        mAdapter = ReminderCardAdapter(reminderList)
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        mAdapter.setOnItemClickListener {position ->
            val intent = Intent(this, EditReminderActivity::class.java)
                .apply {
                    putExtra(REMINDER_MEDICINE, reminderList[position])
                    putExtra(MEDICINE_NAME, medicine.name)
                }
            startActivity(intent)
        }
    }


}
