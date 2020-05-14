package com.pillskeeper.notifier

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.pillskeeper.activity.medicine.FinishedMedicinesActivity
import com.pillskeeper.data.ReminderMedicineSort
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.enums.TypeIntentWorker
import com.pillskeeper.utility.Utils


/**
 * Class receiver used to manage all events (alerts) generated from Android system
 */
class EventBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val TYPE_INTENT = "TypeIntent"
        const val VALUE_INTENT = "ValueIntent"
        private const val TAG = "BROADCAST_RECEIVER: "
    }

    /**
     * function receiver that manage different use cases
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(TAG, "onReceive() - Function started")
            if (intent != null) {

                if (intent.action == Intent.ACTION_BOOT_COMPLETED || intent.action == NotifyPlanner.ACTION_NEXT_DAY_PLANNER) {
                    UserInformation.cleanAppointmentAndReminderList()
                    NotifyPlanner.planFullDayAlarms(context)
                    NotifyPlanner.planNextDayPlanner(context)
                } else if (intent.getStringExtra(TYPE_INTENT) != null) {
                    if (intent.getStringExtra(TYPE_INTENT) == TypeIntentWorker.SHOW_NOTIFY.toString())
                        eventShowNotify(context, intent)
                    else {
                        NotifyPlanner.planFullDayAlarms(context)
                        NotifyPlanner.planNextDayPlanner(context)
                    }
                }
            }
        Log.i(TAG, "onReceive() - Function ended")
    }

    /**
     * Function which show notification and decrease medicine current dosage
     */
    private fun eventShowNotify(context: Context?, intent: Intent){
        val valueIntent = intent.getByteArrayExtra(VALUE_INTENT)
        if(valueIntent != null) {
            val item = Utils.deserialize(valueIntent)
            if (context != null) {
                if (item is ReminderMedicineSort) {

                    val medicine = UserInformation.subMedicineQuantity(
                        item.medName,
                        item.reminder.dosage
                    )

                    if (medicine != null) {
                        if (medicine.remainingQty <= medicine.totalQty * FinishedMedicinesActivity.MINIMUM_QTY) {
                            NotificationBuilder.showNotificationLowQuantity(context, medicine)
                        }
                        if (medicine.remainingQty == 0F)
                            UserInformation.restoreQty(medicine)
                    }
                }
                NotificationBuilder.showNotificationReminder(context, item)
            }
        }
    }

}
