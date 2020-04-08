package com.pillskeeper.utility.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pillskeeper.R;
import com.pillskeeper.data.RemoteMedicine;
import com.pillskeeper.datamanager.UserInformation;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MedicineRemoteCardAdapter extends RecyclerView.Adapter<MedicineRemoteCardAdapter.MedCardHolderJava> {

    private List<RemoteMedicine> medList;
    private OnItemClickListener mListener;

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
            textView = itemView.findViewById(R.id.medText);
            imageView = itemView.findViewById(R.id.forwardIcon);
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

    public MedicineRemoteCardAdapter(List<RemoteMedicine> medList) {
        this.medList = medList;
    }


    @NotNull
    @Override
    public MedCardHolderJava onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.remote_med_card_item, parent, false);
        return new MedCardHolderJava(v, mListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MedCardHolderJava holder, int position) {
        RemoteMedicine currentItem = medList.get(position);
        holder.imageView.setImageResource(R.drawable.ic_med_list_forward);
        holder.textView.setText(currentItem.getName() + "\n" + UserInformation.context.getString(currentItem.getMedicineType().getText()));
    }

    @Override
    public int getItemCount() {
        return medList.size();
    }
}
