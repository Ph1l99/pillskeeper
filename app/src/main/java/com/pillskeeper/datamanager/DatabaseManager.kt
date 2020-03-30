package com.pillskeeper.datamanager

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pillskeeper.data.RemoteMedicine
import com.pillskeeper.data.User
import com.pillskeeper.enums.ErrorTypeEnum

object DatabaseManager {

    private lateinit var databaseReference: DatabaseReference

    const val PATH_MEDICINES = "medicines"

    /**
     * Metodo per ottenere il riferimento remoto del database
     */
    fun obtainRemoteDatabase(): DatabaseReference {
        databaseReference = Firebase.database.reference
        return databaseReference
    }

    /**
     * Metodo per la scrittura di una nuova medicina
     * @param medicine L'oggetto corrispondente alla medicina che si vuole inserire
     * @return Un oggetto Pair contenente l'esito dell'operazione e il tipo di errore ricevuto
     */
    fun writeNewMedicine(medicine: RemoteMedicine): Pair<ErrorTypeEnum, Boolean> {
        Log.i(Log.DEBUG.toString(), "writeNewMedicine()-Started")
        return if (getMedicine(medicine.id) != null) {
            Log.i(Log.DEBUG.toString(), "writeNewMedicine()-Obj exists")
            Pair(ErrorTypeEnum.FIREBASE_OBJECT_ALREADY_EXISTS, false)
        } else {
            databaseReference.child(PATH_MEDICINES).child(medicine.id).setValue(medicine)
            Log.i(Log.DEBUG.toString(), "writeNewMedicine()-Done")
            Pair(ErrorTypeEnum.WRITING_COMPLETE, true)
        }
    }

    /**
     * Funzione per ottenere una medicina dato il suo ID
     * @param medicineId L'identificativo della medicina
     * @return Un oggetto di tipo RemoteMedicine
     */
    private fun getMedicine(medicineId: String): RemoteMedicine? {
        Log.i(Log.DEBUG.toString(), "getUser()-Started")
        var foundMedicine: RemoteMedicine? = null
        databaseReference.child(PATH_MEDICINES).child(medicineId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    foundMedicine =
                        RemoteMedicine.getMedicineFromMap(p0.value as Map<String, String>)
                }

                override fun onCancelled(p0: DatabaseError) {
                    Log.i(
                        Log.DEBUG.toString(),
                        "getUser()-ERROR-FIREBASE: " + p0.message + " (CODE " + p0.code + ")"
                    )
                }
            })
        Log.i(Log.DEBUG.toString(), "getUser()-Ended")
        return foundMedicine
    }

    fun getUserFromResultSet(rs: Map<String, String>): User {
        return User.fromMap(rs)
    }
}