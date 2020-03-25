package com.pillskeeper.interfaces

import com.pillskeeper.data.Appointment
import com.pillskeeper.data.Friend
import com.pillskeeper.data.LocalMedicine
import java.util.*

interface LocalDatabaseInterface {

    fun readUsername(): String?

    fun readFriendList(): LinkedList<Friend>

    fun readMedicineList(): LinkedList<LocalMedicine>

    fun readAppointmentList(): LinkedList<Appointment>

    fun saveUsername(username: String)

    fun saveFriendList(friends: LinkedList<Friend>)

    fun saveFriendList()

    fun saveMedicineList(medicine: LinkedList<LocalMedicine>)

    fun saveMedicineList()

    fun saveAppointmentList(appointments: LinkedList<Appointment>)

    fun saveAppointmentList()

}