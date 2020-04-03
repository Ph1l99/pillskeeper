package com.pillskeeper.activity.medicine.formfragments

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.pillskeeper.enums.MedicineTypeEnum

class FormAdapter(fm: FragmentManager, private val intent: Intent, private val viewPager: PillsViewPager): FragmentPagerAdapter(fm) {

    companion object{
        const val FORM_ONE = 0
        const val FORM_TWO = 1
        const val FORM_THREE = 2

        lateinit var pillName: String
        lateinit var medicineType: MedicineTypeEnum
        var totalQuantity: Float = 0.0F
        var remainingQuantity: Float = 0.0F
    }

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
}