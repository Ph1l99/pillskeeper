package com.pillskeeper.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.pillskeeper.R
import com.pillskeeper.datamanager.LocalDatabase
import com.pillskeeper.utility.Utils
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        LocalDatabase.sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        checkLogin()

        signupButton.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
        }
        loginButton.setOnClickListener {
            login()
        }
    }

    private fun login() {
        if (emailLogin.text.toString().isEmpty() || passwordLogin.text.toString().isEmpty()) {
            Utils.colorEditText(emailLogin)
            Utils.colorEditText(passwordLogin)
            Toast.makeText(this, R.string.error_login, Toast.LENGTH_LONG).show()
        } else {
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(
                    emailLogin.text.toString(),
                    passwordLogin.text.toString()
                )
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
    }

    private fun checkLogin() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.addOnSuccessListener {
                if (it.token != null) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    FirebaseAuth.getInstance().signOut()
                }
            }
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                //for check internet over Bluetooth
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }

    override fun onResume() {
        super.onResume()
        checkLogin()
    }
}
