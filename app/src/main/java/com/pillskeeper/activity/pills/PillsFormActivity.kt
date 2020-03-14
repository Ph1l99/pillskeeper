package com.pillskeeper.activity.pills

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.widget.EditText
import com.pillskeeper.R
import kotlinx.android.synthetic.main.activity_pills_form.*

class PillsFormActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pills_form)
        //TODO finire il form

        buttonCamera.setOnClickListener{
            val it = Intent(this, TextReaderActivity::class.java)
            startActivity(it)
        }

        buttonConfirm.setOnClickListener{
            val it = Intent(this, PillsListActivity::class.java)
            it.putExtra("pillName", editTextName.text.toString())
            it.putExtra("pillQuantity", editTextQuantity.text.toString())
            startActivity(it)
        }


        val bundle: Bundle? = intent.extras

        if(bundle != null) {
            val pillName = bundle!!.getString("pillName")
            editTextName.text = SpannableStringBuilder("")
            editTextName.text = SpannableStringBuilder(pillName)
        }
    }
}
