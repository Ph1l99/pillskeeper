package com.pillskeeper.interfaces

import com.pillskeeper.data.Appointment
import com.pillskeeper.data.Friend
import com.pillskeeper.data.LocalMedicine
import com.pillskeeper.data.User
import java.util.*

interface LocalDatabaseInterface {

    fun readUser(): User?

    fun readFriendList(): LinkedList<Friend>

    fun readMedicineList(): LinkedList<LocalMedicine>

    fun readAppointmentList(): LinkedList<Appointment>

    fun saveUser(user: User)

    fun saveFriendList()

    fun saveMedicineList()

    fun saveAppointmentList()

}