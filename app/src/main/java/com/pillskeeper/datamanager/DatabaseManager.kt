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
    private const val PATH_USERS = "users"
    private const val PATH_MEDICINES = "medicines"

    /**
     * Metodo per ottenere il riferimento remoto del database
     */
    fun obtainRemoteDatabase() {
        Log.d(Log.DEBUG.toString(), "obtanRemoteDatabase()-Started")
        databaseReference = Firebase.database.reference
        Log.d(Log.DEBUG.toString(), "obtanRemoteDatabase()-Ended")
    }

    /**
     * Metodo per ottenere una mappa contenente gli oggetti ad un determinato path
     * @param path Il percoso al quale estrarre i dati
     * @param property Una stringa che indica la tipologia di mappa che si vuole recuperare
     * ad esempio quella mappa degli utenti o delle medicine
     */
    private fun getDataFromDB(path: String, property: String): Any? {
        Log.d(Log.DEBUG.toString(), "getDataFromDB()-Started")
        var map: Map<String, Any>? = null
        databaseReference.child(path).addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                Log.d(
                    Log.DEBUG.toString(),
                    "getDataFromDB()-ERROR-FIREBASE: " + p0.message + " (CODE " + p0.code + ")"
                )
            }

            override fun onDataChange(p0: DataSnapshot) {
                map = if (property == PATH_MEDICINES) {
                    p0.value as Map<String, RemoteMedicine>
                } else {
                    p0.value as Map<String, User>
                }
            }
        })
        Log.d(Log.DEBUG.toString(), "getDataFromDB()-Ended")
        return map
    }

    /**
     * Metodo che permette l'aggiunta di un nuovo utente al database Firebase
     * @param User L'utente che deve essere aggiunto al database
     * @return True se l'inserimento è andato a buon fine, false altrimenti
     */
    fun writeNewUser(user: User): Pair<ErrorTypeEnum?, Boolean>? {
        Log.d(Log.DEBUG.toString(), "writeNewUser()-Started")
        var result: Pair<ErrorTypeEnum?, Boolean>? = null
        return if (getUser(user.userId) != null) {
            Log.w(Log.DEBUG.toString(), "writeNewUser()-Obj exists")
            Pair(ErrorTypeEnum.FIREBASE_OBJECT_ALREADY_EXISTS, false)
        } else {
            databaseReference.child(PATH_USERS).child(user.userId).setValue(user)
                .addOnCompleteListener {
                    Log.w(Log.DEBUG.toString(), "writeNewUser()-Completed")
                    result = Pair(ErrorTypeEnum.WRITING_COMPLETE, true)
                }.addOnFailureListener {
                    Log.w(Log.DEBUG.toString(), "writeNewUser()-ERROR-FIREBASE" + it.message)
                    result = Pair(ErrorTypeEnum.FIREBASE_DB_READING, false)
                    throw it
                }
            Log.w(Log.DEBUG.toString(), "writeNewUser()-Ended")
            result
        }
    }

    /**
     * Metodo per ottenere un User da DB condiviso
     * @param userId La stringa che identifica univocamente l'utente
     * @return User L'oggetto che rappresenta l'utente
     */
    fun getUser(userId: String): User? {
        Log.w(Log.DEBUG.toString(), "getUser()-Started")
        var foundUser: User? = null
        databaseReference.child(PATH_USERS).child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    foundUser = p0.value as User
                    //TODO controllare null
                }

                override fun onCancelled(p0: DatabaseError) {
                    Log.w(
                        Log.DEBUG.toString(),
                        "getUser()-ERROR-FIREBASE: " + p0.message + " (CODE " + p0.code + ")"
                    )
                }
            })
        Log.w(Log.DEBUG.toString(), "getUser()-Ended")
        return foundUser
    }

    /**
     * Metodo per la scrittura di una nuova medicina
     * @param medicine L'oggetto corrispondente alla medicina che si vuole inserire
     */
    fun writeNewMedicine(medicine: RemoteMedicine): Pair<ErrorTypeEnum?, Boolean>? {
        var actionCompleted: Pair<ErrorTypeEnum?, Boolean>? = null
        return if (getMedicine(medicine.id) != null) {
            Pair(ErrorTypeEnum.FIREBASE_OBJECT_ALREADY_EXISTS, false)
        } else {
            databaseReference.child(PATH_MEDICINES).child(medicine.id).setValue(medicine)
                .addOnCompleteListener {
                    Log.d(Log.DEBUG.toString(), "writeNewMedicine()-Completed")
                    actionCompleted = Pair(ErrorTypeEnum.WRITING_COMPLETE, true)
                }
                .addOnFailureListener {
                    Log.d(Log.DEBUG.toString(), "writeNewMedicine()-ERROR-FIREBASE" + it.message)
                    actionCompleted = Pair(ErrorTypeEnum.FIREBASE_DB_READING, false)
                    throw it
                }
            actionCompleted
        }
    }

    /**
     * Metodo per ottenere tutte le RemoteMedicine caricate a DB
     * @return Una Map<String,RemoteMedicine>
     */
    fun getMedicines(): Map<String, RemoteMedicine> {
        Log.d(Log.DEBUG.toString(), "getMedicines()-Started")
        return getDataFromDB(PATH_MEDICINES, PATH_MEDICINES) as Map<String, RemoteMedicine> //TODO check cast
    }

    /**
     * Funzione per ottenere una medicina dato il suo ID
     * @param medicineId L'identificativo della medicina
     * @return Un oggetto di tipo RemoteMedicine
     */
    fun getMedicine(medicineId: String): RemoteMedicine? {
        Log.d(Log.DEBUG.toString(), "getUser()-Started")
        var foundMedicine: RemoteMedicine? = null
        databaseReference.child(PATH_MEDICINES).child(medicineId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    foundMedicine = p0.value as RemoteMedicine
                }

                override fun onCancelled(p0: DatabaseError) {
                    Log.d(
                        Log.DEBUG.toString(),
                        "getUser()-ERROR-FIREBASE: " + p0.message + " (CODE " + p0.code + ")"
                    )
                }
            })
        Log.d(Log.DEBUG.toString(), "getUser()-Ended")
        return foundMedicine
    }
}