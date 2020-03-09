package com.pillskeeper.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.pillskeeper.R
import kotlinx.android.synthetic.main.activity_first_login.*

class FirstLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_login)

        firstLoginButton.setOnClickListener {
            var username = textviewFirstLogin.text.toString()
            if(username.isEmpty() || username == ""){
                Toast.makeText(this, "Perfavore inserisci valori corretti!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else {

                finish()
            }
        }
    }


}
