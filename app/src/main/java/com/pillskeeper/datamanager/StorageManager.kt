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
        Log.i(Log.DEBUG.toString(), "obtainStorageReference()-Started")
        storageReferenceRoot = Firebase.storage.reference
        Log.i(Log.DEBUG.toString(), "obtainStorageReference()-Ended")
    }

    /**
     * Metodo per caricare un'immagine a DB
     * @param image L'oggetto che rappresenta l'immagine
     * @return Un oggetto Pair contenente l'esito dell'operazione e il tipo di errore ricevuto
     */
    fun uploadImage(image: FileInputStream, imageName: String): Pair<ErrorTypeEnum?, Boolean>? {
        Log.i(Log.DEBUG.toString(), "uploadImage()-Started")
        var resultUpload: Pair<ErrorTypeEnum?, Boolean>? = null
        if (storageReferenceRoot.child("$PATH_MEDICINES_IMAGES$imageName.jpg")
                .putStream(image).isComplete
        ) {
            resultUpload = Pair(ErrorTypeEnum.WRITING_COMPLETE, true)
            Log.i(Log.DEBUG.toString(), "uploadImage()-Done")
        } else {
            resultUpload = Pair(ErrorTypeEnum.FIREBASE_STORAGE_GENERIC_ERROR, false)
            Log.i(Log.DEBUG.toString(), "uploadImage()-ERROR")
        }
        Log.i(Log.DEBUG.toString(), "uploadImage()-Ended")
        return resultUpload
    }

    //TODO da rivedere download, prima controllare che funzioni upload

    /**
     * Metodo per ottenere l'immagine dallo storage Firebase
     * @param imageName Il nome dell'immagine
     */
    fun getImage(imageName: String): File {
        Log.i(Log.DEBUG.toString(), "getImage()-Started")
        val localFile = File.createTempFile("", "jpg")
        storageReferenceRoot.child("$PATH_MEDICINES_IMAGES$imageName.jpg").getFile(localFile)
            .addOnSuccessListener {
                //TODO
            }.addOnFailureListener {
                Log.i(Log.DEBUG.toString(), "writeNewUser()-ERROR-FIREBASE" + it.message)
                throw it
            }
        Log.i(Log.DEBUG.toString(), "getImage()-Ended")
        return localFile
    }
}