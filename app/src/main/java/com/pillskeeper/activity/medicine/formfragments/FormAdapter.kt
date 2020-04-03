package com.pillskeeper.activity.medicine.formfragments

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.pillskeeper.enums.MedicineTypeEnum
import java.util.*
import kotlin.collections.ArrayList

class FormAdapter(fm: FragmentManager, intent: Intent, viewPager: PillsViewPager): FragmentPagerAdapter(fm) {

    companion object{
        const val FORM_ONE = 0
        const val FORM_TWO = 1
        const val FORM_THREE = 2

        lateinit var pillName: String
        lateinit var medicineType: MedicineTypeEnum
        var totalQuantity: Float = 0.0F
        var remainingQuantity: Float = 0.0F
    }

    private val intent: Intent = intent
    private val viewPager = viewPager

  /*  lateinit var pillName: String
    lateinit var medicineType: MedicineTypeEnum
    var totalQuantity: Float = 0.0F
    var remainingQuantity: Float = 0.0F
   */

    override fun getItem(position: Int): Fragment {
        return when(position) {
            FORM_ONE -> FormOneFragment(intent, viewPager)
            FORM_TWO -> FormTwoFragment(viewPager)
            FORM_THREE -> FormThreeFragment(viewPager)
            else -> FormOneFragment(intent, viewPager)
        }
    }

    override fun getCount(): Int {
        return 3
    }

    /*
    public fun addPillName(name: String){
        pillName = name
    }
    public fun pillName(): String{
        return pillName
    }

    public fun addMedicineType(type: MedicineTypeEnum){
        medicineType = type
    }
    fun medicineType(): MedicineTypeEnum{
        return medicineType
    }

    fun addtotalQuantity(quantity: Float){
        totalQuantity = quantity
    }
    fun totalQuantity(): Float{
        return totalQuantity
    }

    fun addRemainingQuantity(quantity: Float){
        remainingQuantity = quantity
    }
    fun remainingQuantity(): Float {
        return remainingQuantity
    }
     */
}