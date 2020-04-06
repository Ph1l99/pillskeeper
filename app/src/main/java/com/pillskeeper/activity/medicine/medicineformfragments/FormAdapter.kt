package com.pillskeeper.activity.medicine.medicineformfragments

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.pillskeeper.data.LocalMedicine
import com.pillskeeper.data.ReminderMedicine
import com.pillskeeper.data.RemoteMedicine
import com.pillskeeper.enums.MedicineTypeEnum
import java.util.*

class FormAdapter(fm: FragmentManager, private val intent: Intent, private val viewPager: PillsViewPager): FragmentPagerAdapter(fm) {



    companion object{
        const val FORM_ONE = 0
        const val FORM_TWO = 1
        const val FORM_THREE = 2
        const val FORM_EDIT = 3

        lateinit var pillName: String
        lateinit var medicineType: MedicineTypeEnum
        var totalQuantity: Float = 0.0F
        var remainingQuantity: Float = 0.0F

        var reminderList: LinkedList<ReminderMedicine>? = null
        var remoteMedicine: RemoteMedicine? = null
        var localMedicine: LocalMedicine? = null
        lateinit var stdLayout: Drawable

        lateinit var formActivity: Activity


        fun closeForm(){
            formActivity.finish()
        }
    }

    override fun getItem(position: Int): Fragment {
        return when(position) {
            FORM_ONE -> FormOneFragment(intent, viewPager)
            FORM_TWO -> FormTwoFragment(viewPager)
            FORM_THREE -> FormThreeFragment(viewPager)
            FORM_EDIT -> FormEditingFragment()
            else -> FormOneFragment(intent, viewPager)
        }
    }

    override fun getCount(): Int {
        return 4
    }

}