package com.pillskeeper.utility

import android.util.Log

object Utils {

    private var regexUsername: Regex = "[A-Za-z]{2,30}".toRegex()
    private var regexPhoneNumber : Regex = "[0-9]{10}".toRegex()
    private var regexEmail : Regex = "[A-Za-z]{2,30}@[A-Za-z]{2,30}.[A-Za-z]{2,10}".toRegex()


    fun checkName(value: String) : Boolean {
        Log.w(Log.DEBUG.toString(), "Utils: checkUsername() - Check value!")

        return (value.isNotEmpty() && value.matches(regexUsername))
    }

    fun checkPhoneNumber(phoneNumber: String) : Boolean {
        Log.w(Log.DEBUG.toString(), "Utils: checkPhoneNumber() - Check value!")

        return (phoneNumber.isNotEmpty() && phoneNumber.matches(regexPhoneNumber))
    }

    fun checkEmail(email: String) : Boolean {
        Log.w(Log.DEBUG.toString(), "Utils: checkEmail() - Check value!")

        return (email.isNotEmpty() && email.matches(regexEmail))
    }

}