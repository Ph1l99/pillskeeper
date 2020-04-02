package com.pillskeeper.activity.medicine

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.common.base.Splitter
import com.pillskeeper.R
import com.pillskeeper.data.Friend
import com.pillskeeper.data.LocalMedicine
import com.pillskeeper.data.RemoteMedicine
import com.pillskeeper.data.User
import com.pillskeeper.datamanager.LocalDatabase
import com.pillskeeper.enums.MedicineTypeEnum
import com.pillskeeper.enums.RelationEnum
import com.pillskeeper.utility.Mail
import com.pillskeeper.utility.MedCardAdapter
import kotlinx.android.synthetic.main.activity_finished_medicines.*
import java.util.*

class FinishedMedicinesActivity : AppCompatActivity() {

    companion object {
        const val MINIMUM_PILLS = 4
    }

    private lateinit var mAdapter: MedCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finished_medicines)
        checkAlmostFinishedMedicines()
    }


    private fun checkAlmostFinishedMedicines() {
        //val list = LocalDatabase.readMedicineList()
        /*val outputList = LinkedList(list.filter {
            it.remainingPills <= MINIMUM_PILLS
        })*/
        var outputList: LinkedList<LocalMedicine> = LinkedList()
        outputList.add(LocalMedicine("Ciao", MedicineTypeEnum.PILLS, 20f, 20f, null, "1234"))
        outputList.add(
            LocalMedicine(
                "Bella la Menta",
                MedicineTypeEnum.PILLS,
                20f,
                20f,
                null,
                "1256"
            )
        )

        if (outputList.isEmpty()) {
            //TODO fare qualcosa
        } else {
            displayMedicines(outputList)
        }
    }

    private fun displayMedicines(list: LinkedList<LocalMedicine>) {
        mAdapter = MedCardAdapter(list)
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        mAdapter.setOnItemClickListener {
            val medicine = list[it]
            openDialog(medicine)
        }
    }

    private fun openDialog(localMedicine: LocalMedicine) {
        var listFriends: LinkedList<String> = LinkedList()

        lateinit var selectableFriends: Array<String>

        var chosenFriends: LinkedList<String> = LinkedList()

        val localFriendList = LocalDatabase.readFriendList()

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

        val completeMail = Mail.composeMail(
            RemoteMedicine(medicine.name, medicine.id, medicine.medicineType),
            User("1234", "filippo", "ciao", "filippo.ciao@ciao.com")
        )
        val i = Intent(Intent.ACTION_SEND)

        if (completeMail != null) {
            i.type = "message/rfc822"
            i.putExtra(Intent.EXTRA_EMAIL, recipients.toArray())
            i.putExtra(Intent.EXTRA_SUBJECT, completeMail.mailsubject)
            i.putExtra(Intent.EXTRA_TEXT, completeMail.mailBody)
        }
        startActivity(Intent.createChooser(i, R.string.sendMail.toString() + "..."))
    }
}
