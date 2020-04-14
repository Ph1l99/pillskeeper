package com.pillskeeper.utility.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pillskeeper.R;
import com.pillskeeper.data.LocalMedicine;
import com.pillskeeper.datamanager.UserInformation;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.ResourceBundle;

public class MedicineLocaleCardAdapter extends RecyclerView.Adapter<MedicineLocaleCardAdapter.MedCardHolderJava> {
    private List<LocalMedicine> medList;
    private OnItemClickListener mListener;
    private Boolean isMedicineLocaleListActivity;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    static class MedCardHolderJava extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        MedCardHolderJava(View itemView, OnItemClickListener listener) {
            super(itemView);
            textView = itemView.findViewById(R.id.finishedMedText);
            imageView = itemView.findViewById(R.id.sendMailIcon);
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }

    }

    public MedicineLocaleCardAdapter(List<LocalMedicine> medList) {
        this.medList = medList;
        this.isMedicineLocaleListActivity = false;
    }

    public MedicineLocaleCardAdapter(List<LocalMedicine> medList, boolean bool) {
        this.medList = medList;
        this.isMedicineLocaleListActivity = bool;
    }


    @NotNull
    @Override
    public MedCardHolderJava onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.med_card_item, parent, false);
        return new MedCardHolderJava(v, mListener);
    }



    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(MedCardHolderJava holder, int position) {
        if(isMedicineLocaleListActivity) {
            LocalMedicine currentItem = medList.get(position);
            holder.imageView.setImageResource(R.drawable.ic_med_list_forward);
            holder.textView.setTextColor(R.color.black);
            holder.textView.setText(currentItem.getName() + "\n" + UserInformation.context.getString(currentItem.getMedicineType().getText()));
            holder.itemView.setBackgroundResource(R.drawable.shape_card);
        } else {
            LocalMedicine currentItem = medList.get(position);
            holder.imageView.setImageResource(R.drawable.ic_mail_outline);
            holder.textView.setText(currentItem.getName() + "\n" + UserInformation.context.getString(currentItem.getMedicineType().getText()));
            holder.itemView.setBackgroundResource(R.drawable.shape_card);
        }
    }

    @Override
    public int getItemCount() {
        return medList.size();
    }
}
