package com.pillskeeper.activity.medicine.formfragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.viewpager.widget.ViewPager

import com.pillskeeper.R

class FormTwoFragment(viewPager: PillsViewPager) : Fragment() {

    companion object {
        const val FORM_ONE = 0
        const val FORM_THREE = 2
    }

    private val viewPager = viewPager

    lateinit var textViewNext: TextView
    lateinit var textViewBack: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_form_two, container, false)

        textViewNext = view!!.findViewById(R.id.textViewNext)
        textViewBack = view!!.findViewById(R.id.textViewBack)



        /*LISTENERS*/
        textViewNext.setOnClickListener{
            viewPager.setCurrentItem(FORM_THREE)
        }

        textViewBack.setOnClickListener{
            viewPager.setCurrentItem(FORM_ONE)
        }

        return view
    }

}
