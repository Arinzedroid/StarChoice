package com.tech.arinzedroid.starchoice.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tech.arinzedroid.starchoice.R;
import com.tech.arinzedroid.starchoice.interfaces.UserClickedProduct;
import com.tech.arinzedroid.starchoice.models.ProductModel;
import com.tech.arinzedroid.starchoice.viewHolder.RegisterProductViewHolder;

import java.util.List;

public class RegisterProductAdapter extends RecyclerView.Adapter<RegisterProductViewHolder> {

    private UserClickedProduct userClickedProduct;
    private List<ProductModel> productModelList;

    public RegisterProductAdapter(List<ProductModel> productModelsList, UserClickedProduct userClickedProduct){
        this.userClickedProduct = userClickedProduct;
        this.productModelList = productModelsList;
    }

    public void removeItem(int position){
        productModelList.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(ProductModel item){
        int count = productModelList.size();
        if(!productModelList.contains(item)){
            productModelList.add(count - 1,item);
            notifyItemInserted(count - 1);
        }
    }

    @NonNull
    @Override
    public RegisterProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items,parent,false);
        return new RegisterProductViewHolder(v,userClickedProduct);
    }

    @Override
    public void onBindViewHolder(@NonNull RegisterProductViewHolder holder, int position) {
        ProductModel obj = productModelList.get(position);
        holder.nameTv.setText(obj.getProductName());
        holder.priceTv.setText(String.valueOf(obj.getPrice()));
    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }
}
