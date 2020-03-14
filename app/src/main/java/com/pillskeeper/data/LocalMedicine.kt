package com.pillskeeper.data

import com.pillskeeper.enums.MedicineTypeEnum

data class LocalMedicine(
    var totalPills: Int, var remainingPills: Int, var reminders: LinkedHashMap<String, Reminder>,
    var name: String,
    var type: MedicineTypeEnum, var medicineId: String
) : Medicine(name, type, medicineId) {
}