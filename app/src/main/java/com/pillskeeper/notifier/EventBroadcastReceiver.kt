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
        if (intent != null )
            if(intent.action == Intent.ACTION_BOOT_COMPLETED){
                planAlarmDay(context)
                planNextDayPlanner(context)
            } else if (intent.getStringExtra(TYPE_INTENT) != null) {
                if(intent.getSerializableExtra(TYPE_INTENT) == TypeIntentWorker.SHOW_NOTIFY){
                    if (context != null)
                        Notifier.showNotification("ciao","ciao",context)
                } else {
                    planAlarmDay(context)
                    planNextDayPlanner(context)
                }
            }
        Log.i(Log.DEBUG.toString(), "ServiceStarter: onReceive() - Function ended")
    }


    fun planAlarmDay(context: Context?){
        Log.i(Log.DEBUG.toString(), "MainActivity: planAlarmDay() - Started")

        if (context != null) {
            val listAlarm = getDailyList()
            listAlarm.forEach {
                if(it is ReminderMedicineSort) println(it.reminder.toString()) else it.toString()
            }

            if (!listAlarm.isEmpty()) {
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                listAlarm.forEach {
                    val intent = Intent(context,EventBroadcastReceiver::class.java)
                        .apply {
                            putExtra(VALUE_INTENT,
                                when(it) {
                                    it is ReminderMedicineSort -> { it as ReminderMedicineSort }
                                    it is Appointment -> { it as Appointment }
                                    else -> { null }
                                })
                            putExtra(TYPE_INTENT,TypeIntentWorker.SHOW_NOTIFY)
                            action = Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
                        }
                    val itTime = getDateFromItem(it)
                    if(PendingIntent.getBroadcast(
                        context, ((itTime.time / 1000L) % Int.MAX_VALUE).toInt(),
                        intent,
                        PendingIntent.FLAG_NO_CREATE) == null){

                        println("CREANDOOOOOO $itTime")
                        val pendingIntent = PendingIntent.getBroadcast(
                            context,
                            ((itTime.time / 1000L) % Int.MAX_VALUE).toInt(),
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
        Log.i(Log.DEBUG.toString(), "MainActivity: planAlarmDay() - Ended")
    }

    fun planNextDayPlanner(context: Context?) {
        Log.i(Log.DEBUG.toString(), "MainActivity: planNextDayPlanner() - Started")

        if (context != null) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context,EventBroadcastReceiver::class.java)
                .apply {
                    putExtra(TYPE_INTENT,TypeIntentWorker.PLAN_DAY_ALARM)
                    action = Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
                }
            val pendingIntent = PendingIntent.getBroadcast(context,((Date().time / 1000L) % Int.MAX_VALUE).toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT)

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
            Log.i(Log.DEBUG.toString(), "MainActivity: planNextDayPlanner() - Next Day planned... ${cal.time}")
        }
        Log.i(Log.DEBUG.toString(), "MainActivity: planNextDayPlanner() - Ended")
    }



    private fun getDailyList(): LinkedList<Any> {
        val filterDate = Date(Utils.dataNormalizationLimit(Date()))
        val nowDate = Date()
        val orderedReminderList = Utils.getSortedListReminders(filterDate)

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

}
