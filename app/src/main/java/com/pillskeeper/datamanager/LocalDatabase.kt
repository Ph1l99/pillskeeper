package com.pillskeeper.datamanager

import android.content.ClipData
import android.content.SharedPreferences
import com.google.gson.Gson
import com.pillskeeper.data.Friend
import com.pillskeeper.data.Medicine
import com.pillskeeper.enums.LocalDbKeyEnum

object LocalDatabase {

    var sharedPref: SharedPreferences? = null

    fun saveValue(key: String, value: Any){
        with (sharedPref?.edit()) {
            when (value) {
                is Int      ->  this?.putInt(key, value)
                is String   ->  this?.putString(key, value)
                is Boolean  ->  this?.putBoolean(key, value)
                is Medicine ->  this?.putString(value.name, Gson().toJson(value))
            }
            this?.apply()
        }
    }

    fun readUsername(): String? {

        return sharedPref?.getString(LocalDbKeyEnum.USERNAME.toString(), null)

    }

    fun readFriendList(): ArrayList<Friend> {

        return Gson().fromJson(sharedPref?.getString(LocalDbKeyEnum.FRIENDLIST.toString(), null), Array<Friend>::class.java).toList() as ArrayList<Friend>

    }

    fun readMedicineList(): ArrayList<Medicine> {

        return Gson().fromJson(sharedPref?.getString(LocalDbKeyEnum.MEDICINELIST.toString(), null), Array<Medicine>::class.java).toList() as ArrayList<Medicine>

    }

    fun resetMemory(){

        sharedPref?.edit()?.clear()?.apply()

    }

}