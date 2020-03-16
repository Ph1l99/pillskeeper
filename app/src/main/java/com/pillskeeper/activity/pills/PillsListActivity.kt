package com.pillskeeper.activity.pills

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.pillskeeper.R
import com.pillskeeper.data.LocalMedicine
import com.pillskeeper.datamanager.UserInformation
import kotlinx.android.synthetic.main.activity_pills_list.*
import java.util.*
import kotlin.collections.ArrayList

class PillsListActivity : AppCompatActivity() {

    var pillsArray: ArrayList<String>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pills_list)

        pillsArray?.add("+ new line")
        pills_list.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pillsArray!!)

        pills_list.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, position: Int, l: Long ->
            //TODO scrivere cosa fare sul click degli itemssss
            if (position == 0) {
                val it = Intent(this, PillsFormActivity::class.java)
                startActivityForResult(it, 0)
            } else {
                Toast.makeText(
                    applicationContext,
                    "e' stato premuto " + pillsArray!![position],
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
