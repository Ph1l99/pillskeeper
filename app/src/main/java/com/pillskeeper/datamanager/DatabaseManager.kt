package com.pillskeeper.datamanager

import android.util.Log
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pillskeeper.data.Medicine
import com.pillskeeper.data.RemoteMedicine
import com.pillskeeper.data.User
import java.util.*

object DatabaseManager {

    private lateinit var databaseReference: DatabaseReference
    private const val MEDICINES = "Medicines"
    private const val PATH_USERS = "users"
    private const val PATH_MEDICINES = "medicines"

    /**
     * Metodo per ottenere il riferimento remoto del database
     */
    fun obtainRemoteDatabase() {
        databaseReference = Firebase.database.reference
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
                map = if (property == MEDICINES) {
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
    fun writeNewUser(user: User): Boolean {
        return if (getUser(user.userId) != null) {
            false
        } else {
            databaseReference.child(PATH_USERS).setValue(user)
            true
        }
    }

    /**
     * Metodo per ottenere un User da DB condiviso
     * @param userId La stringa che identifica univocamente l'utente
     * @return User L'oggetto che rappresenta l'utente
     */
    fun getUser(userId: String): User? {
        Log.d(Log.DEBUG.toString(), "getUser()-Started")
        var foundUser: User? = null
        databaseReference.child(PATH_USERS).child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    foundUser = p0.value as User
                }

                override fun onCancelled(p0: DatabaseError) {
                    Log.d(
                        Log.DEBUG.toString(),
                        "getUser()-ERROR-FIREBASE: " + p0.message + " (CODE " + p0.code + ")"
                    )
                }
            })
        Log.d(Log.DEBUG.toString(), "getUser()-Ended")
        return foundUser
    }

    /**
     * Metodo per ottenere tutte le RemoteMedicine caricate a DB
     * @return Una Map<String,RemoteMedicine>
     */
    fun getMedicines(): Map<String, RemoteMedicine> {
        Log.d(Log.DEBUG.toString(), "getMedicines()-Started")
        return getDataFromDB(PATH_MEDICINES, "Medicine") as Map<String, RemoteMedicine>
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

    //Private methods for Users

    private fun checkAlreadyExistingUser(
        users: Map<String, User>,
        userId: String
    ): Pair<Boolean, User?> {
        return Pair(users.containsKey(userId), users[userId])
    }

    //Private methods for medicines
    private fun getMedicineFromList(
        medicines: Map<String, RemoteMedicine>,
        medicineId: String
    ): RemoteMedicine? {
        return medicines[medicineId]
    }
}