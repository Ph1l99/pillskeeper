package com.pillskeeper.activity

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.pillskeeper.R
import com.pillskeeper.datamanager.UserInformation
import com.pillskeeper.enums.DialogModeEnum
import com.pillskeeper.notifier.NotifyPlanner
import com.pillskeeper.utility.Utils
import kotlinx.android.synthetic.main.dialog_appointment.*

class GenericDeleteDialog(
    context: Context,
    private val itemName: String,
    private val dialogModeEnum: DialogModeEnum
) : Dialog(context) {

    init {
        setCancelable(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_appointment)

        val item =
            when (dialogModeEnum) {
                DialogModeEnum.DELETE_APPOINTMENT -> {
                    titleDeleteTV.text = context.getString(R.string.deleteApp)
                    context.getString(R.string.notificationTitleApp)
                }
                DialogModeEnum.DELETE_MEDICINE -> {
                    titleDeleteTV.text = context.getString(R.string.deleteMed)
                    context.getString(R.string.notificationTextMed)
                }
                else -> {
                    titleDeleteTV.text = context.getString(R.string.deleteFriend)
                    context.getString(R.string.friend)
                }

            }

        nameTitleTV.text = itemName

        deleteConfirm.setOnClickListener {
            when (dialogModeEnum) {
                DialogModeEnum.DELETE_APPOINTMENT -> {
                    val appointment = UserInformation.getSpecificAppointment(itemName)
                    if (UserInformation.deleteAppointment(itemName))
                        NotifyPlanner.remove(
                            context,
                            appointment!!
                        )
                    else
                        Utils.buildAlertDialog(
                            context,
                            context.getString(R.string.genericDelete) + " " + item,
                            context.getString(R.string.message_title)
                        ).show()
                }
                DialogModeEnum.DELETE_MEDICINE -> {
                    val reminderMedicineSort = Utils.getListReminderNormalized(
                        UserInformation.getSpecificMedicine(itemName)!!
                    )
                    if (UserInformation.deleteMedicine(itemName))
                        reminderMedicineSort.forEach {
                            NotifyPlanner.remove(
                                context,
                                it
                            )
                        }
                    else
                        Utils.buildAlertDialog(
                            context,
                            context.getString(R.string.genericDelete) + " " + item,
                            context.getString(R.string.message_title)
                        ).show()
                }
                else -> {
                    if (!UserInformation.deleteFriend(itemName))
                        Utils.buildAlertDialog(
                            context,
                            context.getString(R.string.genericDelete) + " " + item,
                            context.getString(R.string.message_title)
                        ).show()
                }
            }

            dismiss()
        }

        deleteDeny.setOnClickListener {
            dismiss()
        }
    }

}