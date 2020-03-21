package com.pillskeeper.data

import com.pillskeeper.data.abstracts.ReminderAbstract
import com.pillskeeper.enums.DaysEnum
import java.util.*

data class ReminderMedicine(
    var dosage: Float,
    var minutes: Int,
    var hours: Int,
    var startingDay: Date,
    var days: LinkedList<DaysEnum>?,
    var expireDate: Date?,
    var additionNotes: String?
) : ReminderAbstract(minutes, hours, startingDay, days, expireDate, additionNotes)