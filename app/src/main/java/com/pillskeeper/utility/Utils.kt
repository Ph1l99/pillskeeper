package com.pillskeeper.utility

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.widget.EditText
import java.util.*

object Utils {

    private val regexUsername: Regex = "[A-Za-z]{2,30}".toRegex()
    private val regexPhoneNumber : Regex = "[0-9]{10}".toRegex()
    private val regexEmail : Regex = "[A-Za-z0-9.]{2,30}.[A-Za-z0-9]{2,30}@[A-Za-z0-9]{2,30}\\.[A-Za-z]{2,10}".toRegex()

    lateinit var stdLayout: Drawable

    fun checkName(value: String) : Boolean {
        Log.i(Log.DEBUG.toString(), "Utils: checkUsername() - Check value!")

        return (value.isNotEmpty() && value.matches(regexUsername))
    }

    fun checkPhoneNumber(phoneNumber: String) : Boolean {
        Log.i(Log.DEBUG.toString(), "Utils: checkPhoneNumber() - Check value!")

        return (phoneNumber.isNotEmpty() && phoneNumber.matches(regexPhoneNumber))
    }

    fun checkEmail(email: String) : Boolean {
        Log.i(Log.DEBUG.toString(), "Utils: checkEmail() - Check value!")

        return (email.isNotEmpty() && email.matches(regexEmail))
    }

    fun colorEditText(editText: EditText, isError: Boolean = true) {
        if(isError){
            stdLayout = editText.background
            val gd  = GradientDrawable()
            gd.setColor(Color.parseColor("#00ffffff"))
            gd.setStroke(2, Color.RED)
            editText.background = gd
        } else {
            editText.background = stdLayout
        }
    }

    fun dataNormalizationLimit(date: Date): Long {
        val cal: Calendar = Calendar.getInstance()
        cal.time = date

        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)

        return cal.time.time
    }


}