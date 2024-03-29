package com.pillskeeper.notifier

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.pillskeeper.data.Appointment
import com.pillskeeper.data.ReminderMedicineSort
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.enums.TypeIntentWorker
import com.pillskeeper.utility.Utils
import java.util.*

object NotifyPlanner{

    private const val ACTION_PLANNER = "com.pillskeeper.notifier.ACTION_PLANNER"
    const val ACTION_NEXT_DAY_PLANNER = "com.pillskeeper.notifier.ACTION_NEXT_DAY_PLANNER"
    private const val TAG = "NotifyPlanner: "

    fun planFullDayAlarms(context: Context?){
        Log.i(TAG, "planFullDayAlarms() - Started")

        if (context != null) {
            val listAlarm = getDailyList()

            if (!listAlarm.isEmpty()) {
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                listAlarm.forEach {
                    planSingleAlarm(context,alarmManager,it)
                }
            }
        }
        Log.i(TAG, "planFullDayAlarms() - Ended")
    }

    fun planSingleAlarm(context: Context, alarmManager: AlarmManager, it: Any){
        Log.i(TAG, "planSingleAlarm() - Started")

        val intent = buildIntent(context, it)

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

            val cal = Calendar.getInstance()
            cal.time = itTime
            Log.i(TAG, "planSingleAlarm() - Alarm planned ${cal.get(Calendar.HOUR_OF_DAY)}:${cal.get(Calendar.MINUTE)} - ${cal.get(Calendar.DAY_OF_MONTH)}/${cal.get(Calendar.MONTH)}")
        }

        Log.i(TAG, "planSingleAlarm() - Ended")
    }

    fun planNextDayPlanner(context: Context?) {
        Log.i(TAG, "planNextDayPlanner() - Started")

        if (context != null) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context,EventBroadcastReceiver::class.java)
                .apply {
                    putExtra(EventBroadcastReceiver.TYPE_INTENT,TypeIntentWorker.PLAN_DAY_ALARM.toString())
                    action = ACTION_NEXT_DAY_PLANNER
                }
            val cal = Calendar.getInstance()
            cal.time = Date()
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            cal.set(Calendar.HOUR_OF_DAY, 4)
            cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1)

            val itID = generateIdForPlanner(cal)
            if(!isAlreadyExistingIntent(context,itID,intent)) {
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

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
                    TAG,
                    "planNextDayPlanner() - Next Day planned... ${cal.time}"
                )
            } else {
                Log.i(
                    TAG,
                    "planNextDayPlanner() - Next already planned!!!"
                )
            }
        }
        Log.i(TAG, "planNextDayPlanner() - Ended")
    }

    private fun buildIntent(context: Context, it: Any, debug: Boolean = false): Intent{
        val intent = Intent(context,EventBroadcastReceiver::class.java)

        val item = Utils.serialize(
            if(it is ReminderMedicineSort) {
                it
            } else {
                (it as Appointment)
            })

        if (debug)
            intent.putExtra(EventBroadcastReceiver.TYPE_INTENT, TypeIntentWorker.SHOW_NOTIFY_DEBUG.toString())
        else
            intent.putExtra(EventBroadcastReceiver.TYPE_INTENT, TypeIntentWorker.SHOW_NOTIFY.toString())

        intent.putExtra(EventBroadcastReceiver.VALUE_INTENT, item)
        intent.action = ACTION_PLANNER
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

    private fun generateIdForPlanner(cal: Calendar): Int{
        return ((cal.time.time / 1000L) % Int.MAX_VALUE).toInt()
    }

    private fun isAlreadyExistingIntent(context: Context, itID: Int, intent: Intent): Boolean {
        return PendingIntent.getBroadcast(
            context,
            itID,
            intent,
            PendingIntent.FLAG_NO_CREATE
        ) != null
    }

    fun remove(context: Context, it: Any){

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = buildIntent(context, it)
        val itTime = getDateFromItem(it)
        val itID = generateIdForItem(it,itTime)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            itID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.cancel(pendingIntent)
    }


}