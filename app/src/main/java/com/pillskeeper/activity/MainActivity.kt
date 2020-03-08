package com.pillskeeper.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pillskeeper.R

class MainActivity : AppCompatActivity() {

    var isFirstLogin : Boolean = true;

    override fun onCreate(savedInstanceState: Bundle?) {


        readFirstLogin();

        if(isFirstLogin){
            //load first login activity and save username
        } else {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
        }
    }

    fun readFirstLogin(){

    }

}
