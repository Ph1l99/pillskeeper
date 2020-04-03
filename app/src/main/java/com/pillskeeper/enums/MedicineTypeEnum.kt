package com.pillskeeper.enums

import com.pillskeeper.R

enum class MedicineTypeEnum(var type:String,var text: Int) {
    PILLS           ("Pills", R.string.Pills),
    SYRUP           ("Syrup",R.string.Syrup),
    SUPPOSITORY     ("Suppository",R.string.Suppository),
    UNDEFINED       ("Undefined",R.string.Undefined)

}