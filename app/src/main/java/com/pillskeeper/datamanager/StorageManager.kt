package com.pillskeeper.datamanager

import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import com.pillskeeper.enums.ErrorTypeEnum
import java.io.File
import java.io.FileInputStream

object StorageManager {
    private lateinit var storageReferenceRoot: StorageReference
    private const val PATH_MEDICINES_IMAGES = "medicines/"

    /**
     * Metodo per ottenere il riferimento all'istanza FirebaseStorage
     */
    fun obtainStorageReference() {
        Log.d(Log.DEBUG.toString(), "obtainStorageReference()-Started")
        storageReferenceRoot = Firebase.storage.reference
        Log.d(Log.DEBUG.toString(), "obtainStorageReference()-Ended")
    }

    /**
     * Metodo per caricare un'immagine a DB
     * @param image L'oggetto che rappresenta l'immagine
     */
    fun uploadImage(image: FileInputStream, imageName: String): Pair<ErrorTypeEnum?, Boolean>? {
        Log.d(Log.DEBUG.toString(), "uploadImage()-Started")
        var resultUpload: Pair<ErrorTypeEnum?, Boolean>? = null
        storageReferenceRoot.child("$PATH_MEDICINES_IMAGES$imageName.jpg").putStream(image)
            .addOnFailureListener {
                Log.d(Log.DEBUG.toString(), "writeNewUser()-ERROR-FIREBASE" + it.message)
                resultUpload = Pair(ErrorTypeEnum.WRITING_COMPLETE, false)
                throw it
            }.addOnSuccessListener {
                resultUpload = Pair(ErrorTypeEnum.FIREBASE_STORAGE_READING, true)
                Log.d(Log.DEBUG.toString(), "uploadImage()-Completed")
            }
        Log.d(Log.DEBUG.toString(), "uploadImage()-Ended")
        return resultUpload
    }

    /**
     * Metodo per ottenere l'immagine dallo storage Firebase
     * @param imageName Il nome dell'immagine
     */
    fun getImage(imageName: String): File {
        Log.d(Log.DEBUG.toString(), "getImage()-Started")
        val localFile = File.createTempFile("", "jpg")
        storageReferenceRoot.child("$PATH_MEDICINES_IMAGES$imageName.jpg").getFile(localFile)
            .addOnSuccessListener {
                //TODO
            }.addOnFailureListener {
                Log.d(Log.DEBUG.toString(), "writeNewUser()-ERROR-FIREBASE" + it.message)
                throw it
            }
        Log.d(Log.DEBUG.toString(), "getImage()-Ended")
        return localFile
    }
}