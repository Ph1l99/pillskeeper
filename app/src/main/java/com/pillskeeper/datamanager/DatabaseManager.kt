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
     * Metodo che permette l'aggiunta di un nuovo utente al database Firebase
     * @param User L'utente che deve essere aggiunto al database
     * @return Un oggetto Pair contenente l'esito dell'operazione e il tipo di errore ricevuto
     */
    fun writeNewUser(user: User): Pair<ErrorTypeEnum, Boolean> {
        Log.d(Log.DEBUG.toString(), "writeNewUser()-Started")
        return if (getUser(user.userId) != null) {
            Log.i(Log.DEBUG.toString(), "writeNewUser()-Obj exists")
            Pair(ErrorTypeEnum.FIREBASE_OBJECT_ALREADY_EXISTS, false)
        } else {
            databaseReference.child(PATH_USERS).child(user.userId).setValue(user)
            Log.i(Log.DEBUG.toString(), "writeNewUser()-Ended")
            Pair(ErrorTypeEnum.WRITING_COMPLETE, true)
        }
    }

    /**
     * Metodo per ottenere un User da DB condiviso
     * @param userId La stringa che identifica univocamente l'utente
     * @return User L'oggetto che rappresenta l'utente
     */
    fun getUser(userId: String): User? {
        Log.i(Log.DEBUG.toString(), "getUser()-Started")
        var foundUser: User? = null
        databaseReference.child(PATH_USERS).child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    foundUser = User.fromMap(p0.value as Map<String, String>)
                }

                override fun onCancelled(p0: DatabaseError) {
                    Log.i(
                        Log.DEBUG.toString(),
                        "getUser()-ERROR-FIREBASE: " + p0.message + " (CODE " + p0.code + ")"
                    )
                }
            })
        Log.i(Log.DEBUG.toString(), "getUser()-Ended")
        return foundUser
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
     * Metodo per ottenere tutte le RemoteMedicine caricate a DB
     * @return Una List<RemoteMedicine>
     */
    fun getMedicines(): List<RemoteMedicine> {
        Log.i(Log.DEBUG.toString(), "getMedicines()-Started")
        lateinit var listMedicines: List<RemoteMedicine>
        databaseReference.child(PATH_MEDICINES)
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onCancelled(p0: DatabaseError) {
                    Log.i(
                        Log.DEBUG.toString(),
                        "getDataFromDB()-ERROR-FIREBASE: " + p0.message + " (CODE " + p0.code + ")"
                    )
                }

                override fun onDataChange(p0: DataSnapshot) {
                    listMedicines =
                        RemoteMedicine.getMedicineListFromMap(p0.value as Map<String, Map<String, String>>)

                }
            })
        return listMedicines
    }

    /**
     * Funzione per ottenere una medicina dato il suo ID
     * @param medicineId L'identificativo della medicina
     * @return Un oggetto di tipo RemoteMedicine
     */
    fun getMedicine(medicineId: String): RemoteMedicine? {
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
}