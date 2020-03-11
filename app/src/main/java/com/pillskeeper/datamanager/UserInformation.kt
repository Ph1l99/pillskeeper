package com.pillskeeper.datamanager

import android.util.Log
import com.pillskeeper.data.Friend
import com.pillskeeper.data.Medicine
import java.util.*

object UserInformation {

    var medicines: LinkedList<Medicine> = LocalDatabase.readMedicineList()
    var friends: LinkedList<Friend> = LocalDatabase.readFriendList()

    @Synchronized fun getSpecificMedicine(name: String): Medicine?{
        Log.w(Log.DEBUG.toString(),"UserInformation: getSpecificMedicine() - Started")
        
        medicines.forEach {
            entry -> if (entry.name == name){
                Log.w(Log.DEBUG.toString(),"UserInformation: getSpecificMedicine() - Ended - Medicine found")
                return entry
            }
        }

        Log.w(Log.DEBUG.toString(),"UserInformation: getSpecificMedicine() - Ended - Medicine not found")
        return null
    }

    @Synchronized fun getSpecificFriend(name: String)  : Friend? {
        Log.w(Log.DEBUG.toString(),"UserInformation: getSpecificFriend() - Started")

        friends.forEach {
            entry -> if(entry.name == name) {
                Log.w(Log.DEBUG.toString(),"UserInformation: getSpecificFriend() - Ended - Friend found")
                return entry
            }
        }

        Log.w(Log.DEBUG.toString(),"UserInformation: getSpecificFriend() - Ended - Friend not found")
        return null
    }

    @Synchronized fun addNewFriend(friend: Friend): Boolean {
        Log.w(Log.DEBUG.toString(),"UserInformation: addNewFriend() - Started")
        friends.forEach {
            entry -> if(entry.name == friend.name){
                Log.w(Log.DEBUG.toString(),"UserInformation: addNewFriend() - Ended - Friend already present")
                return false
            }
        }

        friends.add(friend)

        Log.w(Log.DEBUG.toString(),"UserInformation: addNewFriend() - Ended - Friend inserted")
        return true
    }

    @Synchronized fun addNewMedicine(medicine: Medicine) : Boolean{
        Log.w(Log.DEBUG.toString(),"UserInformation: addNewMedicine() - Started")

        medicines.forEach {
            entry -> if(entry.name == medicine.name){
                Log.w(Log.DEBUG.toString(),"UserInformation: addNewMedicine() - Ended - Medicine already present")
                return false
            }
        }

        medicines.add(medicine)

        Log.w(Log.DEBUG.toString(),"UserInformation: addNewMedicine() - Ended - Medicine inserted")
        return true
    }

    @Synchronized fun editMedicine(oldName: String, medicine: Medicine): Boolean{
        Log.w(Log.DEBUG.toString(),"UserInformation: editMedicine() - Started")

        for(i in medicines.indices){
            if(medicines[i].name == oldName){
                Log.w(Log.DEBUG.toString(),"UserInformation: editMedicine() - Ended - Medicine modified")
                medicines[i] = medicine
                return true
            }
        }

        Log.w(Log.DEBUG.toString(),"UserInformation: editMedicine() - Ended - Medicine not found")
        return false
    }

    @Synchronized fun editFriend(oldName: String, friend: Friend): Boolean{
        Log.w(Log.DEBUG.toString(),"UserInformation: editFriend() - Started")

        for(i in friends.indices){
            if(friends[i].name == oldName){
                Log.w(Log.DEBUG.toString(),"UserInformation: editFriend() - Ended - Friend modified")
                friends[i] = friend
                return true
            }
        }

        Log.w(Log.DEBUG.toString(),"UserInformation: editFriend() - Ended - Friend not found")
        return false
    }

}