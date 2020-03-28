package com.pillskeeper.notifier

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit


class WorkerStarter : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(Log.DEBUG.toString(), "ServiceStarter: onReceive() - Function started")

        if (intent != null)
            if(intent.action == Intent.ACTION_BOOT_COMPLETED)
                startNotifierThread(context)

        Log.i(Log.DEBUG.toString(), "ServiceStarter: onReceive() - Service started")
    }

    companion object {
        fun startNotifierThread(context: Context?) {
            Log.i(Log.DEBUG.toString(), "MainActivity: startNotifierThread() - Started")

            if (context != null) {
                /*if (!isJobServiceOn(context)) {
                val mComponentName = context?.let {
                    ComponentName(it, WorkerNotifier::class.java)
                }

                //Now create a JobInfo and give
                val jobInfo = mComponentName?.let {
                    JobInfo.Builder(WorkerNotifier.JOB_NOTIFIER_ID, it)
                        .setPeriodic(WorkerNotifier.TIME_SERVICE).build()
                }

                val mScheduler =
                    context?.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

                if (jobInfo?.let { mScheduler.schedule(it) } == JobScheduler.RESULT_SUCCESS) {
                    WorkerNotifier().showNotification(
                        "Servizio schedulato!" + mScheduler.allPendingJobs[0].intervalMillis,
                        "partitooooo vedremo tra 15 minuti"
                    )//todo remove
                    Log.i(Log.DEBUG.toString(), "ALL IS FINEEEEEEEEEEEEEEEEEE")
                } else {
                    Log.i(Log.DEBUG.toString(), "NOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO")
                }

                println(mScheduler.allPendingJobs[0].isPeriodic)
            }*/

                val periodicReq = PeriodicWorkRequest.Builder(
                    WorkerNotifier::class.java,
                    PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS
                ).build()

                WorkManager.getInstance().enqueue(periodicReq)
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