package com.pillskeeper.datamanager

import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pillskeeper.data.RemoteMedicine
import com.pillskeeper.data.User
import com.pillskeeper.datamanager.UserInformation.context
import com.pillskeeper.interfaces.FirebaseMedicineCallback
import com.pillskeeper.interfaces.FirebaseUserCallback

object FirebaseDatabaseManager {
    lateinit var databaseReference: DatabaseReference
    private const val PATH_MEDICINES = "medicines"
    private const val PATH_USERS = "users"


    fun obtainDatabaseReference() {
        databaseReference = Firebase.database.reference
    }

    fun getMedicines(firebaseMedicineCallback: FirebaseMedicineCallback) {
        databaseReference.child(PATH_MEDICINES)
            .addValueEventListener(object : ValueEventListener {

                override fun onCancelled(p0: DatabaseError) {
                    firebaseMedicineCallback.onCallback(null)
                    Log.i(
                        Log.ERROR.toString(),
                        "getDataFromDB()-ERROR-FIREBASE: " + p0.message + " (CODE " + p0.code + ")"
                    )
                }

                override fun onDataChange(p0: DataSnapshot) {
                    firebaseMedicineCallback.onCallback(RemoteMedicine.getMedicineListFromMap(p0.value as Map<String, Map<String, String>>))
                    Log.i(Log.DEBUG.toString(), "fillMedicinesList()-Ended")
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
                    Toast.makeText(context, "Dati non scaricati", Toast.LENGTH_LONG).show()
                }
            })

    }

    fun writeUser(user: User) {

        databaseReference.child(PATH_USERS).child(user.userId).setValue(user)

    }

    fun writeMedicine(medicine: RemoteMedicine) {

        databaseReference.child(PATH_MEDICINES).child(medicine.id)
            .setValue(medicine)

    }
}