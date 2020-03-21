package com.pillskeeper.data

import com.pillskeeper.enums.MedicineTypeEnum
import java.io.Serializable

abstract class AbstractMedicine(name: String, id: String, medicineType: MedicineTypeEnum) :
    Serializable