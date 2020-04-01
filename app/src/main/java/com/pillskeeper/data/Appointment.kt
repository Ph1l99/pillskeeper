package com.pillskeeper.data

import java.io.Serializable
import java.util.*

data class Appointment(
    var name: String,
    var date: Date,
    var additionNotes: String?
): Serializable