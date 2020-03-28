package com.pillskeeper.notifier

import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.pillskeeper.R

class WorkerNotifier(var context: Context, workerParams: WorkerParameters) : Worker(context,workerParams) {

    private fun showNotification(title: String, text: String) {
        /*val intent = Intent(context, MainActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    val pendingIntent = PendingIntent.getActivity(
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


    override fun doWork(): Result {
        showNotification("ciao","ciao")

        return Result.success()
    }

}