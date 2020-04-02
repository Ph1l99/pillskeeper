package com.pillskeeper.activity.medicine.formfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.pillskeeper.R
import java.util.*

class FormThreeFragment(viewPager: PillsViewPager) : Fragment() {
    companion object {
        const val FORM_TWO = 1
    }

    private val viewPager = viewPager
    lateinit var textViewBack: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_form_three, container, false)

        textViewBack = view.findViewById(R.id.textViewBack)

        textViewBack.setOnClickListener{
            viewPager.setCurrentItem(FORM_TWO)
        }

        return view
    }

}
