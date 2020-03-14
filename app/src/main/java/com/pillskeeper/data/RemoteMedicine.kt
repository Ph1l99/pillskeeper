package com.pillskeeper.data

import com.pillskeeper.enums.MedicineTypeEnum

data class RemoteMedicine(
    var name: String,
    var type: MedicineTypeEnum,
    var medicineId: String,
    var photoId: String
) :
    Medicine(
        name, type,
        medicineId
    ) {
}