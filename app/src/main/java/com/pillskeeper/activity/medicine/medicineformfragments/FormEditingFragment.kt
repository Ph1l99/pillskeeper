package com.pillskeeper.activity.medicine.medicineformfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.pillskeeper.R
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.utility.Utils

class FormEditingFragment : Fragment() {

    private lateinit var textViewSave: TextView
    private lateinit var textViewAbort: TextView
    private lateinit var textViewMedName: TextView
    private lateinit var editTextTotalQtyEdit: EditText
    private lateinit var editTextRemainingQtyEdit: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_form_editing, container, false)

        textViewSave = view.findViewById(R.id.textViewSave)
        textViewAbort = view.findViewById(R.id.textViewAbort)

        textViewMedName = view.findViewById(R.id.textViewMedName)
        editTextTotalQtyEdit = view.findViewById(R.id.editTextTotalQtyEdit)
        editTextRemainingQtyEdit = view.findViewById(R.id.editTextRemainingQtyEdit)

        textViewMedName.text = FormAdapter.localMedicine?.name
        editTextTotalQtyEdit.setText(FormAdapter.localMedicine?.totalQty.toString())
        editTextRemainingQtyEdit.setText(FormAdapter.localMedicine?.remainingQty.toString())

        textViewSave.setOnClickListener {
            val totQty = editTextTotalQtyEdit.text.toString().toFloat()
            val remQty = editTextRemainingQtyEdit.text.toString().toFloat()

            if (remQty > 0F && remQty <= totQty) {
                FormAdapter.localMedicine!!.totalQty = totQty
                FormAdapter.localMedicine!!.remainingQty = remQty
                if (!UserInformation.editMedicine(
                        FormAdapter.localMedicine!!.name,
                        FormAdapter.localMedicine!!
                    )
                ) {
                    Utils.buildAlertDialog(
                        UserInformation.context,
                        UserInformation.context.getString(R.string.valuesNotChanged)
                    ).show()
                }

                FormAdapter.closeForm()
            } else {
                Toast.makeText(
                    UserInformation.context,
                    UserInformation.context.getString(R.string.genericInfoError),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        textViewAbort.setOnClickListener {
            FormAdapter.closeForm()
        }

        return view
    }
}