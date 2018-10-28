package com.tech.arinzedroid.starchoice.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tech.arinzedroid.starchoice.R;
import com.tech.arinzedroid.starchoice.interfaces.UserClickedInterface;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UsersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    @BindView(R.id.card_layout)
    public View cardLayout;
    @BindView(R.id.date)
    public TextView dateTV;
    @BindView(R.id.amount)
    public TextView amountTv;
    @BindView(R.id.name)
    public TextView nameTv;
    @BindView(R.id.serial)
    public TextView serialTv;
    private UserClickedInterface userClickedInterface;

    public UsersViewHolder(View itemView,UserClickedInterface userClickedInterface) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        cardLayout.setOnClickListener(this);
        this.userClickedInterface = userClickedInterface;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.card_layout)
            userClickedInterface.userData(this.getLayoutPosition());
    }
}
