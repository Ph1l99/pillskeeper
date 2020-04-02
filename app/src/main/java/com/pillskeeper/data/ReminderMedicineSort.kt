package com.pillskeeper.data

import com.pillskeeper.enums.MedicineTypeEnum
import java.io.Serializable

class ReminderMedicineSort(var medName: String, var medType:MedicineTypeEnum, var reminder : ReminderMedicine): Serializable