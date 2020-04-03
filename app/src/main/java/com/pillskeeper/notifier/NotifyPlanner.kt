package com.pillskeeper.notifier

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import com.pillskeeper.data.Appointment
import com.pillskeeper.data.ReminderMedicineSort
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.enums.TypeIntentWorker
import com.pillskeeper.utility.Utils
import java.util.*

class NotifyPlanner: Service() {

    private fun planAlarmDay(context: Context?){
        Log.i(Log.DEBUG.toString(), "EventBroadcastReceiver: planAlarmDay() - Started")

        if (context != null) {
            val listAlarm = getDailyList()

            if (!listAlarm.isEmpty()) {
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                listAlarm.forEach {
                    planSingleAlarm(context,alarmManager,it)
                }
            }
        }
        Log.i(Log.DEBUG.toString(), "EventBroadcastReceiver: planAlarmDay() - Ended")
    }

    fun planSingleAlarm(context: Context, alarmManager: AlarmManager, it: Any){
        val intent = buildIntent(it)

        val itTime = getDateFromItem(it)
        val itID = generateIdForItem(it,itTime)
        if(!Utils.isAlreadyExistingIntent(context,itID,intent)){
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                itID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            if (Build.VERSION.SDK_INT >= 23)
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    itTime.time,
                    pendingIntent
                )
            else
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    itTime.time,
                    pendingIntent
                )
        } else {
            println("alarm già presente!!!!!!!! $itTime") //TODO remove
        }

    }

    private fun buildIntent(it: Any): Intent{
        val intent = Intent(applicationContext,EventBroadcastReceiver::class.java)

        val item = Utils.serialize(
            if(it is ReminderMedicineSort) {
                it
            } else {
                (it as Appointment)
            })

        intent.putExtra(EventBroadcastReceiver.TYPE_INTENT, TypeIntentWorker.SHOW_NOTIFY.toString())
        intent.putExtra(EventBroadcastReceiver.VALUE_INTENT, item)
        intent.action = Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
        return intent
    }

    private fun getDailyList(): LinkedList<Any> {
        val filterDate = Date(Utils.dataNormalizationLimit(Date()))
        val nowDate = Date()
        var orderedReminderList = Utils.getSortedListReminders(filterDate)
        orderedReminderList = LinkedList(orderedReminderList.filter { it.reminder.startingDay > nowDate && it.reminder.startingDay <= filterDate })

        var appointmentListSorted = UserInformation.appointments
        appointmentListSorted = LinkedList(appointmentListSorted.filter {  it.date > nowDate && it.date <= filterDate })

        val mergedList = LinkedList<Any>()
        mergedList.addAll(appointmentListSorted)
        mergedList.addAll(orderedReminderList)
        mergedList.sortWith(compareBy {
            when (it) {
                is Appointment -> {
                    it.date
                }
                is ReminderMedicineSort -> {
                    it.reminder.startingDay
                }
                else -> {
                    null
                }
            }
        })

        return mergedList
    }

    private fun getDateFromItem(item: Any): Date {
        return when(item) {
            is ReminderMedicineSort -> { item.reminder.startingDay}
            is Appointment -> { item.date }
            else -> { Date() }
        }
    }

    private fun generateIdForItem(item: Any, itTime: Date): Int{
        var stdValue: Int =
            when (item){
                is ReminderMedicineSort -> (item.medName.length - item.medName.indexOf("c",0,true) - item.medName.indexOf("e",0,true))
                is Appointment -> (item.name.length - item.name.indexOf("c",0,true) - item.name.indexOf("e",0,true))
                else -> Random().nextInt(10)
            }
        if (stdValue <= 0) stdValue = 1
        return ((itTime.time * stdValue / 1000L) % Int.MAX_VALUE).toInt()
    }

    fun remove(it: Any){

        val alarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = buildIntent(it)
        val itTime = getDateFromItem(it)
        val itID = generateIdForItem(it,itTime)
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            itID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.cancel(pendingIntent)

    }

    override fun onBind(intent: Intent?): IBinder? {
        planAlarmDay(applicationContext)
        return null
    }


}