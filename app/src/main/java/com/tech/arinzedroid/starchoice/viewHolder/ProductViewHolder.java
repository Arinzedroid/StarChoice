package com.tech.arinzedroid.starchoice.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tech.arinzedroid.starchoice.R;
import com.tech.arinzedroid.starchoice.interfaces.RemoveProductInterface;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    @BindView(R.id.name)
    public TextView nameTv;
    @BindView(R.id.price)
    public TextView priceTv;
    @BindView(R.id.remove)
    ImageButton removeButton;

    private RemoveProductInterface removeProductInterface;

    public ProductViewHolder(View itemView, RemoveProductInterface removeProductInterface) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        removeButton.setOnClickListener(this);
        this.removeProductInterface = removeProductInterface;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.remove)
            removeProductInterface.removeProduct(this.getLayoutPosition());
    }
}
