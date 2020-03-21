package com.pillskeeper.data

import java.util.*

data class Appointment(
    var name: String,
    var minutes: Int,
    var hours: Int,
    var date: Date?,
    var additionNotes: String?
)