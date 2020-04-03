package com.pillskeeper.activity.friend

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.pillskeeper.R
import com.pillskeeper.activity.GenericDeleteDialog
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.enums.DialogModeEnum
import com.pillskeeper.utility.Menu
import kotlinx.android.synthetic.main.content_friend_list.*

class FriendListActivity : AppCompatActivity() {

    private lateinit var friendListView : ListView
    private lateinit var listName : ArrayList<String>
    private lateinit var adapter : ArrayAdapter<String>

    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_list)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        //set toolbar
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        //set menu
        val menu = Menu(toolbar, drawerLayout, navView, this)
        menu.createMenu()

        initList()

        addFriendFab.setOnClickListener {
            NewFriendDialog(this, DialogModeEnum.CREATE_NEW_FRIEND, null).show()
        }

        friendListView.setOnItemLongClickListener { _, _, position, _ ->
            GenericDeleteDialog(this,UserInformation.friends[position].name,DialogModeEnum.DELETE_FRIEND).show()
            return@setOnItemLongClickListener true
        }

        friendListView.setOnItemClickListener { _, _, position, _ ->
            NewFriendDialog(this, DialogModeEnum.EDIT_FRIEND,UserInformation.friends[position]).show()
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        initList()
    }

    private fun initList(){
        Log.i(Log.DEBUG.toString(), "FriendListActivity - initList() Started")

        friendListView = findViewById(R.id.friendListView)
        listName = ArrayList(UserInformation.friends.size)

        for (friend in UserInformation.friends) {
            listName.add(friend.name + "  -  " + friend.relationEnum.toString())
        }

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listName)
        friendListView.adapter = adapter

        Log.i(Log.DEBUG.toString(), "FriendListActivity - initList() Ended")
    }

}
