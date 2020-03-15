package com.pillskeeper.datamanager

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pillskeeper.data.AbstractMedicine
import com.pillskeeper.data.User

object CentralDatabase {

    private lateinit var database: DatabaseReference

    fun obtainRemoteDatabase() {
        database = Firebase.database.reference
    }


    fun writeNewUser(user: User) {
        database.child("users").child(user.userId.toString()).setValue(user)
    }

     fun writeNewMedicine(medicine: AbstractMedicine) {
        //database.child("medicines").child(medicine.)
    }

}