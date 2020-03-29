package com.pillskeeper.notifier

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.pillskeeper.R
import java.util.*

class WorkerNotifier(context: Context, workerParams: WorkerParameters) : Worker(context,workerParams) {

    init {
        WorkerNotifier.context = context
    }

    companion object {
        lateinit var context: Context


        fun showNotification(title: String, text: String) {
            /*val intent = Intent(context, MainActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    val pendingIntent = PendingIntent.getActivity(
        context, 0, intent,
        PendingIntent.FLAG_ONE_SHOT
    )*/
            createNotificationChannel()

            val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.pills_icon)
                .setContentTitle(title)
                .setContentText("$text - ${Date()}")
                .setAutoCancel(false)
                .setSound(soundUri)
            //.setContentIntent(pendingIntent)

            val notificationManager = getSystemService(context, NotificationManager::class.java)
            val id = ((Date().time / 1000L) % Int.MAX_VALUE).toInt()
            notificationManager?.notify(id, notificationBuilder.build())
        }

        private fun createNotificationChannel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "caneee"
                val descriptionText = "descrizioneeee"
                val importance = NotificationManager.IMPORTANCE_HIGH//IMPORTANCE_DEFAULT
                val channel = NotificationChannel(3.toString(), name, importance).apply {
                    description = descriptionText
                    lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                }
                val notificationManager = getSystemService(context, NotificationManager::class.java)
                notificationManager?.createNotificationChannel(channel)
            }
        }
    }

    override fun doWork(): Result {
        showNotification("ciao","ciao")
        return Result.success()
    }

}