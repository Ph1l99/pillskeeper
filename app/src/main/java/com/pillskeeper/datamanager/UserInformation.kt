package com.pillskeeper.datamanager

import android.content.Context
import android.util.Log
import com.pillskeeper.data.*
import java.util.*

object UserInformation {

    var medicines: LinkedList<LocalMedicine> = LocalDatabase.readMedicineList()
    var friends: LinkedList<Friend> = LocalDatabase.readFriendList()
    var appointments: LinkedList<Appointment> = LocalDatabase.readAppointmentList()

    lateinit var context: Context


    /*******  GET FUNCTION   *******/

    @Synchronized fun getSpecificMedicine(name: String): LocalMedicine?{
        Log.i(Log.DEBUG.toString(),"UserInformation: getSpecificMedicine() - Started")
        
        medicines.forEach {
            entry -> if (entry.name == name){
                Log.i(Log.DEBUG.toString(),"UserInformation: getSpecificMedicine() - Ended - Medicine found")
                return entry
            }
        }

        Log.i(Log.DEBUG.toString(),"UserInformation: getSpecificMedicine() - Ended - Medicine not found")
        return null
    }

    @Synchronized fun getSpecificFriend(name: String)  : Friend? {
        Log.i(Log.DEBUG.toString(),"UserInformation: getSpecificFriend() - Started")

        friends.forEach {
            entry -> if(entry.name == name) {
                Log.i(Log.DEBUG.toString(),"UserInformation: getSpecificFriend() - Ended - Friend found")
                return entry
            }
        }

        Log.i(Log.DEBUG.toString(),"UserInformation: getSpecificFriend() - Ended - Friend not found")
        return null
    }



    /*******  ADD  FUNCTION   *******/

    @Synchronized fun addNewFriend(friend: Friend): Boolean {
        Log.i(Log.DEBUG.toString(),"UserInformation: addNewFriend() - Started")
        friends.forEach {
            entry -> if(entry.name == friend.name){
                Log.i(Log.DEBUG.toString(),"UserInformation: addNewFriend() - Ended - Friend already present")
                return false
            }
        }

        friends.add(friend)

        Log.i(Log.DEBUG.toString(),"UserInformation: addNewFriend() - Ended - Friend inserted")
        return true
    }

    @Synchronized fun addNewMedicine(medicine: LocalMedicine) : Boolean{
        Log.i(Log.DEBUG.toString(),"UserInformation: addNewMedicine() - Started")

        medicines.forEach {
            entry -> if(entry.name == medicine.name){
                Log.i(Log.DEBUG.toString(),"UserInformation: addNewMedicine() - Ended - Medicine already present")
                return false
            }
        }

        medicines.add(medicine)

        Log.i(Log.DEBUG.toString(),"UserInformation: addNewMedicine() - Ended - Medicine inserted")
        return true
    }

    @Synchronized fun addNewReminder(medicineName: String, reminder: ReminderMedicine): Boolean {
        Log.i(Log.DEBUG.toString(),"UserInformation: addNewReminder() - Started")

        var currMedicine: LocalMedicine? = null

        for(i in medicines.indices) {
            if(medicines[i].name == medicineName) {
                currMedicine = medicines[i]
                break
            }
        }

        if(currMedicine == null){
            Log.i(Log.DEBUG.toString(),"UserInformation: addNewReminder() - Ended - Medicine not found")
            return false
        }

        currMedicine.reminders?.forEach { it ->
            if(it.days == reminder.days && it.expireDate == reminder.expireDate &&
                it.hours == reminder.hours && it.minutes == reminder.minutes && it.dosage == reminder.dosage){
                Log.i(Log.DEBUG.toString(),"UserInformation: addNewReminder() - Ended - Reminder with same Info")
                return false
            }
        }

        currMedicine.reminders?.add(reminder)

        Log.i(Log.DEBUG.toString(),"UserInformation: addNewReminder() - Ended - Reminder inserted")
        return true
    }

    fun addNewReminderList(medicineName: String, reminderList: LinkedList<ReminderMedicine>){
        Log.i(Log.DEBUG.toString(),"UserInformation: addNewReminderList() - Started")

        reminderList.forEach { entry -> addNewReminder(medicineName, entry) }

        Log.i(Log.DEBUG.toString(),"UserInformation: addNewReminderList() - Ended")
    }

    fun addNewAppointment(appointment: Appointment): Boolean{
        Log.i(Log.DEBUG.toString(),"UserInformation: addNewAppointment() - Started")

        appointments.forEach {
            entry -> if(entry.name == appointment.name){
                Log.i(Log.DEBUG.toString(),"UserInformation: addNewAppointment() - Ended - Appointment already present")
                return false
            }
        }

        appointments.add(appointment)

        Log.i(Log.DEBUG.toString(),"UserInformation: addNewAppointment() - Ended - Appointment inserted")
        return true
    }



    /*******  EDIT FUNCTION   *******/

    fun editAppointment(oldName: String, appointment: Appointment): Boolean{
        Log.i(Log.DEBUG.toString(),"UserInformation: editAppointment() - Started")

        for(i in appointments.indices){
            if(appointments[i].name == oldName){
                Log.i(Log.DEBUG.toString(),"UserInformation: editAppointment() - Ended - Appointment modified")
                appointments[i] = appointment
                return true
            }
        }

        Log.i(Log.DEBUG.toString(),"UserInformation: editAppointment() - Ended - Appointment not found")
        return false
    }

    @Synchronized fun editMedicine(oldName: String, medicine: LocalMedicine): Boolean{
        Log.i(Log.DEBUG.toString(),"UserInformation: editMedicine() - Started")

        for(i in medicines.indices){
            if(medicines[i].name == oldName){
                Log.i(Log.DEBUG.toString(),"UserInformation: editMedicine() - Ended - Medicine modified")
                medicines[i] = medicine
                return true
            }
        }

        Log.i(Log.DEBUG.toString(),"UserInformation: editMedicine() - Ended - Medicine not found")
        return false
    }

    @Synchronized fun editFriend(oldName: String, friend: Friend): Boolean{
        Log.i(Log.DEBUG.toString(),"UserInformation: editFriend() - Started")

        for(i in friends.indices){
            if(friends[i].name == oldName){
                Log.i(Log.DEBUG.toString(),"UserInformation: editFriend() - Ended - Friend modified")
                friends[i] = friend
                return true
            }
        }

        Log.i(Log.DEBUG.toString(),"UserInformation: editFriend() - Ended - Friend not found")
        return false
    }

    @Synchronized fun flushAll(){
        Log.i(Log.DEBUG.toString(),"UserInformation: flushAll() - Started")

        LocalDatabase.saveFriendList(friends)
        LocalDatabase.saveMedicineList(medicines)

        Log.i(Log.DEBUG.toString(),"UserInformation: flushAll() - Ended")
    }


}