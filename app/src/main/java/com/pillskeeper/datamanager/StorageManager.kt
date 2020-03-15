package com.pillskeeper.datamanager

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import java.io.FileInputStream

object StorageManager {
    private lateinit var storageReferenceRoot: StorageReference
    private const val PATH_MEDICINES_IMAGES = "medicines/"

    fun obtainStorageReference() {
        storageReferenceRoot = Firebase.storage.reference
    }

    fun uploadImage(image: FileInputStream) {
        var uploadTask = storageReferenceRoot.child(PATH_MEDICINES_IMAGES).putStream(image)
    }
}