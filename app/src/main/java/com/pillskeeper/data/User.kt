package com.pillskeeper.data

import java.io.Serializable

data class User(var userId: String, var name: String, var surname: String, var email: String) :Serializable{

    companion object{
        fun fromMap(mapsUser: Map<String, String>): User {
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