package com.pillskeeper.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.pillskeeper.R
import com.pillskeeper.activity.registration.LoginActivity
import com.pillskeeper.activity.registration.LoginDialog
import com.pillskeeper.datamanager.FirebaseAuthenticationManager
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.interfaces.Callback
import com.pillskeeper.utility.Utils
import kotlinx.android.synthetic.main.activity_danger.*

/**
 * DangerActivity that allows users delete their account
 */
class DangerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_danger)

        deleteAccount.setOnClickListener {
            deleteAccount()
        }
    }

    /**
     * Method for deleting the user account
     */
    private fun deleteAccount() {
        var closedOK = false
        val uid = FirebaseAuthenticationManager.getCurrentUser()?.uid
        val email = FirebaseAuthenticationManager.getCurrentUser()?.email
        if (email != null) {
            val dialog = LoginDialog(this, email, object : Callback {
                override fun onSuccess(res: Boolean) {
                    closedOK = true
                }

                override fun onError() {
                    closedOK = false
                }

            })
            dialog.setOnDismissListener {
                if (closedOK) {
                    val map = HashMap<String, String?>()
                    map["userid"] = uid

                    //Calling the Firebase Function named deleteUser
                    Firebase.functions.getHttpsCallable("deleteUser")
                        .call(map)
                    FirebaseAuthenticationManager.signOut()
                    UserInformation.erase()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    Utils.buildAlertDialog(
                        this,
                        getString(R.string.oops),
                        getString(R.string.message_title)
                    ).show()
                }

            }
            dialog.show()
        }
    }
}
