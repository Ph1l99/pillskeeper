package com.pillskeeper.datamanager

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

object AuthenticationManager {
    private lateinit var auth: FirebaseAuth

    fun obtainAuthentication() {
        auth = FirebaseAuth.getInstance()
    }

    fun getSignedInUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun createNewUser(email:String, password:String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email,password)
    }

}