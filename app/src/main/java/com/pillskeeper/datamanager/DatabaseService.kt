package com.pillskeeper.datamanager

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class DatabaseService:Service() {
    private val binder = DatabaseBinder()

    //TODO public methods

    inner class DatabaseBinder:Binder(){
        fun getService():DatabaseService=this@DatabaseService
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }
}