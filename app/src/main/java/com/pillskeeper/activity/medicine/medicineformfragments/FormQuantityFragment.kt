package com.pillskeeper.activity.medicine.medicineformfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.pillskeeper.R

class FormQuantityFragment(private val viewPager: MedicineViewPager) : Fragment() {

    lateinit var textViewNext: TextView
    lateinit var textViewBack: TextView
    lateinit var editTextTotalQuantity: EditText
    lateinit var editTextRemainingQuantity: EditText


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_form_two, container, false)

        textViewNext = view!!.findViewById(R.id.textViewNext)
        textViewBack = view.findViewById(R.id.textViewBack)
        editTextTotalQuantity = view.findViewById(R.id.editTextTotalQuantity)
        editTextRemainingQuantity = view.findViewById(R.id.editTextRemainingQuantity)


        /*LISTENERS*/
        textViewNext.setOnClickListener {
            if(editTextRemainingQuantity.text.toString() != "" && editTextTotalQuantity.text.toString() != "") {
                if (Integer.parseInt(editTextRemainingQuantity.text.toString()) > Integer.parseInt(
                        editTextTotalQuantity.text.toString()
                    )
                ) {
                    Toast.makeText(
                        context,
                        "La quantità rimanente non può essere maggiore di quella totale!",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    FormAdapter.totalQuantity = editTextTotalQuantity.text.toString().toFloat()
                    FormAdapter.remainingQuantity =
                        editTextRemainingQuantity.text.toString().toFloat()
                    viewPager.currentItem = FormAdapter.FORM_SAVE_OR_REMINDER
                }
            }
        }

        textViewBack.setOnClickListener{
            viewPager.currentItem = FormAdapter.FORM_NAME_TYPE
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
