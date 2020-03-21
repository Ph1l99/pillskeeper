package com.pillskeeper.data

import com.pillskeeper.enums.MedicineTypeEnum
import java.io.Serializable

data class RemoteMedicine(
    var name: String,
    var id: String,
    var medicineType: MedicineTypeEnum
) : AbstractMedicine(name, id, medicineType) {

    companion object : Serializable {

        fun getMedicineListFromMap(medicinesMaps: Map<String, Map<String, String>>): List<RemoteMedicine> {
            val listResult = mutableListOf<RemoteMedicine>()
            for ((_, v) in medicinesMaps) {
                listResult.add(getMedicineFromMap(v))
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