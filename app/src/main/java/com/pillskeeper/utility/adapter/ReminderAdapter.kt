package com.pillskeeper.utility.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pillskeeper.R
import com.pillskeeper.activity.GenericDeleteDialog
import com.pillskeeper.data.ReminderMedicine
import com.pillskeeper.datamanager.UserInformation
import kotlinx.android.synthetic.main.reminder_card_item.view.*
import java.util.*

class ReminderAdapter constructor(list: List<ReminderMedicine>) :
    RecyclerView.Adapter<ReminderAdapter.ReminderHolder>() {

    var onItemClick: ((ReminderMedicine) -> Unit)? = null
    private var remList = list

    inner class ReminderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.reminderText
        val imageView: ImageView = itemView.modifyIcon

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(remList[adapterPosition])
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderHolder {
        return ReminderHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.reminder_card_item, parent, false)
        )
    }

    override fun getItemCount(): Int = remList.size

    override fun onBindViewHolder(holder: ReminderHolder, position: Int) {
        val currentItem = remList[position]
        holder.imageView.setImageResource(R.drawable.ic_modify)
        val text = StringBuilder()
        if (currentItem.isSingleDayRem()) {
            val calendar = Calendar.getInstance()
            calendar.time = currentItem.startingDay
            text.append(calendar.get(Calendar.DAY_OF_MONTH)).append("/")
                .append(calendar.get(Calendar.MONTH) + 1).append("\n")
            text.append(UserInformation.context.getString(R.string.time)).append(" ")
                .append(currentItem.hours)
                .append(":").append(currentItem.minutes).append("\n")
        } else {
            if (currentItem.startingDay.after(Date())) {
                val calendar = Calendar.getInstance()
                calendar.time = currentItem.startingDay //TODO gg/mm/yyyy
                text.append(calendar.get(Calendar.DAY_OF_MONTH)).append("/")
                    .append(calendar.get(Calendar.MONTH) + 1).append(" - ")
            }
            text.append(currentItem.dayStringify()).append("\n")
            text.append(UserInformation.context.getString(R.string.time)).append(" ")
                .append(currentItem.hours)
                .append(":").append(currentItem.minutes).append("\n")
        }
        text.append(UserInformation.context.getString(R.string.quantity)).append(" ")
            .append(currentItem.dosage)
        holder.textView.text = text.toString()
    }
}