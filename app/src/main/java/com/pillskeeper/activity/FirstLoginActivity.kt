package com.pillskeeper.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.pillskeeper.R
import com.pillskeeper.datamanager.AuthenticationManager
import com.pillskeeper.datamanager.CentralDatabase
import com.pillskeeper.datamanager.LocalDatabase
import com.pillskeeper.enums.LocalDbKeyEnum
import kotlinx.android.synthetic.main.activity_first_login.*

class FirstLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_login)
        CentralDatabase.obtainRemoteDatabase()
        AuthenticationManager.obtainAuthentication()

        firstLoginButton.setOnClickListener {
            val username: String = usernameEditText.text.toString()
            if(username.isEmpty() || username == ""){
                Toast.makeText(this, "Perfavore inserisci valori corretti!", Toast.LENGTH_LONG).show()
            } else {
                LocalDatabase.saveValue(LocalDbKeyEnum.USERNAME.toString(),username)

                sendDataBackToPreviousActivity(username)
                finish()
            }
        }
    }

    private fun sendDataBackToPreviousActivity(username: String) {
        val intent = Intent().apply {
            putExtra("username", username)
        }
        setResult(Activity.RESULT_OK, intent)
    }
}
