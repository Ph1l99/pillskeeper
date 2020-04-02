package com.pillskeeper.activity.medicine.formfragments

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class FormAdapter(fm: FragmentManager, intent: Intent): FragmentPagerAdapter(fm) {

    companion object{
        const val FORM_ONE = 0
        const val FORM_TWO = 1
    }

    private val intent: Intent = intent

    override fun getItem(position: Int): Fragment {
        return when(position) {
            FORM_ONE -> FormOneFragment(intent)
            FORM_TWO -> FormTwoFragment(intent)
            else -> FormOneFragment(intent)
        }
    }

    override fun getCount(): Int {
        return 1
    }


}