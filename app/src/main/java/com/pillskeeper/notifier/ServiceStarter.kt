package com.pillskeeper.notifier

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log


class ServiceStarter : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        Log.i(Log.DEBUG.toString(), "ServiceStarter: onReceive() - Function started")

        startNotifierThread(context)

        Log.i(Log.DEBUG.toString(), "ServiceStarter: onReceive() - Service started")
    }

    companion object {
        fun startNotifierThread(context: Context?) {
            Log.i(Log.DEBUG.toString(), "MainActivity: startNotifierThread() - Started")

            if (context != null)
                ServiceNotifier.context = context

            if (!isJobServiceOn(context)) {
                val mComponentName = context?.let {
                    ComponentName(it, ServiceNotifier::class.java) }

                //Now create a JobInfo and give
                val jobInfo = mComponentName?.let {
                    JobInfo.Builder(ServiceNotifier.JOB_NOTIFIER_ID, it)
                        .setPeriodic(ServiceNotifier.TIME_SERVICE).build()
                }

                val mScheduler =
                    context?.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

                if (jobInfo?.let { mScheduler.schedule(it) } == JobScheduler.RESULT_SUCCESS) {
                    ServiceNotifier().showNotification(
                        "Servizio schedulato!" + mScheduler.allPendingJobs[0].intervalMillis,
                        "partitooooo vedremo tra 7 minuti"
                    )//todo remove
                    Log.i(Log.DEBUG.toString(), "ALL IS FINEEEEEEEEEEEEEEEEEE")
                }
                else {
                    Log.i(Log.DEBUG.toString(), "NOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO")
                }

                println(mScheduler.allPendingJobs[0].isPeriodic)
            }

            Log.i(Log.DEBUG.toString(), "MainActivity: startNotifierThread() - Ended")
        }


        private fun isJobServiceOn(context: Context?): Boolean {
            Log.i(Log.DEBUG.toString(), "MainActivity: isJobServiceOn() - Started")
            if (context != null) {
                (context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler)
                    .allPendingJobs.forEach { entry ->
                        if (entry.id == ServiceNotifier.JOB_NOTIFIER_ID) {
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
        }
    }

}
/*val intent = Intent(context, ServiceNotifier::class.java)
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
    context!!.startForegroundService(intent)
else
    context!!.startService(intent)
*/