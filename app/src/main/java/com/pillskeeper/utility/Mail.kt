package com.pillskeeper.utility

import com.pillskeeper.R
import com.pillskeeper.data.RemoteMedicine
import com.pillskeeper.data.User
import com.pillskeeper.datamanager.UserInformation.context

/**
 * Data class for sending emails
 */
data class Mail(var mailsubject: String, var mailBody: String) {

    companion object {
        fun composeMail(medicine: RemoteMedicine, user: User): Mail? {
            val mSubject: String = context.getString(R.string.mailsubject) + medicine.name
            val sb = StringBuilder()
            sb.append(context.getString(R.string.mailtext)).append("\n")
                .append(context.getString(R.string.medicine)).append(": ").append(medicine.name).append("\n")
                .append(context.getString(R.string.medType)).append(": ").append(medicine.medicineType).append("\n")
                .append(context.getString(R.string.user)).append(": ").append(user.name + " " + user.surname).append("\n")
                .append(context.getString(R.string.status))
            return Mail(mSubject, sb.toString())
        }
    }
}