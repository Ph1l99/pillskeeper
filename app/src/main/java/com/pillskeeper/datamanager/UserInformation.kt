package com.pillskeeper.datamanager

import android.content.Context
import android.util.Log
import com.pillskeeper.data.*
import java.util.*

object UserInformation {

    const val TAG = "UserInformation: "
    
    var medicines: LinkedList<LocalMedicine> = LocalDatabase.readMedicineList()
    var friends: LinkedList<Friend> = LocalDatabase.readFriendList()
    var appointments: LinkedList<Appointment> = LocalDatabase.readAppointmentList()
    var user: User? = LocalDatabase.readUser()
    lateinit var context: Context

    /*******  GET FUNCTION   *******/

    @Synchronized fun getSpecificMedicine(name: String) : LocalMedicine?{
        Log.i(TAG,"getSpecificMedicine() - Started")

        medicines.forEach {
            entry -> if (entry.name == name){
                Log.i(TAG,"getSpecificMedicine() - Ended - Medicine found")
                return entry
            }
        }

        Log.i(TAG,"getSpecificMedicine() - Ended - Medicine not found")
        return null
    }

    @Synchronized fun getSpecificFriend(name: String)  : Friend? {
        Log.i(TAG,"getSpecificFriend() - Started")

        friends.forEach {
            entry -> if(entry.name == name) {
                Log.i(TAG,"getSpecificFriend() - Ended - Friend found")
                return entry
            }
        }

        Log.i(TAG,"getSpecificFriend() - Ended - Friend not found")
        return null
    }

    @Synchronized fun getSpecificAppointment(name: String) : Appointment? {
        Log.i(TAG,"getSpecificAppointment() - Started")

        appointments.forEach {
            entry -> if(entry.name == name) {
                Log.i(TAG,"getSpecificAppointment() - Ended - Appointment found")
                return entry
            }
        }

        Log.i(TAG,"getSpecificAppointment() - Ended - Appointment not found")
        return null
    }


    /*******  ADD  FUNCTION   *******/

    @Synchronized fun addNewFriend(friend: Friend): Boolean {
        Log.i(TAG,"addNewFriend() - Started")
        friends.forEach {
            entry -> if(entry.name == friend.name){
                Log.i(TAG,"addNewFriend() - Ended - Friend already present")
                return false
            }
        }

        friends.add(friend)
        LocalDatabase.saveFriendList()

        Log.i(TAG,"addNewFriend() - Ended - Friend inserted")
        return true
    }

    @Synchronized fun addNewMedicine(medicine: LocalMedicine) : Boolean{
        Log.i(TAG,"addNewMedicine() - Started")

        medicines.forEach {
            entry -> if(entry.name == medicine.name && entry.medicineType == medicine.medicineType){
                Log.i(TAG,"addNewMedicine() - Ended - Medicine already present")
                return false
            }
        }

        medicines.add(medicine)
        LocalDatabase.saveMedicineList()

        Log.i(TAG,"addNewMedicine() - Ended - Medicine inserted")
        return true
    }

    @Synchronized fun addNewReminder(medicineName: String, reminder: ReminderMedicine): Boolean {
        Log.i(TAG,"addNewReminder() - Started")

        var currMedicine: LocalMedicine? = null

        for(i in medicines.indices) {
            if(medicines[i].name == medicineName) {
                currMedicine = medicines[i]
                break
            }
        }

        if(currMedicine == null){
            Log.i(TAG,"addNewReminder() - Ended - Medicine not found")
            return false
        }

        currMedicine.reminders?.forEach { it ->
            if(it.days == reminder.days && it.expireDate == reminder.expireDate &&
                it.hours == reminder.hours && it.minutes == reminder.minutes && it.dosage == reminder.dosage){
                Log.i(TAG,"addNewReminder() - Ended - Reminder with same Info")
                return false
            }
        }

        if(currMedicine.reminders == null)
            currMedicine.reminders = LinkedList()

        currMedicine.reminders?.add(reminder)

        LocalDatabase.saveMedicineList()
        Log.i(TAG,"addNewReminder() - Ended - Reminder inserted")
        return true
    }

    @Synchronized fun addNewAppointment(appointment: Appointment): Boolean{
        Log.i(TAG,"addNewAppointment() - Started")

        appointments.forEach {
            entry -> if(entry.name == appointment.name){
                Log.i(TAG,"addNewAppointment() - Ended - Appointment already present")
                return false
            }
        }

        appointments.add(appointment)
        LocalDatabase.saveAppointmentList()

        Log.i(TAG,"addNewAppointment() - Ended - Appointment inserted")
        return true
    }



    /*******  EDIT FUNCTION   *******/

    @Synchronized fun editAppointment(oldName: String, appointment: Appointment): Boolean{
        Log.i(TAG,"editAppointment() - Started")

        for(i in appointments.indices){
            if(appointments[i].name == oldName){
                Log.i(TAG,"editAppointment() - Ended - Appointment modified")
                appointments[i] = appointment
                LocalDatabase.saveAppointmentList()
                return true
            }
        }

        Log.i(TAG,"editAppointment() - Ended - Appointment not found")
        return false
    }

