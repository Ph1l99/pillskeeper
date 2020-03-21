package com.pillskeeper.data

import com.pillskeeper.data.abstracts.ReminderAbstract
import com.pillskeeper.enums.DaysEnum
import java.util.*

data class Appointment(
    var name: String,
    var minutes: Int,
    var hours: Int,
    var startingDay: Date,
    var days: LinkedList<DaysEnum>?,
    var expireDate: Date?,
    var additionNotes: String?
) : ReminderAbstract(minutes, hours, startingDay, days, expireDate, additionNotes)