package com.pillskeeper.notifier

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.pillskeeper.R
import java.util.*

object Notifier {

    fun showNotification(title: String, text: String, context: Context) {
        createNotificationChannel(context)

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

    private fun createNotificationChannel(context: Context) {
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