    @Synchronized fun editMedicine(oldName: String, medicine: LocalMedicine): Boolean{
        Log.i(TAG,"editMedicine() - Started")

        for(i in medicines.indices){
            if(medicines[i].name == oldName){
                Log.i(TAG,"editMedicine() - Ended - Medicine modified")
                medicines[i] = medicine
                LocalDatabase.saveMedicineList()
                return true
            }
        }

        Log.i(TAG,"editMedicine() - Ended - Medicine not found")
        return false
    }

    @Synchronized fun editFriend(oldName: String, friend: Friend): Boolean{
        Log.i(TAG,"editFriend() - Started")

        for(i in friends.indices){
            if(friends[i].name == oldName){
                Log.i(TAG,"editFriend() - Ended - Friend modified")
                friends[i] = friend
                LocalDatabase.saveFriendList()
                return true
            }
        }

        Log.i(TAG,"editFriend() - Ended - Friend not found")
        return false
    }

    @Synchronized fun editReminder(medName: String, oldReminder: ReminderMedicine, newReminder: ReminderMedicine): Boolean{
        Log.i(TAG,"editReminder() - Started")

        val medicine: LocalMedicine? = getSpecificMedicine(medName)

        if(medicine == null){
            Log.i(TAG,"editReminder() - Ended - Medicine not found")
            return false
        }

        for(i in medicine.reminders?.indices!!){
            if (medicine.reminders!![i] == oldReminder){
                medicine.reminders!![i] = newReminder
                Log.i(TAG,"editReminder() - Ended - Reminder edited")
                LocalDatabase.saveMedicineList()
                return true
            }
        }

        Log.i(TAG,"editReminder() - Ended - Reminder not found")
        return false
    }

    @Synchronized fun subMedicineQuantity(nameMedicine: String, subQty: Float ): LocalMedicine? {
        Log.i(TAG,"subMedicineQuantity() - Started")
        val medicine = getSpecificMedicine(nameMedicine)

        if(medicine != null){
            medicine.remainingQty -= subQty
            if(medicine.remainingQty < 0)
                medicine.remainingQty = 0F

            LocalDatabase.saveMedicineList()
            Log.i(TAG,"subMedicineQuantity() - Qty decreased")
            return medicine
        }

        Log.i(TAG,"subMedicineQuantity() - Medicine not found")
        return null
    }

    @Synchronized fun restoreQty(medicine: LocalMedicine): Boolean {
        Log.i(TAG,"subMedicineQuantity() - Started")

        medicine.remainingQty = medicine.totalQty
        LocalDatabase.saveMedicineList()

        Log.i(TAG,"subMedicineQuantity() - Qty restored")
        return true
    }
    

    /*******  DELETE FUNCTION   *******/

    @Synchronized fun deleteAppointment(name: String): Boolean {
        Log.i(TAG,"deleteAppointment() - Started")

        for(i in appointments.indices){
            if(appointments[i].name == name){
                Log.i(TAG,"deleteAppointment() - Ended - Appointment Deleted")
                appointments.removeAt(i)
                LocalDatabase.saveAppointmentList()
                return true
            }
        }

        Log.i(TAG,"deleteAppointment() - Ended - Appointment not found")
        return false
    }

    @Synchronized fun deleteMedicine(name: String): Boolean{
        Log.i(TAG,"deleteMedicine() - Started")

        for(i in medicines.indices){
            if(medicines[i].name == name){
                medicines.removeAt(i)
                LocalDatabase.saveMedicineList()
                Log.i(TAG,"deleteMedicine() - Ended - Medicine Deleted")
                return true
            }
        }

        Log.i(TAG,"deleteMedicine() - Ended - Medicine not found")
        return false
    }

    @Synchronized fun deleteFriend(name: String): Boolean{
        Log.i(TAG,"deleteFriend() - Started")

        for(i in friends.indices){
            if(friends[i].name == name){
                friends.removeAt(i)
                LocalDatabase.saveFriendList()
                Log.i(TAG,"deleteFriend() - Ended - Friend Removed")
                return true
            }
        }

        Log.i(TAG,"deleteFriend() - Ended - Friend not found")
        return false
    }

    @Synchronized fun erase() {
        friends = LinkedList()
        appointments = LinkedList()
        medicines = LinkedList()
        user = null
        LocalDatabase.erase()
    }

    @Synchronized fun cleanAppointmentAndReminderList(){
        Log.i(TAG, "cleanAppointmentAndReminderList() - function started")

        val nowDate = Date()

        for(i in appointments.indices)
            if(appointments[i].date < nowDate)
                appointments.removeAt(i)

        medicines.forEach {
            if (it.reminders != null)
                for(i in it.reminders!!.indices)
                    if (it.reminders!![i].expireDate != null
                        && it.reminders!![i].expireDate!! < nowDate)
                        it.reminders!!.removeAt(i)
        }

        Log.i(TAG, "cleanAppointmentAndReminderList() - function ended")
    }

}