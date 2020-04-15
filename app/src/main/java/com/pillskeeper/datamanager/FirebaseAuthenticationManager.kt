package com.pillskeeper.datamanager

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.pillskeeper.interfaces.Callback
import java.util.*

/**
 * Object that manages the operations regarding all users profiles
 */
object FirebaseAuthenticationManager {

    private lateinit var auth: FirebaseAuth

    /**
     * Method to get the instance for FirebaseAuth
     */
    fun obtainAuthenticationInstance() {
        auth = FirebaseAuth.getInstance()
    }

    /**
     * User signout method
     */
    fun signOut() {
        auth.signOut()
    }

    /**
     * Method to get the current user logged in
     * @return FirebaseUser
     */
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    /**
     * Method that obtains the FirebaseAuth Token for the current user
     * @param user A FirebaseUser
     * @param callback Callback for getting async result
     */
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

    /**
     * Method that allows the creation of a new user
     * @param email The user's email
     * @param password The user's password
     * @param callback Callback for getting the result
     */
    fun createNewUser(email: String, password: String, callback: Callback) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                callback.onSuccess(true)
            }
            .addOnFailureListener {
                callback.onError()
            }
    }

    /**
     * @param email The user's email
     * @param password The user's password
     * @param callback Callback for getting the result
     */
    fun loginUser(email: String, password: String, callback: Callback) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                callback.onSuccess(true)
            } else {
                callback.onError()
            }
        }
    }

    /**
     * Function that allows PillsKeeper to authenticate one user again in order to perform
     * major changes to the profile like changing a password or profile infos
     * @param email The user's email
     * @param password The user's password
     * @param callback Callback for getting the result
     */
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

    /**
     * Methods that allows the user to reset his password by sending an email
     * @param email The user's email
     * @param callback Callback for getting the result
     */
    fun resetPassword(email: String, callback: Callback) {
        auth.setLanguageCode(Locale.getDefault().language)
        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                callback.onSuccess(true)
            } else {
                callback.onError()

            }
        }
    }
}