package com.pillskeeper.data.abstracts

import com.pillskeeper.enums.MedicineTypeEnum
import java.io.Serializable

/**
 * Abstract Class representing a Medicine
 */
abstract class AbstractMedicine(name: String, id: String, medicineType: MedicineTypeEnum) :
    Serializable {
}