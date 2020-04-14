package com.pillskeeper.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.pillskeeper.R
import com.pillskeeper.activity.registration.LoginActivity
import com.pillskeeper.datamanager.FirebaseAuthenticationManager
import kotlinx.android.synthetic.main.activity_danger.*

class DangerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_danger)

        deleteAccount.setOnClickListener {
            deleteAccount()
        }
        resetPassword.setOnClickListener {

        }
    }

    private fun deleteAccount() {
        val uid = FirebaseAuthenticationManager.getCurrentUser()?.uid
        val map = HashMap<String, String?>()
        map["userid"] = uid

        Firebase.functions.getHttpsCallable("deleteUser")
            .call(map)
        FirebaseAuthenticationManager.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun resetPassword() {

    }
}
