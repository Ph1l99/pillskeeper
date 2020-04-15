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

    fun signOut() {
        auth.signOut()
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun getCurrentUserIdToken(user: FirebaseUser, callback: Callback) {
        user.getIdToken(true)
            .addOnFailureListener {
                callback.onError()
            }
            .addOnSuccessListener {
                if (it.token != null) {
                    callback.onSuccess(true)
                } else {
                    callback.onError()
                }
            }
    }

    fun createNewUser(email: String, password: String, callback: Callback) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                callback.onSuccess(true)
            }
            .addOnFailureListener {
                callback.onError()
            }
    }

    fun loginUser(email: String, password: String, callback: Callback) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                callback.onSuccess(true)
            } else {
                callback.onError()
            }
        }
    }

    fun reautenticateUser(email: String, password: String, callback: Callback) {
        val credential = EmailAuthProvider.getCredential(email, password)
        getCurrentUser()?.reauthenticate(credential)?.addOnCompleteListener {
            if (it.isSuccessful) {
                callback.onSuccess(true)
            } else {
                callback.onError()
            }
        }
    }

    fun resetPassword(email: String, callback: Callback) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                callback.onSuccess(true)
            } else {
                callback.onError()

            }
        }
    }
}