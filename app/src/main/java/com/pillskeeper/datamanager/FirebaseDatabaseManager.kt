package com.pillskeeper.datamanager

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pillskeeper.data.RemoteMedicine
import com.pillskeeper.data.User
import com.pillskeeper.interfaces.Callback
import com.pillskeeper.interfaces.FirebaseMedicineCallback
import com.pillskeeper.interfaces.FirebaseUserCallback

object FirebaseDatabaseManager {
    lateinit var databaseReference: DatabaseReference
    lateinit var medicineRefs: DatabaseReference

    private const val PATH_MEDICINES = "medicines"
    private const val PATH_USERS = "users"

    fun enablePersistence() {
        Firebase.database.setPersistenceEnabled(true)
        medicineRefs = Firebase.database.getReference(PATH_MEDICINES)
        medicineRefs.keepSynced(true)
    }


    fun obtainDatabaseReference() {
        databaseReference = Firebase.database.reference
    }

    fun getMedicines(firebaseMedicineCallback: FirebaseMedicineCallback) {
        databaseReference.child(PATH_MEDICINES)
            .addValueEventListener(object : ValueEventListener {

                override fun onCancelled(p0: DatabaseError) {
                    firebaseMedicineCallback.onCallback(null)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    firebaseMedicineCallback.onCallback(RemoteMedicine.getMedicineListFromMap(p0.value as Map<String, Map<String, String>>))
                }
            })
    }

    fun getUser(userId: String, firebaseUserCallback: FirebaseUserCallback) {
        databaseReference.child(PATH_USERS).child(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    firebaseUserCallback.onCallback(User.fromMap(p0.value as Map<String, String>))
                }

                override fun onCancelled(p0: DatabaseError) {
                    firebaseUserCallback.onCallback(null)
                }
            })

    }

    fun writeUser(user: User, callback: Callback) {

        databaseReference.child(PATH_USERS).child(user.userId).setValue(user)
            .addOnFailureListener {
                callback.error()
            }
            .addOnCompleteListener {
                callback.success(true)
            }

    }

    fun writeMedicine(medicine: RemoteMedicine) {

        databaseReference.child(PATH_MEDICINES).child(medicine.id)
            .setValue(medicine)

    }
}