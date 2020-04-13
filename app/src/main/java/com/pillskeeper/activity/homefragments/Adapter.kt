package com.pillskeeper.activity.homefragments

import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.pillskeeper.R

class Adapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    companion object{
        const val PILLS_PAGE = 0
        const val APPOINTMENTS_PAGE = 1
    }



    override fun getItem(position: Int): Fragment {
        return when(position){
            PILLS_PAGE -> ReminderMedicineListFragment()
            APPOINTMENTS_PAGE -> ReminderAppointmentListFragment()
            else -> ReminderMedicineListFragment()

        }
    }

    override fun getCount(): Int {
        return 2
    }
}