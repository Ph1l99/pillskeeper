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

    private fun saveValue(key: String, value: Any){
        Log.w(Log.DEBUG.toString(), "LocalDatabase: saveValue() - Started")

        with (sharedPref?.edit()) {
            when (value) {
                is Int      ->  this?.putInt(key, value)
                is String   ->  this?.putString(key, value)
                is Boolean  ->  this?.putBoolean(key, value)
                else        ->  this?.putString(key, Gson().toJson(value))
            }
            this?.apply()
        }

        Log.w(Log.DEBUG.toString(), "LocalDatabase: saveValue() - Ended - Value Saved")
    }


    /*  READ Function   */

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


    /*  SAVE Function   */

    override fun saveUsername(username: String){
        Log.w(Log.DEBUG.toString(), "LocalDatabase: saveUsername() - Started")

        saveValue(LocalDbKeyEnum.USERNAME.toString(), username)

        Log.w(Log.DEBUG.toString(), "LocalDatabase: saveUsername() - Started")
    }

    override fun saveFriendList(friends: LinkedList<Friend>){
        Log.w(Log.DEBUG.toString(), "LocalDatabase: saveFriendList() - Started")

        saveValue(LocalDbKeyEnum.FRIENDLIST.toString(),friends)

        Log.w(Log.DEBUG.toString(), "LocalDatabase: saveFriendList() - Started")
    }

    override fun saveFriendList(){
        Log.w(Log.DEBUG.toString(), "LocalDatabase: saveFriendList() - Started")

        saveValue(LocalDbKeyEnum.FRIENDLIST.toString(),UserInformation.friends)

        Log.w(Log.DEBUG.toString(), "LocalDatabase: saveFriendList() - Started")
    }

    override fun saveMedicineList(medicine: LinkedList<Medicine>) {
        Log.w(Log.DEBUG.toString(), "LocalDatabase: saveMedicineList() - Started")

        saveValue(LocalDbKeyEnum.MEDICINELIST.toString(),medicine)

        Log.w(Log.DEBUG.toString(), "LocalDatabase: saveMedicineList() - Ended")
    }

    override fun saveMedicineList() {
        Log.w(Log.DEBUG.toString(), "LocalDatabase: saveMedicineList() - Started")

        saveValue(LocalDbKeyEnum.MEDICINELIST.toString(),UserInformation.medicines)

        Log.w(Log.DEBUG.toString(), "LocalDatabase: saveMedicineList() - Ended")
    }

    //TODO TEST
    override fun resetMemory(){

        sharedPref?.edit()?.clear()?.commit()

    }



}