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
import com.pillskeeper.data.Appointment
import com.pillskeeper.data.LocalMedicine
import com.pillskeeper.data.ReminderMedicineSort
import com.pillskeeper.datamanager.UserInformation.context
import java.util.*

object NotificationBuilder {

    private var notificationManager : NotificationManager? = getSystemService(context, NotificationManager::class.java)
    private const val NOTIFICATION_CHANNEL_ID = "6081945"

    init {
        createNotificationChannel()
    }

    fun showNotificationReminder(context: Context, it: Any?) {

        val icon: Int
        val title: String
        var text = "Buongiorno! "

        val cal = Calendar.getInstance()
        cal.time = Date()
        val timeStr = cal.get(Calendar.HOUR_OF_DAY).toString() + ":" + cal.get(Calendar.MINUTE).toString()
        if(it is ReminderMedicineSort) {
            title = "Medicina!"
            text += "Hai una nuova medicina da prendere. \n${it.medName}: ${it.reminder.dosage} ${context.getText(it.medType.text)} \n $timeStr"
            icon = R.drawable.records_medicines
        } else {
            (it as Appointment)
            title = "Appuntamento!"
            text += "Ha un appuntamento: ${it.name} $timeStr"
            icon = R.drawable.calendar
        }

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM/*TYPE_NOTIFICATION*/)
        val notificationBuilder = NotificationCompat.Builder(context)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(text)
            .setAutoCancel(false)
            .setPriority(Notification.VISIBILITY_PUBLIC)
            .setDefaults(Notification.DEFAULT_ALL)
            .setSound(soundUri)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(text))

        val id = ((Date().time / 1000L) % Int.MAX_VALUE).toInt()
        notificationManager?.notify(id, notificationBuilder.build())
    }

    fun showNotificationLowQuantity(context: Context, med :LocalMedicine){

        val icon = R.drawable.records_medicines
        val title = "Medicina in esaurimento"
        val text = "Attenzione, la seguente medicina si sta esaurendo! \n${med.name} ${med.remainingQty} ${context.getText(med.medicineType.text)}"


        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText("$text - ${Date()}")
            .setAutoCancel(false)
            .setSound(soundUri)
            .setPriority(Notification.VISIBILITY_PUBLIC)
            .setDefaults(Notification.DEFAULT_ALL)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(text))

        val id = ((Date().time / 1000L) % Int.MAX_VALUE).toInt()
        notificationManager?.notify(id, notificationBuilder.build())

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "channelPillsKeeper"
            val descriptionText = "Channel used for PillsKeeper"
            val importance = NotificationManager.IMPORTANCE_HIGH//IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            notificationManager?.createNotificationChannel(channel)
        }
    }

}