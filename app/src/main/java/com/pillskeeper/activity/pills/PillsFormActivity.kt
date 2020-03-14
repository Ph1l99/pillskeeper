package com.pillskeeper.activity.pills

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.widget.EditText
import com.pillskeeper.R
import com.pillskeeper.activity.MainActivity
import kotlinx.android.synthetic.main.activity_pills_form.*

class PillsFormActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pills_form)
        //TODO finire il form

        buttonCamera.setOnClickListener{
            val it = Intent(this, TextReaderActivity::class.java)
            startActivityForResult(it, 0)
        }

        buttonConfirm.setOnClickListener{
            val it = Intent(this, PillsListActivity::class.java)
            it.putExtra("pillName", editTextName.text.toString())
            it.putExtra("pillQuantity", editTextQuantity.text.toString())
            setResult(Activity.RESULT_OK, it)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == MainActivity.START_FIRST_LOGIN_ACTIVITY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val pillName: String = data!!.getStringExtra("pillName")
                editTextName.text = SpannableStringBuilder("")
                editTextName.text = SpannableStringBuilder(pillName)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
