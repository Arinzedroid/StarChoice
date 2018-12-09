package com.tech.arinzedroid.starchoice.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tech.arinzedroid.starchoice.R;
import com.tech.arinzedroid.starchoice.interfaces.RemoveProductInterface;
import com.tech.arinzedroid.starchoice.models.ProductModel;
import com.tech.arinzedroid.starchoice.util.FormatUtil;
import com.tech.arinzedroid.starchoice.viewHolder.ProductViewHolder;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {

    private List<ProductModel> productModelList;
    private RemoveProductInterface removeProductInterface;

    public ProductAdapter(List<ProductModel> productModels, RemoveProductInterface removeProductInterface){
        this.productModelList = productModels;
        this.removeProductInterface = removeProductInterface;
        setHasStableIds(true);
    }

    public void addProduct(ProductModel productModel){
        this.productModelList.add(productModel);
        notifyItemInserted(this.productModelList.size() - 1);
    }

    public void removeProduct(int position){
        productModelList.remove(position);
        notifyItemRemoved(position);
        //notifyItemRangeChanged(position,productModelList.size());
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items,parent,false);
        return new ProductViewHolder(v,removeProductInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductModel data = productModelList.get(position);
        holder.nameTv.setText(data.getProductName());
        holder.priceTv.setText(FormatUtil.formatPrice(data.getPrice()));
    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }


}
