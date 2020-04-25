package com.pillskeeper.activity

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Toast
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
                DialogModeEnum.DELETE_APPOINTMENT -> {//TODO
                    titleDeleteTV.text = "Cancellare l'Appuntamento?"
                    "Appuntamento"
                }
                DialogModeEnum.DELETE_MEDICINE -> {
                    titleDeleteTV.text = "Cancellare l'Medicina?"
                    "Medicina"
                }
                else -> {
                    titleDeleteTV.text = "Cancellare l'Amico?"
                    "Amico"
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
                        Toast.makeText(
                            context,
                            "Non è stato possibile cancellare l'$item",
                            Toast.LENGTH_LONG
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
                        Toast.makeText(
                            context,
                            "Non è stato possibile cancellare la $item",
                            Toast.LENGTH_LONG
                        ).show()
                }
                else -> {
                    if (!UserInformation.deleteFriend(itemName))
                        Toast.makeText(
                            context,
                            "Non è stato possibile cancellare l'$item",
                            Toast.LENGTH_LONG
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