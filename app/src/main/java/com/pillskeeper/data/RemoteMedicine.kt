package com.pillskeeper.data

import com.pillskeeper.data.abstracts.AbstractMedicine
import com.pillskeeper.enums.MedicineTypeEnum
import java.io.Serializable

/**
 * Data class Remote Medicine that extends AbstractMedicine
 * @param name The medicine name
 * @param id The medicine ID
 * @param medicineType The MedicineTypeEnum indicating the type of medicine
 */
data class RemoteMedicine(
    var name: String,
    var id: String,
    var medicineType: MedicineTypeEnum
) : AbstractMedicine(name, id, medicineType) {

    companion object : Serializable {

        /**
         * Method for getting a List of RemoteMedicines from the Map representing the Realtime Database JSON
         * @param medicinesMaps The Map representing the Realtime Database JSON
         * @return List<RemoteMedicine>
         */
        fun getMedicineListFromMap(medicinesMaps: Map<String, Map<String, String>>): List<RemoteMedicine> {
            val listResult = mutableListOf<RemoteMedicine>()
            for ((_, v) in medicinesMaps) {
                listResult.add(getMedicineFromMap(v))
            }
            return listResult
        }

        /**
         * Function that obtains a RemoteMedicine object from a map, tipically a JSON from the
         * Realtime Database
         * @param medicineMap The Map that represents a node in the JSON format
         * @return The RemoteMedicine extracted from the map
         */
        private fun getMedicineFromMap(medicineMap: Map<String, String>): RemoteMedicine {
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