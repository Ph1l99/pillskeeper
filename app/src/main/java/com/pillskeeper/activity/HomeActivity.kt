package com.pillskeeper.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pillskeeper.R
import com.pillskeeper.activity.pills.PillsListActivity
import com.pillskeeper.activity.pills.TextReaderActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        pills_button.setOnClickListener{
            val it = Intent(this, PillsListActivity::class.java)
            startActivity(it)
        }
    }
}
