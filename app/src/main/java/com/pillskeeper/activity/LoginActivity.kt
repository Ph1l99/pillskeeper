package com.pillskeeper.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.pillskeeper.R
import com.pillskeeper.utility.Utils
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.annotations.NotNull

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        checkLogin()

        signupButton.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
            checkLogin()
        }
        loginButton.setOnClickListener {
            login()
        }
    }

    private fun login() {
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(emailLogin.text.toString(), passwordLogin.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Utils.colorEditText(emailLogin)
                    Utils.colorEditText(passwordLogin)
                    Toast.makeText(this, R.string.error_login, Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun checkLogin() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
