package com.pillskeeper.datamanager

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

object StorageManager {
    private lateinit var storageReferenceRoot: StorageReference

    fun obtainStorageReference() {
        storageReferenceRoot = Firebase.storage.reference
    }
}