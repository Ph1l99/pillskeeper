package com.pillskeeper.utility.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pillskeeper.R
import com.pillskeeper.data.LocalMedicine
import com.pillskeeper.datamanager.UserInformation
import kotlinx.android.synthetic.main.med_card_item.view.*

class LocalMedicineAdapter(list: List<LocalMedicine>) :
    RecyclerView.Adapter<LocalMedicineAdapter.LocalMedicineHolder>() {

    private var medList: List<LocalMedicine> = list
    private var isMedicineLocaleListActivity: Boolean = false
    var onItemClick: ((LocalMedicine) -> Unit)? = null

    constructor(list: List<LocalMedicine>, isMed: Boolean) : this(list) {
        medList = list
        isMedicineLocaleListActivity = isMed
    }

    inner class LocalMedicineHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.finishedMedText
        val imageView: ImageView = itemView.sendMailIcon

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(medList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalMedicineHolder {
        return LocalMedicineHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.med_card_item, parent, false)
        )
    }

    override fun getItemCount(): Int = medList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: LocalMedicineHolder, position: Int) {
        val currentItem = medList[position]
        holder.textView.text =
            currentItem.name + "\n" + UserInformation.context.getString(currentItem.medicineType.text)
        if (isMedicineLocaleListActivity) {
            holder.imageView.setImageResource(R.drawable.ic_med_list_forward)
            holder.textView.setTextColor(
                UserInformation.context.resources.getColor(
                    R.color.colorPrimaryDark,
                    null
                )
            )
        } else {
            holder.imageView.setImageResource(R.drawable.ic_mail_outline)
            holder.textView.setTextColor(
                UserInformation.context.resources.getColor(
                    R.color.red,
                    null
                )
            )
        }
    }

}