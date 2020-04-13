package com.pillskeeper.interfaces

import com.pillskeeper.data.RemoteMedicine

interface FirebaseMedicineCallback {

    fun onCallback(list: List<RemoteMedicine>?)
}