package com.pillskeeper.utility.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pillskeeper.R;
import com.pillskeeper.data.ReminderMedicine;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReminderCardAdapter extends RecyclerView.Adapter<ReminderCardAdapter.RemCardHolderJava> {

    private List<ReminderMedicine> remList;
    private ReminderCardAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    static class RemCardHolderJava extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        RemCardHolderJava(View itemView, OnItemClickListener listener) {
            super(itemView);
            textView = itemView.findViewById(R.id.reminderText);
            imageView = itemView.findViewById(R.id.modifyIcon);
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                        listener.onItemClick(position);

                }
            });
        }

    }

    public ReminderCardAdapter(List<ReminderMedicine> medList) { this.remList = medList; }


    @NotNull
    @Override
    public RemCardHolderJava onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_card_adapter, parent, false);
        return new RemCardHolderJava(v, mListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RemCardHolderJava holder, int position) {
        ReminderMedicine item = remList.get(position);
        holder.imageView.setImageResource(R.drawable.ic_modify);
        holder.itemView.setBackgroundResource(R.drawable.shape_card);
        StringBuilder text = new StringBuilder();
        if(item.getStartingDay().equals(item.getExpireDate())){
            Calendar cal = Calendar.getInstance();
            cal.setTime(item.getStartingDay());
            text.append("Da: ").append(cal.get(Calendar.DAY_OF_MONTH) + 1).append("/").append(cal.get(Calendar.MONTH)).append("/").append(cal.get(Calendar.YEAR));
            cal.setTime(item.getExpireDate());
            text.append("  A: ").append(cal.get(Calendar.DAY_OF_MONTH) + 1).append("/").append(cal.get(Calendar.MONTH)).append("/").append(cal.get(Calendar.YEAR));
        } else {
            if (item.getStartingDay().after(new Date()))
                text.append(item.getStartingDay()).append(" - ");
            text.append(item.getHours()).append(":").append(item.getMinutes()).append(" - ");
            text.append(item.dayStringify());
        }
        text.append("\nQuantità: ").append(item.getDosage());
        holder.textView.setText(text.toString());
    }

    @Override
    public int getItemCount() {
        return remList.size();
    }
}
