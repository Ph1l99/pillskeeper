package com.pillskeeper.data

import com.pillskeeper.interfaces.CastInterfaceCommon
import java.util.*

data class Appointment(
    var name: String,
    var date: Date,
    var additionNotes: String?
): CastInterfaceCommon