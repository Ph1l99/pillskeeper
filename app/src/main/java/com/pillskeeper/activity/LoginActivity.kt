package com.pillskeeper.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.pillskeeper.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        readFirstLogin()
    }



    private fun readFirstLogin() {
        if (FirebaseAuth.getInstance().currentUser == null) {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
    }
}
