package com.pillskeeper.datamanager

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.pillskeeper.data.Appointment
import com.pillskeeper.data.Friend
import com.pillskeeper.data.LocalMedicine
import com.pillskeeper.data.User
import com.pillskeeper.enums.LocalDbKeyEnum
import java.util.*

object LocalDatabase {

    var sharedPref: SharedPreferences? = null

    private fun saveValue(key: String, value: Any) {
        Log.i(Log.DEBUG.toString(), "LocalDatabase: saveValue() - Started")

        with(sharedPref?.edit()) {
            when (value) {
                is Int -> this?.putInt(key, value)
                is String -> this?.putString(key, value)
                is Boolean -> this?.putBoolean(key, value)
                else -> this?.putString(key, Gson().toJson(value))
            }
            this?.apply()
        }

        Log.i(Log.DEBUG.toString(), "LocalDatabase: saveValue() - Ended - Value Saved")
    }



    /*  READ Function   */

    fun readUser(): User? {
        Log.i(Log.DEBUG.toString(), "LocalDatabase: readUser() - Started")

        val userString: String? = sharedPref?.getString(LocalDbKeyEnum.USER.toString(), null)

        return if (userString.isNullOrEmpty()) {
            Log.i(
                Log.DEBUG.toString(),
                "LocalDatabase: readUser() - Ended - Username empty or null"
            )
            null
        } else {
            Log.i(
                Log.DEBUG.toString(),
                "LocalDatabase: readUser() - Ended"
            )
            Gson().fromJson(userString, User::class.java)
        }
    }

    fun readFriendList(): LinkedList<Friend> {
        Log.i(Log.DEBUG.toString(), "LocalDatabase: readFriendList() - Started")

        val friendsJson: String? = sharedPref?.getString(LocalDbKeyEnum.FRIENDLIST.toString(), null)

        if (friendsJson.isNullOrEmpty()) {
            Log.i(Log.DEBUG.toString(), "LocalDatabase: readFriendList() - Ended - List empty")
            return LinkedList<Friend>()
        }

        Log.i(Log.DEBUG.toString(), "LocalDatabase: readFriendList() - Ended - List full")
        return LinkedList(Gson().fromJson(friendsJson, Array<Friend>::class.java).toList())
    }

    fun readMedicineList(): LinkedList<LocalMedicine> {
        Log.i(Log.DEBUG.toString(), "LocalDatabase: readMedicineList() - Started")

        val medicinesJson: String? =
            sharedPref?.getString(LocalDbKeyEnum.MEDICINELIST.toString(), null)

        if (medicinesJson.isNullOrEmpty()) {
            Log.i(Log.DEBUG.toString(), "LocalDatabase: readMedicineList() - Ended - List empty")
            return LinkedList<LocalMedicine>()
        }

        Log.i(Log.DEBUG.toString(), "LocalDatabase: readMedicineList() - Ended - List full")
        return LinkedList(Gson().fromJson(medicinesJson, Array<LocalMedicine>::class.java).toList())
    }

    fun readAppointmentList(): LinkedList<Appointment> {
        Log.i(Log.DEBUG.toString(), "LocalDatabase: readAppointmentList() - Started")

        val appointmentJson: String? =
            sharedPref?.getString(LocalDbKeyEnum.APPOINTMENTLIST.toString(), null)

        if (appointmentJson.isNullOrEmpty()) {
            Log.i(Log.DEBUG.toString(), "LocalDatabase: readAppointmentList() - Ended - List empty")
            return LinkedList<Appointment>()
        }

        Log.i(Log.DEBUG.toString(), "LocalDatabase: readAppointmentList() - Ended - List full")
        return LinkedList(Gson().fromJson(appointmentJson, Array<Appointment>::class.java).toList())
    }



    /*  SAVE Function   */

    fun saveUser(user: User) {
        Log.i(Log.DEBUG.toString(), "LocalDatabase: saveUser() - Started")
        saveValue(LocalDbKeyEnum.USER.toString(), user)
        Log.i(Log.DEBUG.toString(), "LocalDatabase: saveUser() - Started")
    }

    fun saveFriendList() {
        Log.i(Log.DEBUG.toString(), "LocalDatabase: saveFriendList() - Started")

        saveValue(LocalDbKeyEnum.FRIENDLIST.toString(), UserInformation.friends)

        Log.i(Log.DEBUG.toString(), "LocalDatabase: saveFriendList() - Started")
    }

    fun saveMedicineList() {
        Log.i(Log.DEBUG.toString(), "LocalDatabase: saveMedicineList() - Started")

        saveValue(LocalDbKeyEnum.MEDICINELIST.toString(), UserInformation.medicines)

        Log.i(Log.DEBUG.toString(), "LocalDatabase: saveMedicineList() - Ended")
    }

    fun saveAppointmentList() {
        Log.i(Log.DEBUG.toString(), "LocalDatabase: saveAppointmentList() - Started")

        saveValue(LocalDbKeyEnum.APPOINTMENTLIST.toString(), UserInformation.appointments)

        Log.i(Log.DEBUG.toString(), "LocalDatabase: saveAppointmentList() - Ended")
    }
}