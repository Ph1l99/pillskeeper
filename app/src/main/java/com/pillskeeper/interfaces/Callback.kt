package com.pillskeeper.interfaces

/**
 * Interface Callback for returning back data in async operations
 */
interface Callback {

    /**
     * Method for returning a success result
     * @param res Boolean that represents the result of the operation
     */
    fun onSuccess(res: Boolean)

    /**
     * Method for returning an error result
     */
    fun onError()
}