package com.pillskeeper.activity.medicine

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.google.common.base.Splitter
import com.pillskeeper.R
import com.pillskeeper.data.Friend
import com.pillskeeper.data.LocalMedicine
import com.pillskeeper.data.RemoteMedicine
import com.pillskeeper.datamanager.LocalDatabase
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.utility.Mail
import com.pillskeeper.utility.adapter.MedicineLocaleCardAdapter
import kotlinx.android.synthetic.main.activity_finished_medicines.*
import java.util.*

class FinishedMedicinesActivity : AppCompatActivity() {

    companion object {
        const val MINIMUM_QTY = 0.20
    }

    private lateinit var mAdapter: MedicineLocaleCardAdapter
    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finished_medicines)
        //TODO ADD menu!!!!!!!!!!!!
        /*drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        //set toolbar
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val menu = Menu(toolbar, drawerLayout, navView, this)
        menu.createMenu()*/


        checkAlmostFinishedMedicines()
    }


    private fun checkAlmostFinishedMedicines() {
        val list = UserInformation.medicines

        val outputList = LinkedList(list.filter {
            it.remainingQty <= it.totalQty * MINIMUM_QTY
        })

        if (outputList.isEmpty()) {
            nothingImage.visibility = (View.VISIBLE)
            recyclerView.visibility = (View.GONE)
        } else {
            nothingImage.visibility = (View.GONE)
            recyclerView.visibility = (View.VISIBLE)
            displayMedicines(outputList)
        }
    }

    private fun displayMedicines(list: LinkedList<LocalMedicine>) {
        mAdapter = MedicineLocaleCardAdapter(list)
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        mAdapter.setOnItemClickListener {
            val medicine = list[it]
            openDialog(medicine)
        }
    }

    private fun openDialog(localMedicine: LocalMedicine) {
        val listFriends: LinkedList<String> = LinkedList()

        lateinit var selectableFriends: Array<String>

        val chosenFriends: LinkedList<String> = LinkedList()

        val localFriendList = UserInformation.friends

        for (friend in localFriendList) {
            listFriends.add(friend.name + " " + friend.surname)
        }
        selectableFriends = Array(listFriends.size) { "" }
        listFriends.toArray(selectableFriends)
        val selectedItems = Array(listFriends.size) { false }

        val builder = AlertDialog.Builder(this)
        builder.setMultiChoiceItems(selectableFriends, null) { _, which, isChecked ->
            selectedItems[which] = isChecked
        }
        builder.setPositiveButton(R.string.sendMail) { _, _ ->
            for (i in selectedItems.indices) {
                if (selectedItems[i]) {
                    chosenFriends.add(extractMail(selectableFriends[i], localFriendList))
                }
            }
            sendEmail(localMedicine, chosenFriends)
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun extractMail(friend: String, listFriends: LinkedList<Friend>): String {
        val splittedName = Splitter.on(" ").splitToList(friend)
        val name = splittedName[0]
        val surname = splittedName[1]
        var email: String? = null
        for (element in listFriends) {
            if (element.name == name && element.surname == surname) {
                email = element.email
                break
            }
        }
        return email ?: ""
    }

    private fun sendEmail(medicine: LocalMedicine, recipients: LinkedList<String>) {

        val completeMail = LocalDatabase.readUser()?.let {
            Mail.composeMail(
                RemoteMedicine(medicine.name, medicine.id, medicine.medicineType),
                it
            )
        }
        val mailto = Array(recipients.size) { "" }
        recipients.toArray(mailto)

        if (completeMail != null) {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "message/rfc822"
            i.putExtra(Intent.EXTRA_EMAIL, mailto)
            i.putExtra(Intent.EXTRA_SUBJECT, completeMail.mailsubject)
            i.putExtra(Intent.EXTRA_TEXT, completeMail.mailBody)
            startActivity(Intent.createChooser(i, getString(R.string.sendMail) + "..."))
        }
    }

}
