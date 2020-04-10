package com.pillskeeper.activity.medicine.medicineformfragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.pillskeeper.R
import com.pillskeeper.activity.medicine.MedicineLocaleListActivity
import com.pillskeeper.activity.medicine.ReminderChooseDialog
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.utility.Utils
import java.util.*

class FormSaveOrReminderFragment(private val viewPager: MedicineViewPager) : Fragment() {

    private lateinit var textViewBack: TextView
    private lateinit var textViewConfirm: TextView
    private lateinit var buttonAddReminder: Button
    private lateinit var reminderListFragment: ListView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_form_three, container, false)

        textViewBack = view.findViewById(R.id.textViewBack)
        textViewConfirm = view.findViewById(R.id.textViewConfirm)
        buttonAddReminder = view.findViewById(R.id.buttonAddReminder)
        reminderListFragment = view.findViewById(R.id.reminderListFragment)

        textViewBack.setOnClickListener {
            viewPager.currentItem = FormAdapter.FORM_QUANTITY
        }

        textViewConfirm.setOnClickListener {
            if (Utils.checkTextWords(
                    FormAdapter.pillName.toString(),
                    Locale.getDefault().displayLanguage
                )
            ) {
                Toast.makeText(UserInformation.context, R.string.toxicWords, Toast.LENGTH_LONG)
                    .show()
            } else {
                FormAdapter.addNewMedicine()
                val intent = Intent(context, MedicineLocaleListActivity::class.java)
                startActivity(intent)
                FormAdapter.closeForm()
            }
        }

        buttonAddReminder.setOnClickListener {
            ReminderChooseDialog(FormAdapter.formActivity!!, viewPager).show()
        }

        return view
    }

    override fun onResume() {
        super.onResume()

        initList()
    }

    private fun initList() {
        val stringList = LinkedList<String>()

        FormAdapter.reminderList?.forEach {
            if (it.startingDay == it.expireDate)
                stringList.add("${it.hours}:${it.minutes} - ${it.startingDay}")
            else
                stringList.add("${it.hours}:${it.minutes} - ${it.dayStringify()}")
        }

        reminderListFragment.adapter =
            ArrayAdapter(
                FormAdapter.formActivity!!,
                android.R.layout.simple_list_item_1,
                stringList
            )
    }
}



