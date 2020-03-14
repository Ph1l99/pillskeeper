package com.pillskeeper.activity.pills

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.pillskeeper.R
import kotlinx.android.synthetic.main.activity_pills_list.*

class PillsListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pills_list)

        val pillsListView = findViewById<ListView>(R.id.pillsListView)
        val pillsArray: ArrayList<String> = ArrayList()
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pillsArray)
        pillsListView.adapter = adapter


        new_line_button.setOnClickListener{
            val it = Intent(this, PillsFormActivity::class.java)
            startActivity(it)
        }

        val bundle: Bundle? = intent.extras

        //creo una nuova linea che contiene il farmaco appena aggiunto
        if(bundle != null) {
            val pillName: String = bundle!!.getString("pillName").toString()
            val pillQuantity: String = bundle!!.getString("pillQuantity").toString()
            pillsArray.add(pillName)
            (pillsListView.adapter as ArrayAdapter<String>).notifyDataSetChanged()
        }

        pillsListView.setOnItemClickListener{ adapterView: AdapterView<*>, view1: View, position: Int, l: Long ->
            //TODO scrivere cosa fare sul click degli itemssss
            Toast.makeText(applicationContext, "e' stato premuto "+pillsArray[position], Toast.LENGTH_LONG ).show()
        }
    }
}
