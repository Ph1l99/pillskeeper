package com.pillskeeper.notifier

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.pillskeeper.activity.medicine.FinishedMedicinesActivity
import com.pillskeeper.data.ReminderMedicineSort
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.enums.TypeIntentWorker
import com.pillskeeper.utility.Utils


class EventBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val TYPE_INTENT = "TypeIntent"
        const val VALUE_INTENT = "ValueIntent"
        private const val TAG = "BROADCAST_RECEIVER"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(TAG, "EventBroadcastReceiver: onReceive() - Function started")
        try {
            if (intent != null) {
                if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
                    NotifyPlanner.planFullDayAlarms(context)
                    NotifyPlanner.planNextDayPlanner(context)
                } else if (intent.getStringExtra(TYPE_INTENT) != null) {
                    if (intent.getStringExtra(TYPE_INTENT) == TypeIntentWorker.SHOW_NOTIFY.toString())
                        eventShowNotify(context, intent)
                    else if (intent.getStringExtra(TYPE_INTENT) == TypeIntentWorker.SHOW_NOTIFY_DEBUG.toString()) {
                        NotificationBuilder.showNotificationDebug(context!!)
                        NotifyPlanner.testPlanner(
                            context,
                            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                        )
                    } else {
                        NotifyPlanner.planFullDayAlarms(context)
                        NotifyPlanner.planNextDayPlanner(context)
                    }
                }
            }
        }catch (e: Exception){
            Log.i(TAG, "ServiceStarter: onReceive() - Function ended " + e.printStackTrace())
        }
        Log.i(TAG, "ServiceStarter: onReceive() - Function ended")
    }

    private fun eventShowNotify(context: Context?, intent: Intent){
        val item = Utils.deserialize(intent.getByteArrayExtra(VALUE_INTENT)!!)
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
