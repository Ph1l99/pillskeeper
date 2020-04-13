package com.pillskeeper.data

import com.pillskeeper.data.abstracts.ReminderAbstract
import com.pillskeeper.enums.DaysEnum
import java.util.*


data class ReminderMedicine(
    var dosage: Float,
    var minutes: Int,// TODO change with short!!!!! and make nullable
    var hours: Int,// TODO change with short!!!!! and make nullable
    var startingDay: Date,
    var days: LinkedList<DaysEnum>?,
    var expireDate: Date?,
    var additionNotes: String?
) : ReminderAbstract(minutes, hours, startingDay, days, expireDate, additionNotes) {
    
    override fun equals(other: Any?) : Boolean{
        if (other !is ReminderMedicine) return false

        if(this.startingDay == other.startingDay && this.expireDate == other.expireDate && this.hours == other.hours
                && this.minutes == other.minutes && this.dosage == other.dosage){
            return compareDaysList(this.days,other.days)

        }
        return false
    }

    fun dayStringify(): String{
        var daysString = ""
        for (i in days!!.indices) {
            daysString += "${days!![i]}"
            if(i < days!!.size-1)
                daysString += " - "
        }
        return daysString
    }

    fun isSingleDayRem(): Boolean {
        return startingDay == expireDate
    }

    private fun compareDaysList(thisList: LinkedList<DaysEnum>?, comparator: LinkedList<DaysEnum>?): Boolean{
        if(thisList == null && comparator == null)
            return true
        else {
            if(thisList == null || comparator == null)
                return false
            else {
                if(thisList.size != comparator.size)
                    return false
                else {
                    val temp = LinkedList(comparator)
                    thisList.forEach {day ->
                        for(i in 0..temp.size) {
                            if (temp[i] == day) {
                                temp.removeAt(i)
                                break
                            }
                        }
                    }
                    return temp.isEmpty()
                }
            }
        }
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}