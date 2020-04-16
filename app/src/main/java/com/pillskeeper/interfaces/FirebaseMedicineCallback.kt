package com.pillskeeper.interfaces

import com.pillskeeper.data.RemoteMedicine

/**
 * FirebaseMedicineCallback for getting the medicines results from async reading
 */
interface FirebaseMedicineCallback {

    /**
     * Method for passing back the list of RemoteMedicine
     * @param list A List of RemoteMedicine
     */
    fun onCallback(list: List<RemoteMedicine>?)
}