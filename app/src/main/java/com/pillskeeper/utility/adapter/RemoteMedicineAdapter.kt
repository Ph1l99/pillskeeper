package com.pillskeeper.utility.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pillskeeper.R
import com.pillskeeper.data.RemoteMedicine
import com.pillskeeper.datamanager.UserInformation
import kotlinx.android.synthetic.main.remote_med_card_item.view.*

class RemoteMedicineAdapter constructor(list: List<RemoteMedicine>) :
    RecyclerView.Adapter<RemoteMedicineAdapter.RemoteMedicineHolder>() {
    var onItemClick: ((RemoteMedicine) -> Unit)? = null
    private var medList: List<RemoteMedicine> = list

    inner class RemoteMedicineHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.medText
        val imageView: ImageView = itemView.forwardIcon

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(medList[adapterPosition])
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RemoteMedicineHolder {
        return RemoteMedicineHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.remote_med_card_item, parent, false)
        )
    }

    override fun getItemCount(): Int = medList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RemoteMedicineHolder, position: Int) {
        val currentItem = medList[position]
        holder.imageView.setImageResource(R.drawable.ic_med_list_forward)
        holder.textView.text =
            currentItem.name + "\n" + UserInformation.context.getString(currentItem.medicineType.text)
    }

}