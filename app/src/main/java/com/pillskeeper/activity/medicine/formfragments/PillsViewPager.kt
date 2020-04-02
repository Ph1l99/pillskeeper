package com.pillskeeper.activity.medicine.formfragments

import android.content.Context
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class PillsViewPager(context: Context) : ViewPager(context) {

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }
}