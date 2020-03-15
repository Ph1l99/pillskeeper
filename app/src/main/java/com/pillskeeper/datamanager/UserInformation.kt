package com.pillskeeper.datamanager

import android.util.Log
import com.pillskeeper.data.Friend
import com.pillskeeper.data.LocalMedicine
import com.pillskeeper.data.AbstractMedicine
import com.pillskeeper.data.Reminder
import java.util.*

object UserInformation {

    var medicines: LinkedList<LocalMedicine> = LocalDatabase.readMedicineList()
    var friends: LinkedList<Friend> = LocalDatabase.readFriendList()

    @Synchronized fun getSpecificMedicine(name: String): LocalMedicine?{
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

    @Synchronized fun addNewMedicine(medicine: LocalMedicine) : Boolean{
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

    @Synchronized fun editMedicine(oldName: String, medicine: LocalMedicine): Boolean{
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

    @Synchronized fun addNewReminder(medicineName: String, reminder: Reminder): Boolean {
        Log.w(Log.DEBUG.toString(),"UserInformation: addNewReminder() - Started")

        var currMedicine: LocalMedicine? = null

        for(i in medicines.indices) {
            if(medicines[i].name == medicineName) {
                currMedicine = medicines[i]
                break
            }
        }

        if(currMedicine == null){
            Log.w(Log.DEBUG.toString(),"UserInformation: addNewReminder() - Ended - Medicine not found")
            return false
        }

        currMedicine.reminders?.forEach { it ->
            if(it.days == reminder.days && it.duration == reminder.duration &&
                it.hours == reminder.hours && it.minutes == reminder.minutes && it.number_pills == reminder.number_pills){
                Log.w(Log.DEBUG.toString(),"UserInformation: addNewReminder() - Ended - Reminder with same Info")
                return false
            }
        }

        currMedicine.reminders?.add(reminder)

        Log.w(Log.DEBUG.toString(),"UserInformation: addNewReminder() - Ended - Reminder inserted")
        return true
    }

    fun addNewReminderList(medicineName: String, reminderList: LinkedList<Reminder>){
        Log.w(Log.DEBUG.toString(),"UserInformation: addNewReminderList() - Started")

        reminderList.forEach { entry -> addNewReminder(medicineName, entry) }

        Log.w(Log.DEBUG.toString(),"UserInformation: addNewReminderList() - Ended")
    }

    @Synchronized fun flushAll(){
        Log.w(Log.DEBUG.toString(),"UserInformation: flushAll() - Started")

        LocalDatabase.saveFriendList(friends)
        LocalDatabase.saveMedicineList(medicines)

        Log.w(Log.DEBUG.toString(),"UserInformation: flushAll() - Ended")
    }

}