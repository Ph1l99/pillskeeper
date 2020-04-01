package com.pillskeeper.notifier

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.pillskeeper.data.Appointment
import com.pillskeeper.data.ReminderMedicineSort
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.utility.Utils
import java.util.*


class WorkerStarter : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(Log.DEBUG.toString(), "ServiceStarter: onReceive() - Function started")

        if (intent != null)
            if(intent.action == Intent.ACTION_BOOT_COMPLETED || intent.getStringExtra(RESTART_ALARM_ACTION) != null){
                startNotifier(context)
                WorkerNotifier.showNotification("Test","Ripartenza nuovo ciclo")
                Log.i(Log.DEBUG.toString(), "ServiceStarter: onReceive() - Ripianifico Ciclo! ${Date()}")
            } else if (intent.getStringExtra(START_ALARM_ACTION) != null) {
                WorkerNotifier.context = context!!
                WorkerNotifier.showNotification("ciao","ciao")
                Log.i(Log.DEBUG.toString(), "ServiceStarter: onReceive() - Mostro una notifica ${Date()}")
            }

        Log.i(Log.DEBUG.toString(), "ServiceStarter: onReceive() - Function ended")
    }

    companion object {

        const val RESTART_ALARM_ACTION = "RestartAlarmAction"
        private const val START_ALARM_ACTION = "StartAlarmAction"

        fun startNotifier(context: Context?) {
            Log.i(Log.DEBUG.toString(), "MainActivity: startNotifierThread() - Started")

            if (context != null) {
                getDailyList().forEach { println(it.toString()) }
                /*
                if((context.getSystemService(Context.POWER_SERVICE) as PowerManager).isDeviceIdleMode)
                    Log.i(Log.DEBUG.toString(), "MainActivity: startNotifierThread() - ora attuale: ${Date()}")
                val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                val intent = Intent(context,WorkerStarter::class.java)
                    .apply {
                        putExtra(START_ALARM_ACTION,START_ALARM_ACTION)
                        action = Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
                    }

                val intentR = Intent(context,WorkerStarter::class.java)
                    .apply {
                        action = Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
                        putExtra(RESTART_ALARM_ACTION,RESTART_ALARM_ACTION)
                    }

                val pendingIntent: LinkedList<PendingIntent> = LinkedList()
                pendingIntent.add(PendingIntent.getBroadcast(context,((Date().time / 1000L) % Int.MAX_VALUE).toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT))
                pendingIntent.add(PendingIntent.getBroadcast(context,((Date().time / 999L) % Int.MAX_VALUE).toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT))
                pendingIntent.add(PendingIntent.getBroadcast(context,((Date().time / 998L) % Int.MAX_VALUE).toInt(), intentR, PendingIntent.FLAG_UPDATE_CURRENT))

                val triggerCal = Calendar.getInstance()
                triggerCal.time = Date()

                Log.i(Log.DEBUG.toString(), "MainActivity: startNotifierThread() - ora attuale: ${Date()}")
                for(int in pendingIntent) {
                    triggerCal.set(Calendar.MINUTE,triggerCal.get(Calendar.MINUTE) + 1)
                    if (Build.VERSION.SDK_INT >= 23) {
                        alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            triggerCal.timeInMillis,
                            int
                        )
                        Log.i(Log.DEBUG.toString(), "MainActivity: startNotifierThread() - SDK >= 23")
                    }
                    else
                        alarmManager.setExact(
                            AlarmManager.RTC_WAKEUP,
                            triggerCal.timeInMillis,
                            int
                        )
                    Log.i(Log.DEBUG.toString(), "MainActivity: startNotifierThread() - Timer " +
                            "settato alle ${triggerCal.time}")
                    triggerCal.set(Calendar.MINUTE,triggerCal.get(Calendar.MINUTE) + 14)
                }
                Log.i(Log.DEBUG.toString(), "MainActivity: startNotifierThread() - Timer Settati")
            }
            Log.i(Log.DEBUG.toString(), "MainActivity: startNotifierThread() - Ended")
            */
            }
        }


        private fun getDailyList(): LinkedList<Any> {
            val filterDate = Date(Utils.dataNormalizationLimit(Date()))
            val orderedReminderList: LinkedList<ReminderMedicineSort> =
                Utils.getSortedListReminders(filterDate)

            UserInformation.appointments.sortWith(compareBy { it.date })
            var appointmentListSorted = UserInformation.appointments
            appointmentListSorted =
                LinkedList(appointmentListSorted.filter { it.date <= filterDate })

            val mergedList = LinkedList<Any>()
            mergedList.addAll(appointmentListSorted)
            mergedList.addAll(orderedReminderList)
            mergedList.sortWith(compareBy {
                when (it) {
                    is Appointment -> {
                        it.date
                    }
                    is ReminderMedicineSort -> {
                        val cal = Calendar.getInstance()
                        cal.time = it.reminder.startingDay
                        cal.set(Calendar.HOUR_OF_DAY, it.reminder.hours)
                        cal.set(Calendar.MINUTE, it.reminder.minutes)
                        cal.time
                    }
                    else -> {
                        null
                    }
                }
            })

            return mergedList
        }

    }
}