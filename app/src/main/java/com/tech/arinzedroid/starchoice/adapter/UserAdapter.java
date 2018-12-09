package com.tech.arinzedroid.starchoice.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tech.arinzedroid.starchoice.R;
import com.tech.arinzedroid.starchoice.interfaces.UserClickedInterface;
import com.tech.arinzedroid.starchoice.models.UserModel;
import com.tech.arinzedroid.starchoice.util.DateTimeUtils;
import com.tech.arinzedroid.starchoice.util.FormatUtil;
import com.tech.arinzedroid.starchoice.viewHolder.UsersViewHolder;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UsersViewHolder> {

    private List<UserModel> userModelList;
    private UserClickedInterface userClickedInterface; private int count = 0;
    public UserAdapter(List<UserModel> userModelList, UserClickedInterface userClickedInterface){
        this.userModelList = userModelList; count = userModelList.size();
        this.userClickedInterface = userClickedInterface;
    }

    public void clearAdapter(){
        userModelList.clear();
        count = userModelList.size();
        notifyDataSetChanged();
    }

    public void addAll(List<UserModel> userModels){
        userModelList.clear();
        userModelList.addAll(userModels);
        count = userModels.size();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_items,parent,false);
        return new UsersViewHolder(v,userClickedInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        UserModel data = userModelList.get(position);
        holder.nameTv.setText(data.getFullname());
        holder.dateTV.setText(DateTimeUtils.parseDateTime(data.getDateCreated()));
        holder.amountTv.setText(FormatUtil.formatPrice(data.getTotalAmount()));
        holder.serialTv.setText(String.valueOf(count - position));
    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }
}
