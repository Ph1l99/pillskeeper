package com.pillskeeper.data

import java.util.*

data class Reminder(var number_pills: Float, var minutes: Int, var hours: Int,
                    var days: String, var duration: Date, var additionNotes: String?)