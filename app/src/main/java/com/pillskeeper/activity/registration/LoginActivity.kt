package com.pillskeeper.activity.registration

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pillskeeper.R
import com.pillskeeper.activity.home.HomeActivity
import com.pillskeeper.data.User
import com.pillskeeper.datamanager.FirebaseAuthenticationManager
import com.pillskeeper.datamanager.LocalDatabase
import com.pillskeeper.interfaces.Callback
import com.pillskeeper.utility.Utils
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val PATH_USERS = "users"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailLogin.requestFocus()

        signupButton.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        loginButton.setOnClickListener {
            login()
        }
        val textView = findViewById<TextView>(R.id.resetPassordTextView)
        resetPassordTextView.setOnClickListener {
            resetPassordTextView.isClickable = false
            resetPassword()
        }
    }

    private fun resetPassword() {
        if (emailLogin.text.toString().isEmpty()) {
            Utils.buildAlertDialog(
                this@LoginActivity,
                getString(R.string.error_values),
                getString(R.string.message_title)
            ).show()
        } else {
            FirebaseAuthenticationManager.resetPassword(
                emailLogin.text.toString(),
                object : Callback {
                    override fun onSuccess(res: Boolean) {
                        Utils.buildAlertDialog(
                            this@LoginActivity,
                            getString(R.string.emailPassword),
                            getString(R.string.message_title)
                        ).show()
                    }

                    override fun onError() {
                        Utils.buildAlertDialog(
                            this@LoginActivity,
                            getString(R.string.emailPassword),
                            getString(R.string.message_title)
                        ).show()
                    }

                })
        }

    }

    /**
     * Login method
     */
    private fun login() {
        if (emailLogin.text.toString().isEmpty() || passwordLogin.text.toString().isEmpty()) {
            Utils.errorEditText(emailLogin)
            Utils.errorEditText(passwordLogin)
            Toast.makeText(this, R.string.error_login, Toast.LENGTH_LONG).show()
        } else {

            FirebaseAuthenticationManager.loginUser(emailLogin.text.toString(),
                passwordLogin.text.toString(), object : Callback {
                    override fun onSuccess(res: Boolean) {
                        FirebaseAuth.getInstance().currentUser?.uid?.let { checkUserOnLocalDB(it) }
                        startActivity(Intent(applicationContext, HomeActivity::class.java))
                        finish()
                    }

                    override fun onError() {
                        Utils.errorEditText(emailLogin)
                        Utils.errorEditText(passwordLogin)
                        Utils.buildAlertDialog(
                            this@LoginActivity,
                            getString(R.string.error_login), getString(R.string.message_title)
                        ).show()
                    }
                })
        }
    }

    private fun checkUserOnLocalDB(userId: String) {
        if (LocalDatabase.readUser() == null) {
            val databaseReference = Firebase.database.reference
            databaseReference.child(PATH_USERS).child(userId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        @Suppress("UNCHECKED_CAST")
                        LocalDatabase.saveUser(User.getUserFromMap(p0.value as Map<String, String>))
                    }

                    override fun onCancelled(p0: DatabaseError) {
                        Toast.makeText(this@LoginActivity, "Dati non scaricati", Toast.LENGTH_LONG)
                            .show()
                    }
                })
        }
    }
}
