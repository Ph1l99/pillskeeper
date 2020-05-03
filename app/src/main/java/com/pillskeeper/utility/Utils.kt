package com.pillskeeper.utility

import android.Manifest
import android.app.Activity
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
import com.pillskeeper.enums.MedicineTypeEnum
import com.pillskeeper.interfaces.Callback
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.*
import kotlin.collections.HashMap

object Utils {

    private val regexUsername = "[A-Za-z]{2,30}".toRegex()
    private val regexPhoneNumber = "[0-9]{10}".toRegex()
    private val regexEmail = "[A-Za-z0-9.]{1,30}[A-Za-z0-9]{1,30}@[A-Za-z0-9]{2,30}\\.[A-Za-z]{2,10}".toRegex()

    lateinit var stdLayout: Drawable

    /**
     * Function to check for wrong char inside userText string
     * It made check with regex pattern
     * @param value string we need to check
     * @return result of check
     */
    fun checkName(value: String): Boolean {
        Log.i(Log.DEBUG.toString(), "Utils: checkUsername() - Check value!")

        return (value.isNotEmpty() && value.matches(regexUsername))
    }

    /**
     * Function to check for wrong char inside userText string
     * It made check with regex pattern
     * @param phoneNumber stringPhoneNumber we need to check
     * @return result of check
     */
    fun checkPhoneNumber(phoneNumber: String): Boolean {
        Log.i(Log.DEBUG.toString(), "Utils: checkPhoneNumber() - Check value!")

        return (phoneNumber.isNotEmpty() && phoneNumber.matches(regexPhoneNumber))
    }

    /**
     * Function to check for wrong char inside userText string
     * It made check with regex pattern
     * @param email stringEmail we need to check
     * @return result of check
     */
    fun checkEmail(email: String): Boolean {
        Log.i(Log.DEBUG.toString(), "Utils: checkEmail() - Check value!")

        return (email.isNotEmpty() && email.matches(regexEmail))
    }

    /**
     * Function used as bridge, it makes code more readable
     * error case
     * @param editText the editText we need to change the color
     */
    fun errorEditText(editText: EditText) {
        colorEditText(editText, true)
    }

    /**
     * Function used as bridge, it makes code more readable
     * valid case
     * @param editText the editText we need to change the color
     */
    fun validEditText(editText: EditText) {
        colorEditText(editText, false)
    }

    /**
     * Function used to change borderColor of editText
     * @param editText the editText we need to change the color
     * @param isError flag used to chang the color based on an error or not
     */
    private fun colorEditText(editText: EditText, isError: Boolean) {
        if (isError) {
            stdLayout = editText.background
            val gd = GradientDrawable()
            gd.setColor(Color.parseColor("#00ffffff"))
            gd.setStroke(4, Color.RED)
            editText.background = gd
        } else {
            editText.background = stdLayout
        }
    }

