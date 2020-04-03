package com.pillskeeper.utility

import android.app.Application
import com.pillskeeper.R
import com.pillskeeper.data.RemoteMedicine
import com.pillskeeper.data.User

data class Mail(var mailsubject: String, var mailBody: String) {

    companion object {
        fun composeMail(medicine: RemoteMedicine, user: User): Mail? {
            val mSubject: String = R.string.mailsubject.toString() + medicine.name
            val mText: String = R.string.mailtext.toString() + "aggiungere le info della medicina"
            return Mail(mSubject, mText)
        }
    }
}