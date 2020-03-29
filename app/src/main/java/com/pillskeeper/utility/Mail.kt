package com.pillskeeper.utility

import com.pillskeeper.R
import com.pillskeeper.data.Friend
import com.pillskeeper.data.RemoteMedicine
import com.pillskeeper.data.User

data class Mail(var mailto: String, var mailsubject: String, var mailBody: String) {

    companion object {
        fun composeMail(medicine: RemoteMedicine, user: User, friend: Friend): Mail? {
            val mSubject: String = R.string.mailsubject.toString() + medicine.name
            val mText: String = R.string.mailtext.toString() + "aggiungere le info della medicina"
            return friend.email?.let { Mail(it, mSubject, mText) }
        }
    }
}