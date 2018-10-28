package com.tech.arinzedroid.starchoice.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tech.arinzedroid.starchoice.R;
import com.tech.arinzedroid.starchoice.models.TransHistoryModel;
import com.tech.arinzedroid.starchoice.util.DateTimeUtils;
import com.tech.arinzedroid.starchoice.util.FormatUtil;
import com.tech.arinzedroid.starchoice.viewHolder.TransHistoryViewHolder;

import java.util.List;

public class TransHistoryAdapter extends RecyclerView.Adapter<TransHistoryViewHolder> {

    private List<TransHistoryModel> transHistories;

    public TransHistoryAdapter(List<TransHistoryModel> transHistories){
        this.transHistories = transHistories;
    }

    public void clearAll(){
        transHistories.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TransHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_history_items,parent,false);
        return new TransHistoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TransHistoryViewHolder holder, int position) {
        TransHistoryModel data = transHistories.get(position);
        holder.statusTv.setText(data.getStatus());
        holder.amountTv.setText(FormatUtil.formatPrice(data.getAmount()));
        holder.dateTv.setText(DateTimeUtils.parseDateTime(data.getDateCreated()));
    }

    @Override
    public int getItemCount() {
        return transHistories.size();
    }
}
