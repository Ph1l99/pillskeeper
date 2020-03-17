package com.pillskeeper.datamanager

import android.util.Log
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.pillskeeper.enums.ErrorTypeEnum

object AuthenticationManager {
    private lateinit var auth: FirebaseAuth

    /**
     * Metodo per ottenere il riferimento all'istanza dell'autenticazione remota
     */
    fun obtainAuthentication() {
        auth = FirebaseAuth.getInstance()
    }

    /**
     * Metodo per ottenere l'utente attualmente autenticato
     * @return FirebaseUser
     */
    fun getSignedInUser(): FirebaseUser? {
        return auth.currentUser
    }

    /**
     * Metodo per la creazione di un utente sfruttando l'autenticazione integrata di Firebase
     * @param email L'indirizzo email
     * @param password La password
     * @return Un Task che indica se l'operazione è
     */
    fun createNewUser(email: String, password: String): Pair<ErrorTypeEnum?, Boolean>? {
        Log.w(Log.DEBUG.toString(), "createNewUser()-Started")
        var resultAuth: Pair<ErrorTypeEnum?, Boolean>? = null

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                await(it)
                Log.w(Log.DEBUG.toString(), "createNewUser()-Done")
                resultAuth = Pair(ErrorTypeEnum.WRITING_COMPLETE, true)

            }.addOnFailureListener {
                Log.w(Log.DEBUG.toString(), "createNewUser()-ERRORFIREBSDE")
                resultAuth = Pair(ErrorTypeEnum.FIREBASE_AUTH, false)
            }
        Log.w(Log.DEBUG.toString(), "createNewUser()-Ended")
        return resultAuth
    }
}