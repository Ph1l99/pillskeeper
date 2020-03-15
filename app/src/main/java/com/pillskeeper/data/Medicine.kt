package com.pillskeeper.data

import java.util.*

data class Medicine (var name:String, var totalPills: Float, var remainingPills: Float, var reminders: LinkedList<Reminder>)