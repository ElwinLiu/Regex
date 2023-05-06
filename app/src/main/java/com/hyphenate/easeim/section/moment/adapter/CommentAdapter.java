package com.hyphenate.easeim.section.moment.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hyphenate.easeim.R;

import com.hyphenate.easeim.section.moment.response.CommentListResp;
import com.hyphenate.easeim.section.moment.view_holder.CommentViewHolder;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder>{

    private final List<CommentListResp.Comment> commentList;
    private ItemClickListener itemClickListener;


    public CommentAdapter(List<CommentListResp.Comment> commentList){
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*View view = View.inflate(parent.getContext(), R.layout.item_comment, null);*/
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment,parent,false);
        CommentViewHolder commentViewHoder = new CommentViewHolder(view);
        return commentViewHoder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CommentListResp.Comment comment = commentList.get(position);
        Glide.with(holder.itemView).load(comment.avatar).centerCrop().into(holder.avatar);
        holder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemClickListener!=null) itemClickListener.onAvatarClick(position);
            }
        });
        holder.name.setText(comment.nickname);
        holder.content.setText(comment.text_content);
        holder.date.setText(comment.created_time);
        if(!comment.image.equals("")){
            Glide.with(holder.itemView).load(comment.image).override(300,400).into(holder.image);
            holder.image.setVisibility(View.VISIBLE);
        }
        else {
            holder.image.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemClickListener!=null) itemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }


    //Getter, Setter
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    /**
     * 自定义list_item点击监听接口
     */
    public interface ItemClickListener{
        void onItemClick(int position);
        void onAvatarClick(int position);
    }
}
