package com.pillskeeper.data.abstracts

import com.pillskeeper.enums.DaysEnum
import java.util.*

abstract class ReminderAbstract (minutes: Int, hours: Int, startingDay: Date,
                                 days: LinkedList<DaysEnum>?, expireDate: Date?, additionNotes: String?)