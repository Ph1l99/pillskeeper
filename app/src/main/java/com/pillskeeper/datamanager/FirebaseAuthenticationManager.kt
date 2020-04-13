package com.pillskeeper.datamanager

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.pillskeeper.interfaces.Callback

object FirebaseAuthenticationManager {

    private lateinit var auth: FirebaseAuth

    fun obtainAuthenticationInstance() {
        auth = FirebaseAuth.getInstance()
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun getCurrentUserIdToken(user: FirebaseUser, callback: Callback) {
        user.getIdToken(true)
            .addOnFailureListener {
                callback.error()
            }
            .addOnSuccessListener {
                if (it.token != null) {
                    callback.success(true)
                } else {
                    callback.error()
                }
            }
    }

    fun createNewUser(email: String, password: String, callback: Callback) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                callback.success(true)
            }
            .addOnFailureListener {
                callback.error()
            }
    }

    fun loginUser(email: String, password: String, callback: Callback) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                callback.success(true)
            } else {
                callback.error()
            }
        }
    }

    fun reautenticateUser(email: String, password: String, callback: Callback) {
        val credential = EmailAuthProvider.getCredential(email, password)
        getCurrentUser()?.reauthenticate(credential)?.addOnCompleteListener {
            if (it.isSuccessful) {
                callback.success(true)
            } else {
                callback.error()
            }
        }
    }
}