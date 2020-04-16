package com.pillskeeper.enums

import com.pillskeeper.R

/**
 * Enumerative which represents the type of a Medicine
 */
enum class MedicineTypeEnum(var type: String, var text: Int) {
    PILLS("Pills", R.string.Pills),
    SYRUP("Syrup", R.string.Syrup),
    SUPPOSITORY("Suppository", R.string.Suppository),
    UNDEFINED("Undefined", R.string.Undefined)

}