package com.pillskeeper.datamanager

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.pillskeeper.data.Friend
import com.pillskeeper.data.Medicine
import com.pillskeeper.enums.LocalDbKeyEnum
import com.pillskeeper.interfaces.LocalDatabaseInterface
import java.util.*
import kotlin.collections.ArrayList

object LocalDatabase : LocalDatabaseInterface{

    var sharedPref: SharedPreferences? = null

    fun saveValue(key: String, value: Any){
        Log.w(Log.DEBUG.toString(), "LocalDatabase: saveValue() - Started")

        with (sharedPref?.edit()) {
            when (value) {
                is Int      ->  this?.putInt(key, value)
                is String   ->  this?.putString(key, value)
                is Boolean  ->  this?.putBoolean(key, value)
                else ->  {
                    println("this info is going to be inserted: ${Gson().toJson(value)}")
                    this?.putString(key, Gson().toJson(value))
                }
            }
            this?.apply()
        }

        Log.w(Log.DEBUG.toString(), "LocalDatabase: saveValue() - Ended - Value Saved")
    }

    override fun readUsername(): String? {
        Log.w(Log.DEBUG.toString(), "LocalDatabase: readUsername() - Started")

        val username: String? = sharedPref?.getString(LocalDbKeyEnum.USERNAME.toString(), null)

        if(username.isNullOrEmpty())
            Log.w(Log.DEBUG.toString(), "LocalDatabase: readUsername() - Ended - Username empty or null")
        else
            Log.w(Log.DEBUG.toString(), "LocalDatabase: readUsername() - Ended - Username full + $username")

        return username
    }

    override fun readFriendList(): LinkedList<Friend> {
        Log.w(Log.DEBUG.toString(), "LocalDatabase: readFriendList() - Started")

        val friendsJson: String? = sharedPref?.getString(LocalDbKeyEnum.FRIENDLIST.toString(),null)

        if (friendsJson.isNullOrEmpty()) {
            Log.w(Log.DEBUG.toString(), "LocalDatabase: readFriendList() - Ended - List empty")
            return LinkedList<Friend>()
        }

        Log.w(Log.DEBUG.toString(), "LocalDatabase: readFriendList() - Ended - List full")
        return LinkedList(Gson().fromJson(friendsJson, Array<Friend>::class.java).toList())
    }

    override fun readMedicineList(): LinkedList<Medicine> {
        Log.w(Log.DEBUG.toString(), "LocalDatabase: readMedicineList() - Started")

        val medicinesJson: String? = sharedPref?.getString(LocalDbKeyEnum.MEDICINELIST.toString(), null)

        if (medicinesJson.isNullOrEmpty()) {
            Log.w(Log.DEBUG.toString(), "LocalDatabase: readMedicineList() - Ended - List empty")
            return LinkedList<Medicine>()
        }

        Log.w(Log.DEBUG.toString(), "LocalDatabase: readMedicineList() - Ended - List full")
        return LinkedList(Gson().fromJson(medicinesJson, Array<Medicine>::class.java).toList())
    }

    override fun saveMedicineList(medicine: LinkedList<Medicine>) {
        Log.w(Log.DEBUG.toString(), "LocalDatabase: saveMedicineList() - Started")

        saveValue(LocalDbKeyEnum.MEDICINELIST.toString(),medicine)

        Log.w(Log.DEBUG.toString(), "LocalDatabase: saveMedicineList() - Ended")
    }

    override fun resetMemory(){

        sharedPref?.edit()?.clear()?.commit()

    }

}