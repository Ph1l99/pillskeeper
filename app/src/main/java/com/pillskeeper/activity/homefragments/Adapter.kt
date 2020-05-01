package com.pillskeeper.activity.homefragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class Adapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        const val PILLS_PAGE = 0
        const val APPOINTMENTS_PAGE = 1
    }


    override fun getItem(position: Int): Fragment {
        return when (position) {
            PILLS_PAGE -> ReminderMedicineListFragment()
            APPOINTMENTS_PAGE -> ReminderAppointmentListFragment()
            else -> ReminderMedicineListFragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }
}