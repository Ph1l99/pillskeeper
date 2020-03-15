package com.pillskeeper.activity.friend

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.pillskeeper.R
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.enums.DialogModeEnum
import kotlinx.android.synthetic.main.activity_friend_list.*

class FriendListActivity : AppCompatActivity() {

    private lateinit var friendListView : ListView
    private lateinit var listName : ArrayList<String>
    private lateinit var adapter : ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_list)

        initList()

        addFriendFab.setOnClickListener {
            NewFriendActivity(this, DialogModeEnum.CREATE_NEW_FRIEND, null).show()
        }

        friendListView.setOnItemClickListener { _, _, position, _ ->
            NewFriendActivity(this, DialogModeEnum.EDIT_FRIEND,UserInformation.friends[position]).show()
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        initList()
    }

    private fun initList(){
        Log.w(Log.DEBUG.toString(), "FriendListActivity - initList() Started")

        friendListView = findViewById(R.id.friendListView)
        listName = ArrayList(UserInformation.friends.size)

        for (friend in UserInformation.friends) {
            listName.add(friend.name + "  -  " + friend.relationEnum.toString())
        }

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listName)
        friendListView.adapter = adapter

        Log.w(Log.DEBUG.toString(), "FriendListActivity - initList() Ended")
    }

}
