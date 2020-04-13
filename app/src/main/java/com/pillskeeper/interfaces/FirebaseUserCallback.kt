package com.pillskeeper.interfaces

import com.pillskeeper.data.User

interface FirebaseUserCallback {

    fun onCallback(user: User?)
}