    /**
     * Function used to normalized a date to the ending day time
     * Like : dd/mm/aaaa - 23:59:59:9999 (the really end date)  -  (it keep the initial day, month and year)
     * @param date the date we want to normalize (90% of cases is today)
     * @return it return the long version of time (time in millis)
     */
    fun dataNormalizationLimit(date: Date = Date()): Long {
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

    /**
     * Function that get the list normalized and sort it and remove out of date reminder
     * Used in the homePage and to plan alarms
     * @param filterDate used to remove future reminders
     * @param medList medicine list, it process them all
     * @return return a sorted-daily reminderList
     */
    fun getSortedListReminders(
        filterDate: Date,
        medList: LinkedList<LocalMedicine> = UserInformation.medicines
    ): LinkedList<ReminderMedicineSort> {

        var randomList: LinkedList<ReminderMedicineSort> = getListReminderNormalized(medList)

        randomList = LinkedList(randomList.filter { it.reminder.startingDay <= filterDate })
        randomList.sortBy { it.reminder.startingDay }

        return randomList
    }

    /**
     * Function used to get a normalized only a single reminder for a specific medicine
     * It's useless to process all reminders of a medicine, it reduce overflow
     * @param medName medicine name we want to process
     * @param medType param used to build the reminderMedicine normalized
     * @param reminder The item we want to process
     * @return the same as @getListReminderNormalized (normalized reminderList)
     * */
    fun getSingleReminderListNormalized(
        medName: String,
        medType: MedicineTypeEnum,
        reminder: ReminderMedicine
    ): LinkedList<ReminderMedicineSort> {
        val reminderSortList = LinkedList<ReminderMedicineSort>()
        reminderSortList.add(ReminderMedicineSort(medName, medType, reminder))
        return convertSeqToDate(reminderSortList)
    }

    /**
     * Function used to get a normalized reminderList only for a specific medicine
     * @param localMedicine medicine we want to process
     * @return the same as @getListReminderNormalized (normalized reminderList)
     * */
    fun getListReminderNormalized(localMedicine: LocalMedicine): LinkedList<ReminderMedicineSort> {
        val medList = LinkedList<LocalMedicine>()
        medList.add(localMedicine)
        return LinkedList(
            getListReminderNormalized(medList)
                .filter { it.reminder.startingDay < Date(dataNormalizationLimit()) }
        )
    }

    /**
     * Because of reminder types, we need to normalize all reminders before create the alarms
     * The function process all reminders and for each, if they are SEQUENCE type it normalize them all
     * @param medList medicine list (function can be used with multiple medicine)
     * @return a normalized reminderList
     * */
    private fun getListReminderNormalized(medList: LinkedList<LocalMedicine>): LinkedList<ReminderMedicineSort> {
        val randomList: LinkedList<ReminderMedicineSort> = LinkedList()
        medList.forEach {
            it.reminders?.forEach { reminder ->
                randomList.add(ReminderMedicineSort(it.name, it.medicineType, reminder))
            }
        }
        return convertSeqToDate(randomList)
    }

    /**
     * Function used to convert a SEQUENCE reminder to a dateReminder (really more easy to process)
     * @param list take a mixed (SEQ and DATE) list od reminder
     * @return a list composed by only DATE reminder. The size of list returned >= than list in input
     */
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

    /**
     * Function that take a SEQ reminder and convert it to a list of DATE reminder
     * @param entry the specific reminder we need to process
     * @return the reminder converted to a list of DATE reminder
     */
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

    /**
     * Function used to serialized a generic obj into an array of byte.
     * We need it to send a specific item with Intent
     * @param obj item we want to serialize
     * @return the object serialized
     */
    fun serialize(obj: Any): ByteArray? {
        val out = ByteArrayOutputStream()
        val os = ObjectOutputStream(out)
        os.writeObject(obj)
        return out.toByteArray()
    }

    /**
     * Function used to rebuild an array of byte into a generic obj
     * We need it to get a specific item from an Intent
     * @param data array of byte we want to deserialize
     * @return the object deserialized
     */
    fun deserialize(data: ByteArray): Any? {
        val `in` = ByteArrayInputStream(data)
        val `is` = ObjectInputStream(`in`)
        return `is`.readObject()
    }

    /**
     * Method for checking rude language during medicine insertion
     * @param text The text that has be analyzed
     * @param language The language in the UTF 2 digit format
     * @param job The callback for the return value
     */
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

    /**
     * Method for checking the results
     * @param resultMap The map (JSON) that returns from the checkToxicWords()
     */
    private fun checkMap(resultMap: Map<String, Any>): Boolean {
        for (result in resultMap)
            if (result.value as Double >= 0.8)
                return true
        return false
    }

    /**
     * Method for building a generic AlertDialog
     * @param context The context that the dialog has to use
     * @param message The dialog message
     * @param title The dialog title
     * @param callback The optional callback for returning the result
     */
    fun buildAlertDialog(
        context: Context,
        message: String,
        title: String = context.getText(R.string.message_title).toString(),
        callback: Callback? = null
    ): AlertDialog {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setIcon(R.drawable.pills_icon)
        builder.setPositiveButton("OK") { _, _ ->
            callback?.onSuccess(true)
        }
        return builder.create()
    }

    /**
     * Function that opens the Maps application by locating the user with GPS
     */
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
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)

            }
        } else {
            // Make a request for foreground-only location access.
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_POSITION_PERMISSION_ID
            )
        }
    }
}
