package com.pillskeeper.datamanager

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
        var resultAuth: Pair<ErrorTypeEnum?, Boolean>? = null
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                resultAuth = Pair(ErrorTypeEnum.WRITING_COMPLETE, true)
            }.addOnFailureListener {
                resultAuth = Pair(ErrorTypeEnum.FIREBASE_AUTH, false)
            }
        return resultAuth
    }
}