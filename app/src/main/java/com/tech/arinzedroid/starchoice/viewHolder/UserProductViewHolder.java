package com.tech.arinzedroid.starchoice.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tech.arinzedroid.starchoice.R;
import com.tech.arinzedroid.starchoice.interfaces.UserProductClickedInterface;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    @BindView(R.id.amt_paid)
    public TextView amtPaidTv;
    @BindView(R.id.amt_remaining)
    public TextView amtRemainingTv;
    @BindView(R.id.date)
    public TextView dateTv;
    @BindView(R.id.product_name)
    public TextView productNameTv;
    @BindView(R.id.product_price)
    public TextView productPriceTv;
    @BindView(R.id.card_view)
    View cardView;

    private UserProductClickedInterface userProductClickedInterface;

    public UserProductViewHolder(View itemView, UserProductClickedInterface userProductClickedInterface) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        cardView.setOnClickListener(this);
        this.userProductClickedInterface = userProductClickedInterface;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.card_view)
            userProductClickedInterface.onProductClick(this.getLayoutPosition());
    }
}
