package com.pillskeeper.notifier

import android.app.NotificationManager
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.pillskeeper.R

class ServiceNotifier : JobService() {


    companion object {
        private const val SEC: Long = 1000
        private const val MIN: Long = 60 * SEC
        const val TIME_SERVICE: Long = 7 * MIN
        const val JOB_NOTIFIER_ID = 1
        lateinit var context: Context
    }

    fun showNotification(title: String, text: String) {
        /*val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)*/
        /*val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )*/

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(text)
            .setAutoCancel(false)
            .setSound(soundUri)
            //.setContentIntent(pendingIntent)

        val notificationManager = getSystemService(context, NotificationManager::class.java)
        notificationManager?.notify(0, notificationBuilder.build())
    }


    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }

    override fun onStartJob(params: JobParameters?): Boolean {

        while (true){
        showNotification("ciao","ciao")

        Thread.sleep(1000)
        }
        return true
    }

}