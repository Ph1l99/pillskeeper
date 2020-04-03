package com.pillskeeper.notifier

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.pillskeeper.data.ReminderMedicineSort
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.enums.TypeIntentWorker
import com.pillskeeper.utility.Utils
import java.util.*


class EventBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val TYPE_INTENT = "TypeIntent"
        const val VALUE_INTENT = "ValueIntent"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(Log.DEBUG.toString(), "ServiceStarter: onReceive() - Function started")
        if (intent != null) {
            if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
                NotifyPlanner.planAlarmDay(context)
                planNextDayPlanner(context)
            } else if (intent.getStringExtra(TYPE_INTENT) != null) {
                if (intent.getStringExtra(TYPE_INTENT) == TypeIntentWorker.SHOW_NOTIFY.toString()) {
                    val item = Utils.deserialize(intent.getByteArrayExtra(VALUE_INTENT))
                    if (context != null) {
                        if (item is ReminderMedicineSort) {
                            val medicine = UserInformation.subMedicineQuantity(
                                item.medName,
                                item.reminder.dosage
                            )
                            if (medicine != null && medicine.remainingQty < 5F)
                                NotificationBuilder.showNotificationLowQuantity(context, medicine)
                        }
                        NotificationBuilder.showNotificationReminder(context, item)
                    }
                } else {
                    NotifyPlanner.planAlarmDay(context)
                    planNextDayPlanner(context)
                }
            }
            println(println(intent.extras.toString()))
        }
        Log.i(Log.DEBUG.toString(), "ServiceStarter: onReceive() - Function ended")
    }


    private fun planNextDayPlanner(context: Context?) {
        Log.i(Log.DEBUG.toString(), "MainActivity: planNextDayPlanner() - Started")

        if (context != null) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context,EventBroadcastReceiver::class.java)
                .apply {
                    putExtra(TYPE_INTENT,TypeIntentWorker.PLAN_DAY_ALARM.toString())
                    action = Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
                }
            val itID = 0
            if(!Utils.isAlreadyExistingIntent(context,itID,intent)) {
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

                val cal = Calendar.getInstance()
                cal.time = Date()
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)
                cal.set(Calendar.HOUR_OF_DAY, 7)
                cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1)

                if (Build.VERSION.SDK_INT >= 23)
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        cal.timeInMillis,
                        pendingIntent
                    )
                else
                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        cal.timeInMillis,
                        pendingIntent
                    )
                Log.i(
                    Log.DEBUG.toString(),
                    "MainActivity: planNextDayPlanner() - Next Day planned... ${cal.time}"
                )
            } else {
                Log.i(
                    Log.DEBUG.toString(),
                    "MainActivity: planNextDayPlanner() - Next already planned!!!"
                )
            }
        }
        Log.i(Log.DEBUG.toString(), "MainActivity: planNextDayPlanner() - Ended")
    }

}
