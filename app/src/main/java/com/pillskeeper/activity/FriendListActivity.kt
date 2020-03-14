package com.pillskeeper.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import com.pillskeeper.R
import com.pillskeeper.datamanager.UserInformation
import kotlinx.android.synthetic.main.activity_friend_list.*

class FriendListActivity : AppCompatActivity() {

    private lateinit var friendListView : ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_list)

        initList()

        addFriendFab.setOnClickListener {
            NewFriendActivity(this).show()
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        initList()
    }



    private fun initList(){
        Log.w(Log.DEBUG.toString(), "FriendListActivity - initList() Started")
        friendListView = findViewById(R.id.friendListView)
        val listItems = arrayOfNulls<String>(UserInformation.friends.size)

        for (i in 0 until UserInformation.friends.size) {
            val friend = UserInformation.friends[i]
            listItems[i] = friend.name + "  -  " + friend.relationEnum.toString()
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)
        friendListView.adapter = adapter
        Log.w(Log.DEBUG.toString(), "FriendListActivity - initList() Ended")
    }

}
