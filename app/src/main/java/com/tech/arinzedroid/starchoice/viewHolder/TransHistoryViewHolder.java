package com.tech.arinzedroid.starchoice.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tech.arinzedroid.starchoice.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransHistoryViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.amount)
    public TextView amountTv;
    @BindView(R.id.date)
    public TextView dateTv;
    @BindView(R.id.status)
    public TextView statusTv;

    public TransHistoryViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
