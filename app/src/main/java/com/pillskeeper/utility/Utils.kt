package com.pillskeeper.utility

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import com.pillskeeper.R
import com.pillskeeper.datamanager.LocalDatabase
import java.util.*

object Utils {

    private val regexUsername: Regex = "[A-Za-z]{2,30}".toRegex()
    private val regexPhoneNumber: Regex = "[0-9]{10}".toRegex()
    private val regexEmail: Regex =
        "[A-Za-z0-9.]{2,30}.[A-Za-z0-9]{2,30}@[A-Za-z0-9]{2,30}\\.[A-Za-z]{2,10}".toRegex()

    lateinit var stdLayout: Drawable

    fun checkName(value: String): Boolean {
        Log.i(Log.DEBUG.toString(), "Utils: checkUsername() - Check value!")

        return (value.isNotEmpty() && value.matches(regexUsername))
    }

    fun checkPhoneNumber(phoneNumber: String): Boolean {
        Log.i(Log.DEBUG.toString(), "Utils: checkPhoneNumber() - Check value!")

        return (phoneNumber.isNotEmpty() && phoneNumber.matches(regexPhoneNumber))
    }

    fun checkEmail(email: String): Boolean {
        Log.i(Log.DEBUG.toString(), "Utils: checkEmail() - Check value!")

        return (email.isNotEmpty() && email.matches(regexEmail))
    }

    fun checkDate(dateSelected: Date, context: Context): Boolean{
        val calCurrent = Calendar.getInstance()
        val calSelected = Calendar.getInstance()
        calCurrent.time = Date(System.currentTimeMillis())
        calSelected.time = dateSelected
        if(calCurrent.get(Calendar.YEAR) > calSelected.get(Calendar.YEAR) || calCurrent.get(Calendar.MONTH) > calSelected.get(Calendar.MONTH) ||
            calCurrent.get(Calendar.DAY_OF_YEAR) > calSelected.get(Calendar.DAY_OF_YEAR)){
            Toast.makeText(context,"Perfavore inserire una data corretta", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    fun errorEditText(editText: EditText){
        colorEditText(editText,true)
    }

    fun validEditText(editText: EditText){
        colorEditText(editText, false)
    }

    private fun colorEditText(editText: EditText, isError: Boolean) {
        if (isError) {
            stdLayout = editText.background
            val gd = GradientDrawable()
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

    fun insertNameMenu(view: NavigationView) {
        val username = view.getHeaderView(0).findViewById<TextView>(R.id.username_text_view_menu)
        username.text = LocalDatabase.readUser()?.name
    }


}