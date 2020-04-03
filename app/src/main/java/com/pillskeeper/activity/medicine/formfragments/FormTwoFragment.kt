package com.pillskeeper.activity.medicine.formfragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
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
    lateinit var editTextTotalQuantity: EditText
    lateinit var editTextRemainingQuantity: EditText


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_form_two, container, false)

        textViewNext = view!!.findViewById(R.id.textViewNext)
        textViewBack = view!!.findViewById(R.id.textViewBack)
        editTextTotalQuantity = view.findViewById(R.id.editTextTotalQuantity)
        editTextRemainingQuantity = view.findViewById(R.id.editTextRemainingQuantity)


        /*LISTENERS*/
        textViewNext.setOnClickListener {
            if ( Integer.parseInt(editTextRemainingQuantity.text.toString()) > Integer.parseInt(editTextTotalQuantity.text.toString()) ) {
               Toast.makeText(context, "La quantità rimanente non può essere maggiore di quella totale!", Toast.LENGTH_LONG).show()
            } else {
                FormAdapter.totalQuantity = editTextTotalQuantity.text.toString().toFloat()
                FormAdapter.remainingQuantity = editTextRemainingQuantity.text.toString().toFloat()
                viewPager.currentItem = FORM_THREE
            }
        }

        textViewBack.setOnClickListener{
            viewPager.currentItem = FORM_ONE
        }

        editTextTotalQuantity.addTextChangedListener {
            editTextRemainingQuantity.text = editTextTotalQuantity.text

            if(editTextTotalQuantity.text.isNotEmpty() && editTextRemainingQuantity.text.isNotEmpty()){
                textViewNext.visibility = View.VISIBLE
            } else {
                textViewNext.visibility = View.INVISIBLE
            }
        }

        return view
    }

}
