package com.pillskeeper.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pillskeeper.R
import kotlinx.android.synthetic.main.activity_danger.*

class DangerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_danger)

        deleteAccount.setOnClickListener {

        }
        resetPassword.setOnClickListener {

        }
    }

    private fun deleteAccount() {

    }

    private fun resetPassword() {

    }
}
