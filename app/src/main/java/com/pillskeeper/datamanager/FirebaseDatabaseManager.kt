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

/**
 * Object that manages all the set and get operations related to the Firebase Realtime Database
 */
object FirebaseDatabaseManager {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var medicineRefs: DatabaseReference

    private const val PATH_MEDICINES = "medicines"
    private const val PATH_USERS = "users"

    /**
     * Method for enabling the offline database cache
     */
    fun enablePersistence() {
        Firebase.database.setPersistenceEnabled(true)
        medicineRefs = Firebase.database.getReference(PATH_MEDICINES)
        medicineRefs.keepSynced(true)
    }

    /**
     * Method for getting the Firebase Database reference
     */
    fun obtainDatabaseReference() {
        databaseReference = Firebase.database.reference
    }

    /**
     * Method for getting a list of Remote Medicines
     * @param firebaseMedicineCallback The callback for returning the values
     */
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

    /**
     * Method for getting the user via userId
     * @param userId The String which represents the UID
     * @param firebaseUserCallback The callback for returning the values
     */
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

    /**
     * Method for writing a new user to the Firebase Database
     * @param user The object User
     * @param callback The callback for returning the values
     */
    fun writeUser(user: User, callback: Callback) {

        databaseReference.child(PATH_USERS).child(user.userId).setValue(user)
            .addOnFailureListener {
                callback.onError()
            }
            .addOnCompleteListener {
                callback.onSuccess(true)
            }

    }

    /**
     * Method for writing a new medicine to the Firebase Database
     * @param medicine The object RemoteMedicine
     * @param callback The callback for returning the values
     */
    fun writeMedicine(medicine: RemoteMedicine, callback: Callback) {

        databaseReference.child(PATH_MEDICINES).child(medicine.id)
            .setValue(medicine)
            .addOnCompleteListener {
                callback.onSuccess(true)
            }
            .addOnFailureListener {
                callback.onError()
            }

    }
}