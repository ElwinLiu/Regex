package com.hyphenate.easeim.section.follow.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hyphenate.easeim.R;
import com.hyphenate.easeim.section.follow.response.UserListResp;
import com.hyphenate.easeim.section.follow.view_holder.UserViewHolder;
import com.hyphenate.easeim.section.moment.view_holder.CommentViewHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder>{

    private List<UserListResp.User> userList;
    private ItemClickListener itemClickListener;

    public UserAdapter(List<UserListResp.User> userList){
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user,parent,false);
        UserViewHolder userViewHoder = new UserViewHolder(view);
        return userViewHoder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, @SuppressLint("RecyclerView") int position) {
        UserListResp.User user = userList.get(position);
        Glide.with(holder.itemView).load(user.avatar).centerCrop().into(holder.avatar);
        holder.name.setText(user.nickname);
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssX");
        Date date = null;
        try {
            date = sdf.parse(user.created_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date cur_date = new Date(System.currentTimeMillis());
        long days = (cur_date.getTime()-date.getTime())/(1000*60*60*24);
        holder.infomation.setText(days +"天，"+user.momentNum.toString()+"条瞬间");
        if(user.subscribe_status){
            holder.follow.setText("已关注");
            holder.follow.setBackgroundResource(R.drawable.btn_white);
            holder.follow.setTextColor(Color.rgb(4,174,240));
        }
        else{
            holder.follow.setText("关注");
            holder.follow.setBackgroundResource(R.drawable.btn_blue);
            holder.follow.setTextColor(Color.rgb(255,255,255));
        }
        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onFollowClick(position);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemClickListener!=null) itemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }


    /**
     * 自定义list_item点击监听接口
     */
    public interface ItemClickListener{
        void onItemClick(int position);
        void onFollowClick(int position);
    }

}
