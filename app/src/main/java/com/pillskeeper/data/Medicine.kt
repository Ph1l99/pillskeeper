package com.pillskeeper.data

data class Medicine (var name:String, var totalPills: Int, var remainingPills: Int, var reminders: LinkedHashMap<String, Reminder>)