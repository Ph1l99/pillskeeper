package com.pillskeeper.data

import com.pillskeeper.data.abstracts.AbstractMedicine
import com.pillskeeper.enums.MedicineTypeEnum
import java.util.*

data class LocalMedicine(
    var name: String,
    var medicineType: MedicineTypeEnum,
    var totalPills: Float,
    var remainingPills: Float,
    var reminders: LinkedList<ReminderMedicine>?,
    var id :String
) : AbstractMedicine(name, id, medicineType)