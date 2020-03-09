package com.pillskeeper.datamanager

import android.content.SharedPreferences
import com.pillskeeper.enums.LocalDbKeyEnum

object LocalDatabase {

    var sharedPref: SharedPreferences? = null

    fun saveValue(key: String, value: Any){
        with (sharedPref?.edit()) {
            when (value) {
                is Int -> this?.putInt(key, value)
                is String -> this?.putString(key, value)
                is Boolean -> this?.putBoolean(key, value)
            }

            this?.commit()
        }
    }


    fun readUsername(): String? {

        return sharedPref?.getString(LocalDbKeyEnum.USERNAME.toString(), null)

    }


}