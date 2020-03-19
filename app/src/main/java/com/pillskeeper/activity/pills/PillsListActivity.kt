package com.pillskeeper.activity.pills

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.pillskeeper.R
import com.pillskeeper.activity.MainActivity
import com.pillskeeper.data.LocalMedicine
import com.pillskeeper.datamanager.UserInformation
import kotlinx.android.synthetic.main.activity_pills_form.*
import kotlinx.android.synthetic.main.activity_pills_list.*
import java.util.*
import kotlin.collections.ArrayList

class PillsListActivity : AppCompatActivity() {

    private lateinit var pillsArray: LinkedList<String>
    var adapter: ArrayAdapter<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pills_list)

        initList()

        pills_list.setOnItemClickListener { _: AdapterView<*>, _: View, position: Int, _: Long ->
            //TODO scrivere cosa fare sul click degli itemssss
            if (position == 0) {
                val it = Intent(this, PillsFormActivity::class.java)
                startActivityForResult(it, 0)
            } else {
                Toast.makeText(
                    applicationContext,
                    "e' stato premuto " + pillsArray[position],
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        initList()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == MainActivity.START_FIRST_LOGIN_ACTIVITY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val pillName: String = data!!.getStringExtra("pillName")
                pillsArray.add(pillName)
                adapter!!.notifyDataSetChanged()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun initList () {
        pillsArray = LinkedList()
        pillsArray.add("+ nuova medicina")

        UserInformation.medicines.forEach { entry -> pillsArray.add(entry.name) }

        adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pillsArray)
        pills_list.adapter = adapter
    }
}
