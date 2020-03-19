package com.pillskeeper.data

import com.pillskeeper.enums.MedicineTypeEnum

data class RemoteMedicine(
    var name: String,
    var id: String,
    var medicineType: MedicineTypeEnum
) : AbstractMedicine(name, id, medicineType) {
    companion object {

        fun getMedicineListFromMap(medicinesMaps: Map<String, Map<String, String>>): List<RemoteMedicine> {
            var listResult = mutableListOf<RemoteMedicine>()
            for ((k, v) in medicinesMaps) {
                listResult.add(k.toInt(), getMedicineFromMap(v))
            }
            return listResult
        }

        fun getMedicineFromMap(medicineMap: Map<String, String>): RemoteMedicine {
            lateinit var name: String
            lateinit var id: String
            lateinit var medicineType: MedicineTypeEnum
            for ((k, v) in medicineMap) {
                when (k) {
                    "id" -> id = v
                    "name" -> name = v
                    "medicineType" -> medicineType = MedicineTypeEnum.valueOf(v)
                }
            }
            return RemoteMedicine(name, id, medicineType)
        }
    }
}