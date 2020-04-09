package com.pillskeeper.data

import com.pillskeeper.data.abstracts.ReminderAbstract
import com.pillskeeper.enums.DaysEnum
import java.util.*

data class ReminderMedicine(
    var dosage: Float,
    var minutes: Int,// TODO change with short!!!!! and make nullable
    var hours: Int,
    var startingDay: Date,
    var days: LinkedList<DaysEnum>?,
    var expireDate: Date?,
    var additionNotes: String?
) : ReminderAbstract(minutes, hours, startingDay, days, expireDate, additionNotes) {
    
    fun isEquals(comparator:ReminderMedicine) : Boolean{
        if(this.startingDay == comparator.startingDay && this.expireDate == comparator.expireDate && this.hours == comparator.hours
                && this.minutes == comparator.minutes && this.dosage == comparator.dosage){
            if(this.days?.equals(comparator.days)!!){
                return true
            }
        }
        return false
    }

    fun dayStringify(): String{
        var daysString = ""
        for (i in days!!.indices) {
            daysString += "${days!![i]}"
            if(i <= days!!.size-1)
                daysString += " - "
        }
        return daysString
    }
}