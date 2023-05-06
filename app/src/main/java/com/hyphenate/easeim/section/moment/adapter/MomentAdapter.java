package com.hyphenate.easeim.section.moment.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hyphenate.easeim.R;

import com.hyphenate.easeim.section.moment.response.MomentListResp;
import com.hyphenate.easeim.section.moment.utils.PhotoUtils;
import com.hyphenate.easeim.section.moment.view_holder.MomentViewHolder;

import java.util.List;

public class MomentAdapter extends RecyclerView.Adapter<MomentViewHolder>{

    private final List<MomentListResp.Moment> momentList;
    private ItemClickListener itemClickListener;
    private ItemLongClickListener itemLongClickListener;
    private boolean showInfo;  //是否显示个人信息
    private int id;  //用户id


    public MomentAdapter(List<MomentListResp.Moment> momentList,int id){
        this.momentList = momentList;
        this.id = id;
        showInfo = true;
    }

    @NonNull
    @Override
    public MomentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /* View view = View.inflate(parent.getContext(), R.layout.item_moment, null);*/
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_moment,parent,false);
        MomentViewHolder momentViewHoder = new MomentViewHolder(view);
        return momentViewHoder;
    }

    @Override
    public void onBindViewHolder(@NonNull MomentViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //TODO: set holder view
        MomentListResp.Moment moment = momentList.get(position);
        if(showInfo){
            Glide.with(holder.itemView).load(moment.avatar).centerCrop().into(holder.avatar);
            holder.avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemClickListener!=null) itemClickListener.onAvatarClick(position);
                }
            });
            holder.name.setText(moment.nickname);
            if(id!=moment.sender_id){  //非自己才显示
                if(moment.is_follower){
                    holder.follow.setText("私聊");
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
                    public void onClick(View view) {  //关注或私聊
                        if(itemClickListener!=null) itemClickListener.onFollowClick(position);
                    }
                });
                holder.follow.setVisibility(View.VISIBLE);
            }
            else holder.follow.setVisibility(View.GONE);
        }
        else{
            holder.cardAvatar.setVisibility(View.GONE);
            holder.avatar.setVisibility(View.GONE);
            holder.name.setVisibility(View.GONE);
            holder.follow.setVisibility(View.GONE);
        }
        holder.date.setText(moment.created_time);
        holder.content.setText(moment.text_content);
        if(!moment.image.equals("")){
            Glide.with(holder.itemView).load(moment.image).override(600, 800).into(holder.image);
            holder.image.setVisibility(View.VISIBLE);
        }
        else {
            holder.image.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {  //进入动态详情
                if(itemClickListener!=null) itemClickListener.onItemClick(position);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(itemLongClickListener!=null){
                    itemLongClickListener.onItemLongClick(position,holder.itemView);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return momentList.size();
    }


    //Getter, Setter
    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public void setOnItemLongClickListener(ItemLongClickListener itemLongClickListener){
        this.itemLongClickListener = itemLongClickListener;
    }

    public void setShowInfo(boolean showInfo){
        this.showInfo = showInfo;
    }


    /**
     * 自定义list_item点击监听接口
     */
    public interface ItemClickListener{
        void onItemClick(int position);
        void onFollowClick(int position);
        void onAvatarClick(int position);
    }


    /**
     * 自定义list_item长按监听接口
     */
    public interface ItemLongClickListener{
        void onItemLongClick(int position,View view);
    }

}
