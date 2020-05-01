package com.pillskeeper.data

import com.pillskeeper.enums.MedicineTypeEnum
import java.io.Serializable
import java.util.*

data class LocalMedicine(
    var name: String,
    var medicineType: MedicineTypeEnum,
    var totalQty: Float,
    var remainingQty: Float,
    var reminders: LinkedList<ReminderMedicine>?,
    var id :String
) : Serializable