package com.pillskeeper.notifier

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import java.util.*


class WorkerStarter : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(Log.DEBUG.toString(), "ServiceStarter: onReceive() - Function started")

        if (intent != null)
            if(intent.action == RESTART_ALARM_ACTION || intent.action == Intent.ACTION_BOOT_COMPLETED){
                startNotifier(context)
                WorkerNotifier.showNotification("Test","Ripartenza nuovo ciclo")
            }
            else if (intent.action == START_ALARM_ACTION) {
                WorkerNotifier.context = context!!
                WorkerNotifier.showNotification("ciao","ciao")
            }

        Log.i(Log.DEBUG.toString(), "ServiceStarter: onReceive() - Service started")
    }

    companion object {

        const val RESTART_ALARM_ACTION = "RestartAlarmAction"
        private const val START_ALARM_ACTION = "StartAlarmAction"

        fun startNotifier(context: Context?) {
            Log.i(Log.DEBUG.toString(), "MainActivity: startNotifierThread() - Started")

            if (context != null) {

                val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                val intent = Intent(context,WorkerStarter::class.java)
                intent.action = START_ALARM_ACTION

                val intentR = Intent(context,WorkerStarter::class.java)
                intentR.action = RESTART_ALARM_ACTION

                val pendingIntent: LinkedList<PendingIntent> = LinkedList()
                pendingIntent.add(PendingIntent.getBroadcast(context,1, intent, PendingIntent.FLAG_UPDATE_CURRENT))
                pendingIntent.add(PendingIntent.getBroadcast(context,2, intent, PendingIntent.FLAG_UPDATE_CURRENT))
                pendingIntent.add(PendingIntent.getBroadcast(context,3, intentR, PendingIntent.FLAG_UPDATE_CURRENT))

                val triggerCal = Calendar.getInstance()
                triggerCal.time = Date()

                Log.i(Log.DEBUG.toString(), "MainActivity: startNotifierThread() - ora attuale: ${Date()}")
                for(int in pendingIntent) {
                    triggerCal.set(Calendar.MINUTE,triggerCal.get(Calendar.MINUTE) + 5)
                    if (Build.VERSION.SDK_INT >= 23)
                        alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            triggerCal.timeInMillis,
                            int
                        )
                    else
                        alarmManager.setExact(
                            AlarmManager.RTC_WAKEUP,
                            triggerCal.timeInMillis,
                            int
                        )
                    Log.i(Log.DEBUG.toString(), "MainActivity: startNotifierThread() - Timer " +
                            "settato alle ${triggerCal.time}")
                }
                Log.i(Log.DEBUG.toString(), "MainActivity: startNotifierThread() - Timer Settati")
            }
            Log.i(Log.DEBUG.toString(), "MainActivity: startNotifierThread() - Ended")
        }
    }
}
/*val intent = Intent(context, ServiceNotifier::class.java)
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
    context!!.startForegroundService(intent)
else
    context!!.startService(intent)
*/
/*private fun isJobServiceOn(context: Context?): Boolean {
            Log.i(Log.DEBUG.toString(), "MainActivity: isJobServiceOn() - Started")
            if (context != null) {
                (context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler)
                    .allPendingJobs.forEach { entry ->
                        if (entry.id == WorkerNotifier.JOB_NOTIFIER_ID) {
                            Log.i(
                                Log.DEBUG.toString(),
                                "MainActivity: isJobServiceOn() - Ended, service running"
                            )
                            return true
                        }
                    }
            }

            Log.i(
                Log.DEBUG.toString(),
                "MainActivity: isJobServiceOn() - Ended, no service running"
            )
            return false
        }*/