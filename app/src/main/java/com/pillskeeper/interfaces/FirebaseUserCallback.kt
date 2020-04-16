package com.pillskeeper.interfaces

import com.pillskeeper.data.User

/**
 * FirebaseUserCallback for getting the users results from async reading
 */
interface FirebaseUserCallback {

    /**
     * Method for passing back a User
     * @param user The User object
     */
    fun onCallback(user: User?)
}