package com.pillskeeper.datamanager

import android.util.Log
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pillskeeper.data.Medicine
import com.pillskeeper.data.RemoteMedicine
import com.pillskeeper.data.User
import java.util.*

object CentralDatabase {

    private lateinit var databaseReference: DatabaseReference

    /**
     * Metodo per ottenere il riferimento remoto del database
     */
    fun obtainRemoteDatabase() {
        databaseReference = Firebase.database.reference
    }

    /**
     * Metodo che permette l'aggiunta di un nuovo utente al database Firebase
     * @param User L'utente che deve essere aggiunto al database
     * @return True se l'inserimento è andato a buon fine, false altrimenti
     */
    fun writeNewUser(user: User): Boolean {
        Log.d(Log.DEBUG.toString(), "writeNewUser()-Started")
        var userAlreadyExists: Boolean = false
        databaseReference.child("users")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.d(Log.DEBUG.toString(), "writeNewUser()-ERROR FIREBASE DATABASE")
                    TODO("Not yet implemented")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    userAlreadyExists =
                        (checkAlreadyExistingUser(p0.value as Map<String, Objects>, user.userId))
                }
            })
        return if (userAlreadyExists) {
            false
        } else {
            updateUser(user)
            true
        }
    }

    fun updateUser(user: User) {
        databaseReference.child("users").child(user.userId.toString()).setValue(user)
            .addOnCompleteListener { }
    }

    fun writeNewMedicine(medicine: RemoteMedicine) {
        databaseReference.child("medicines").child(medicine.medicineId).setValue(medicine)
    }

    fun getRemoteMedicineList(): RemoteMedicine {
        var medicinesList: Map<String, Objects>

    }


    //Private methods for Users

    private fun checkAlreadyExistingUser(users: Map<String, Objects>, userId: Int): Boolean {
        return users.containsKey(userId)
    }

    //Private methods
}