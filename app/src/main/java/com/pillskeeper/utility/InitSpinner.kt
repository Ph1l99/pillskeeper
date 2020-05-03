package com.pillskeeper.utility

import android.content.Context
import android.widget.ArrayAdapter
import java.util.*

object InitSpinner {

    private const val MINUTE_LIMIT = 11
    private const val DOSAGE_LIMIT = 10

    const val MINUTE_MULTI = 5F
    const val DOSAGE_MULTI = 0.5F

    val hours = arrayListOf(
        "00", "01", "02", "03", "04", "05", "06", "07", "08", "09"
        , "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"
    )

    fun initSpinnerDosage(context: Context): ArrayAdapter<String> {
        val arrayAdapterGeneric = ArrayAdapter(
            context, android.R.layout.simple_spinner_item,
            getArrayValue(
                DOSAGE_LIMIT,
                DOSAGE_MULTI
            )
        )
        arrayAdapterGeneric.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return arrayAdapterGeneric
    }

    fun initSpinnerHour(context: Context): ArrayAdapter<String> {
        val arrayAdapterGeneric = ArrayAdapter(
            context, android.R.layout.simple_spinner_item,
            hours
        )
        arrayAdapterGeneric.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return arrayAdapterGeneric
    }

    fun initSpinnerMinute(context: Context): ArrayAdapter<String> {
        val arrayAdapterGeneric = ArrayAdapter(
            context, android.R.layout.simple_spinner_item,
            getArrayValue(
                MINUTE_LIMIT,
                MINUTE_MULTI
            )
        )
        arrayAdapterGeneric.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return arrayAdapterGeneric
    }

    private fun getArrayValue(limit: Int, multiplier: Float): ArrayList<String> {
        val genericArray = ArrayList<String>()
        for (i in 0..limit)
            genericArray.add(
                if (multiplier > 1)
                    if (i < 2)
                        "0${(i * multiplier).toInt()}"
                    else
                        "${(i * multiplier).toInt()}"
                else
                    "${i * multiplier}"
            )
        return genericArray
    }

}