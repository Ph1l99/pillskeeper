package com.pillskeeper.data

import java.io.Serializable

/**
 * Data class representing a PillsKeeper User
 * @param userId The userId
 * @param name The user's name
 * @param surname The user's surname
 * @param email The user's email
 */
data class User(var userId: String, var name: String, var surname: String, var email: String) :
    Serializable {

    companion object {

        /**
         * Method for getting a User from the map ubtained from the Realtime Database
         * @param mapsUser The Map representing the users node JSON
         * @return The User extracted
         */
        fun getUserFromMap(mapsUser: Map<String, String>): User {
            lateinit var emailM: String
            lateinit var nameM: String
            lateinit var surnameM: String
            lateinit var userIdM: String
            for ((k, v) in mapsUser) {
                when (k) {
                    "email" -> emailM = v
                    "name" -> nameM = v
                    "surname" -> surnameM = v
                    "userId" -> userIdM = v
                }
            }
            return User(userIdM, nameM, surnameM, emailM)
        }

    }
}