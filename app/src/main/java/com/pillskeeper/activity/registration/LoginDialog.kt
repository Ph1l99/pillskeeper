package com.pillskeeper.activity.registration

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.pillskeeper.R
import com.pillskeeper.datamanager.FirebaseAuthenticationManager
import com.pillskeeper.interfaces.Callback
import com.pillskeeper.utility.Utils
import kotlinx.android.synthetic.main.dialog_login.*

/**
 * LoginDialog for re-authenticating a user in order to change personal information on Firebase
 */
class LoginDialog(context: Context, private val userEmail: String, val callback: Callback) :
    Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_login)
        displayInfos(userEmail)
        loginDialogButton.setOnClickListener {
            loginAgain()

        }
    }

    //Method for loggin in the user and refresh the authentication token
    private fun loginAgain() {
        if (loginDialogPasswordText.text != null) {
            FirebaseAuthenticationManager.reautenticateUser(
                userEmail,
                loginDialogPasswordText.text.toString(),
                object :
                    Callback {
                    override fun onSuccess(res: Boolean) {
                        callback.onSuccess(true)
                        dismiss()
                    }

                    override fun onError() {
                        callback.onError()
                        Utils.buildAlertDialog(
                            context,
                            context.getString(R.string.loginDialogError),
                            context.getString(R.string.message_title)
                        ).show()
                    }

                })
        } else {
            callback.onError()
            Utils.errorEditText(loginDialogPasswordText)
        }

    }

    //Method for displaying infos like email
    private fun displayInfos(userEmail: String) {
        loginDialogMailText.setText(userEmail)
        loginDialogMailText.isEnabled = false
        loginDialogPasswordText.requestFocus()
    }
}
