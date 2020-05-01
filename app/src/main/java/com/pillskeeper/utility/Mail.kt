package com.pillskeeper.utility

import com.pillskeeper.R
import com.pillskeeper.data.RemoteMedicine
import com.pillskeeper.data.User
import com.pillskeeper.datamanager.UserInformation.context

data class Mail(var mailsubject: String, var mailBody: String) {

    companion object {
        fun composeMail(medicine: RemoteMedicine, user: User): Mail? {
            val mSubject: String = context.getString(R.string.mailsubject) + medicine.name //TODO
            val mText: String =
                context.getString(R.string.mailtext) + "Medicina: " + medicine.name + "\nTiplogia: " + medicine.medicineType + "\nUtente: " + user.name + " " + user.surname
            return Mail(mSubject, mText)
        }
    }
}