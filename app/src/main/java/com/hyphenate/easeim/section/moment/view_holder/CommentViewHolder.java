package com.hyphenate.easeim.section.moment.view_holder;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hyphenate.easeim.R;

public class CommentViewHolder extends RecyclerView.ViewHolder{
    public final ImageView avatar;
    public final TextView name;
    public final TextView content;
    public final TableRow tableRow;
    public final TextView date;
    public final ImageView image;


    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);
        avatar = itemView.findViewById(R.id.item_comment_avatar);
        name = itemView.findViewById(R.id.item_comment_name);
        content = itemView.findViewById(R.id.item_comment_content);
        tableRow = itemView.findViewById(R.id.item_comment_tabelRow);
        date = itemView.findViewById(R.id.item_comment_date);
        image = itemView.findViewById(R.id.item_comment_image);
    }
}
