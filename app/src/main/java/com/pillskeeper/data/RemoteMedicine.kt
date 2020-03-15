package com.pillskeeper.data

import com.pillskeeper.enums.MedicineTypeEnum

data class RemoteMedicine(
    var name: String,
    var id: String,
    var medicineType: MedicineTypeEnum
) : AbstractMedicine(name, id, medicineType)