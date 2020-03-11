package com.pillskeeper.interfaces

import com.pillskeeper.data.Friend
import com.pillskeeper.data.Medicine
import java.util.*

interface LocalDatabaseInterface {

    fun readUsername(): String?

    fun readFriendList(): LinkedList<Friend>

    fun readMedicineList(): LinkedList<Medicine>

    fun saveMedicineList(medicine: LinkedList<Medicine>)

    fun resetMemory()

}