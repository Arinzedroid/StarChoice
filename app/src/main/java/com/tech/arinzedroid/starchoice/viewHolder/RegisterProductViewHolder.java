package com.tech.arinzedroid.starchoice.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tech.arinzedroid.starchoice.R;
import com.tech.arinzedroid.starchoice.interfaces.UserClickedProduct;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    @BindView(R.id.name)
    public TextView nameTv;
    @BindView(R.id.price)
    public TextView priceTv;
    @BindView(R.id.remove)
    public ImageButton removeBtn;
    private UserClickedProduct userClickedProduct;

    public RegisterProductViewHolder(View itemView, UserClickedProduct userClickedProduct) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        removeBtn.setOnClickListener(this);
        this.userClickedProduct = userClickedProduct;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.remove)
            userClickedProduct.remove(this.getLayoutPosition());
    }
}
