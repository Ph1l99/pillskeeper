package com.pillskeeper.notifier

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.pillskeeper.data.Appointment
import com.pillskeeper.data.ReminderMedicineSort
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.enums.TypeIntentWorker
import com.pillskeeper.utility.Utils
import java.util.*


class EventBroadcastReceiver : BroadcastReceiver() {

    companion object {

        private const val TYPE_INTENT = "TypeIntent"
        private const val VALUE_INTENT = "ValueIntent"

    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(Log.DEBUG.toString(), "ServiceStarter: onReceive() - Function started")
        if (intent != null) {
            if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
                planAlarmDay(context)
                planNextDayPlanner(context)
            } else if (intent.getStringExtra(TYPE_INTENT) != null) {
                if (intent.getStringExtra(TYPE_INTENT) == TypeIntentWorker.SHOW_NOTIFY.toString()) {
                    val item = Utils.deserialize(intent.getByteArrayExtra(VALUE_INTENT))
                    if (context != null)
                        Notifier.showNotification(context, item)
                } else {
                    planAlarmDay(context)
                    planNextDayPlanner(context)
                }
            }
            println(println(intent.extras.toString()))
        }
        Log.i(Log.DEBUG.toString(), "ServiceStarter: onReceive() - Function ended")
    }


    fun planAlarmDay(context: Context?){
        Log.i(Log.DEBUG.toString(), "EventBroadcastReceiver: planAlarmDay() - Started")

        if (context != null) {
            val listAlarm = getDailyList()
            listAlarm.forEach {
                if(it is ReminderMedicineSort) println(it.reminder.toString()) else println(it.toString())//TODO remove
            }

            println()
            if (!listAlarm.isEmpty()) {
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                listAlarm.forEach {
                    val intent = Intent(context,EventBroadcastReceiver::class.java)



                    val item = Utils.serialize(
                    if(it is ReminderMedicineSort) {
                        it
                    } else {
                        (it as Appointment)
                    })


                    intent.putExtra(TYPE_INTENT,TypeIntentWorker.SHOW_NOTIFY.toString())
                    intent.putExtra(VALUE_INTENT, item)
                    intent.action = Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS

                    val itTime = getDateFromItem(it)
                    val itID = generateIdForItem(it,itTime)
                    if(!isAlreadyExistingIntent(context,itID,intent)){
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
                        println("alarm già presente!!!!!!!! $itTime")
                    }

                }
            }
        }
        Log.i(Log.DEBUG.toString(), "EventBroadcastReceiver: planAlarmDay() - Ended")
    }

    fun planNextDayPlanner(context: Context?) {
        Log.i(Log.DEBUG.toString(), "MainActivity: planNextDayPlanner() - Started")

        if (context != null) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context,EventBroadcastReceiver::class.java)
                .apply {
                    putExtra(TYPE_INTENT,TypeIntentWorker.PLAN_DAY_ALARM.toString())
                    action = Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
                }
            val itID = 0
            if(!isAlreadyExistingIntent(context,itID,intent)) {
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

                val cal = Calendar.getInstance()
                cal.time = Date()
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)
                cal.set(Calendar.HOUR_OF_DAY, 7)
                cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1)

                if (Build.VERSION.SDK_INT >= 23)
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        cal.timeInMillis,
                        pendingIntent
                    )
                else
                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        cal.timeInMillis,
                        pendingIntent
                    )
                Log.i(
                    Log.DEBUG.toString(),
                    "MainActivity: planNextDayPlanner() - Next Day planned... ${cal.time}"
                )
            } else {
                Log.i(
                    Log.DEBUG.toString(),
                    "MainActivity: planNextDayPlanner() - Next already planned!!!"
                )
            }
        }
        Log.i(Log.DEBUG.toString(), "MainActivity: planNextDayPlanner() - Ended")
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

    private fun isAlreadyExistingIntent(context: Context,itID: Int,intent: Intent): Boolean{
        return PendingIntent.getBroadcast(
            context,
            itID,
            intent,
            PendingIntent.FLAG_NO_CREATE) != null
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

}