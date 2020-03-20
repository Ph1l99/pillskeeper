package com.pillskeeper.data

import com.pillskeeper.enums.DaysEnum
import java.util.*

abstract class Reminder (minutes: Int, hours: Int, startingDay: Date,
                         days: LinkedList<DaysEnum>?, expireDate: Date?, additionNotes: String?)