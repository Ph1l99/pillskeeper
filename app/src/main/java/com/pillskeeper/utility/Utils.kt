package com.pillskeeper.utility

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.navigation.NavigationView
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.pillskeeper.R
import com.pillskeeper.data.LocalMedicine
import com.pillskeeper.data.ReminderMedicine
import com.pillskeeper.data.ReminderMedicineSort
import com.pillskeeper.datamanager.LocalDatabase
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.interfaces.Callback
import com.pillskeeper.notifier.NotifyPlanner
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.*
import kotlin.collections.HashMap

object Utils {

    private val regexUsername: Regex = "[A-Za-z]{2,30}".toRegex()
    private val regexPhoneNumber: Regex = "[0-9]{10}".toRegex()
    private val regexEmail: Regex =
        "[A-Za-z0-9.]{1,30}[A-Za-z0-9]{1,30}@[A-Za-z0-9]{2,30}\\.[A-Za-z]{2,10}".toRegex()

    lateinit var stdLayout: Drawable

    fun checkName(value: String): Boolean {
        Log.i(Log.DEBUG.toString(), "Utils: checkUsername() - Check value!")

        return (value.isNotEmpty() && value.matches(regexUsername))
    }

    fun checkPhoneNumber(phoneNumber: String): Boolean {
        Log.i(Log.DEBUG.toString(), "Utils: checkPhoneNumber() - Check value!")

        return (phoneNumber.isNotEmpty() && phoneNumber.matches(regexPhoneNumber))
    }

    fun checkEmail(email: String): Boolean {
        Log.i(Log.DEBUG.toString(), "Utils: checkEmail() - Check value!")

        return (email.isNotEmpty() && email.matches(regexEmail))
    }

    fun checkDate(dateSelected: Date, context: Context): Boolean {
        val calCurrent = Calendar.getInstance()
        val calSelected = Calendar.getInstance()
        calCurrent.time = Date(System.currentTimeMillis())
        calSelected.time = dateSelected
        if (calCurrent.get(Calendar.YEAR) > calSelected.get(Calendar.YEAR) || calCurrent.get(
                Calendar.MONTH
            ) > calSelected.get(Calendar.MONTH) ||
            calCurrent.get(Calendar.DAY_OF_YEAR) > calSelected.get(Calendar.DAY_OF_YEAR)
        ) {
            Toast.makeText(context, "Perfavore inserire una data corretta", Toast.LENGTH_LONG)
                .show()
            return false
        }
        return true
    }

    fun errorEditText(editText: EditText) {
        colorEditText(editText, true)
    }

    fun validEditText(editText: EditText) {
        colorEditText(editText, false)
    }

    private fun colorEditText(editText: EditText, isError: Boolean) {
        if (isError) {
            stdLayout = editText.background
            val gd = GradientDrawable()
            gd.setColor(Color.parseColor("#00ffffff"))
            gd.setStroke(2, Color.RED)
            editText.background = gd
        } else {
            editText.background = stdLayout
        }
    }

    fun dataNormalizationLimit(date: Date): Long {
        val cal: Calendar = Calendar.getInstance()
        cal.time = date

        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)

