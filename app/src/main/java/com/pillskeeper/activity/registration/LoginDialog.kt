package com.pillskeeper.activity.registration

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.pillskeeper.R
import com.pillskeeper.datamanager.FirebaseAuthenticationManager
import com.pillskeeper.interfaces.Callback
import com.pillskeeper.utility.Utils
import kotlinx.android.synthetic.main.dialog_login.*

class LoginDialog(context: Context, private val userEmail: String) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_login)
        displayInfos(userEmail)
        loginDialogButton.setOnClickListener {
            loginAgain()
        }
    }

    /**
     * Method for logging the user and update the token
     */
    private fun loginAgain() {
        if (loginDialogPasswordText.text != null) {
            FirebaseAuthenticationManager.reautenticateUser(
                userEmail,
                loginDialogPasswordText.text.toString(),
                object :
                    Callback {
                    override fun onSuccess(res: Boolean) {
                        dismiss()
                    }

                    override fun onError() {
                        Utils.buildAlertDialog(
                            context,
                            context.getString(R.string.loginDialogError),
                            context.getString(R.string.message_title)
                        ).show()
                    }

                })
        } else {
            Utils.errorEditText(loginDialogPasswordText)
        }

    }

    /**
     * Method for displaying infos like the email address
     */
    private fun displayInfos(userEmail: String) {
        loginDialogMailText.setText(userEmail)
        loginDialogMailText.isEnabled = false
        loginDialogPasswordText.requestFocus()
    }
}
