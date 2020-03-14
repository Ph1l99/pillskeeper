package com.pillskeeper.data

import com.pillskeeper.enums.MedicineTypeEnum

data class RemoteMedicine(
    var name: String, var id: String,
    var medicineType: MedicineTypeEnum
) : Medicine(name, id, medicineType) {
}