        return cal.time.time
    }

    fun insertNameMenu(view: NavigationView) {
        val username = view.getHeaderView(0).findViewById<TextView>(R.id.username_text_view_menu)
        username.text = LocalDatabase.readUser()?.name
    }

    fun getSortedListReminders(
        filterDate: Date,
        medList: LinkedList<LocalMedicine> = UserInformation.medicines
    ): LinkedList<ReminderMedicineSort> {

        var randomList: LinkedList<ReminderMedicineSort> = getListReminderNormalized(medList)

        randomList = LinkedList(randomList.filter { it.reminder.startingDay <= filterDate })
        randomList.sortBy { it.reminder.startingDay }

        return randomList
    }

    fun getListReminderNormalized(medList: LinkedList<LocalMedicine>): LinkedList<ReminderMedicineSort> {
        val randomList: LinkedList<ReminderMedicineSort> = LinkedList()
        medList.forEach {
            it.reminders?.forEach { reminder ->
                randomList.add(ReminderMedicineSort(it.name, it.medicineType, reminder))
            }
        }
        return convertSeqToDate(randomList)
    }

    private fun convertSeqToDate(list: LinkedList<ReminderMedicineSort>): LinkedList<ReminderMedicineSort> {
        val convertedList: LinkedList<ReminderMedicineSort> = LinkedList()

        list.forEach {
            if (it.reminder.startingDay == it.reminder.expireDate)
                convertedList.add(it)
            else
                convertedList.addAll(getDataListFromDays(it))
        }

        return convertedList
    }

    private fun getDataListFromDays(entry: ReminderMedicineSort): Collection<ReminderMedicineSort> {
        val returnedList: LinkedList<ReminderMedicineSort> = LinkedList()

        entry.reminder.days?.forEach {
            val calendar = Calendar.getInstance()
            calendar.time = Date(System.currentTimeMillis())

            while (calendar[Calendar.DAY_OF_WEEK] != it.dayNumber) {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }

            calendar.set(Calendar.MINUTE, entry.reminder.minutes)
            calendar.set(Calendar.HOUR_OF_DAY, entry.reminder.hours)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            returnedList.add(
                ReminderMedicineSort(
                    entry.medName,
                    entry.medType,
                    ReminderMedicine(
                        entry.reminder.dosage,
                        entry.reminder.minutes,
                        entry.reminder.hours,
                        calendar.time,
                        null,
                        calendar.time,
                        entry.reminder.additionNotes
                    )
                )
            )
        }
        return returnedList
    }

    fun serialize(obj: Any): ByteArray? {
        val out = ByteArrayOutputStream()
        val os = ObjectOutputStream(out)
        os.writeObject(obj)
        return out.toByteArray()
    }

    fun deserialize(data: ByteArray): Any? {
        val `in` = ByteArrayInputStream(data)
        val `is` = ObjectInputStream(`in`)
        return `is`.readObject()
    }

    fun isAlreadyExistingIntent(context: Context, itID: Int, intent: Intent): Boolean {
        return PendingIntent.getBroadcast(
            context,
            itID,
            intent,
            PendingIntent.FLAG_NO_CREATE
        ) != null
    }

    fun startNotifyService(context: Context) {
        Log.i(Log.DEBUG.toString(), "Utils: startNotifyService() - Function started")
        val service = Intent(context, NotifyPlanner::class.java)
        context.startService(service)
        Log.i(Log.DEBUG.toString(), "Utils: startNotifyService() - Function ended")
    }

    fun checkTextWords(text: String, language: String, job: Callback) {
        Log.i(Log.DEBUG.toString(), "Utils: checkTextWords() - function Started")
        val map = HashMap<String, Any>()
        map["text"] = text
        map["lang"] = language

        Firebase.functions.getHttpsCallable("checkToxic").call(map)
            .addOnSuccessListener {
                job.onSuccess(
                    checkMap(
                        it?.data as Map<String, Any>
                    )
                )
            }
            .addOnCanceledListener {
                job.onError()
            }
        Log.i(Log.DEBUG.toString(), "Utils: checkTextWords() - function Ended")
    }

    private fun checkMap(resultMap: Map<String, Any>): Boolean {
        for (result in resultMap)
            if (result.value as Double >= 0.8)
                return true
        return false
    }

    fun buildAlertDialog(context: Context, message: String, title: String, callback: Callback? = null): AlertDialog {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setIcon(R.drawable.pills_icon)
        builder.setPositiveButton("OK") { _, _ ->
            callback?.success()
        }

        return builder.create()
    }

    fun openMaps(activity: Activity, context: Context) {
        val REQUEST_POSITION_PERMISSION_ID = 1
        lateinit var fusedLocationClient: FusedLocationProviderClient

        val permissionAccessCoarseLocationApproved = ActivityCompat
            .checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED

        if (permissionAccessCoarseLocationApproved) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
            fusedLocationClient.lastLocation.addOnSuccessListener {
                val intent =
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(context.getString(R.string.query_location))
                    )
                intent.setPackage("com.google.android.apps.maps")
                context.startActivity(intent)

            }
        } else {
            // Make a request for foreground-only location access.
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_POSITION_PERMISSION_ID
            )
        }
    }
}