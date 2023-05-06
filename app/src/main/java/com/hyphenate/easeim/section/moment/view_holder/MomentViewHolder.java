package com.hyphenate.easeim.section.moment.view_holder;



import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hyphenate.easeim.R;

public class MomentViewHolder extends RecyclerView.ViewHolder{
    public final ImageView avatar;
    public final TextView name;
    public final TextView date;
    public final TextView content;
    public final TableRow tableRow;
    public final Button follow;
    public final ImageView image;
    public final CardView cardAvatar;


    public MomentViewHolder(@NonNull View itemView) {
        super(itemView);
        avatar = itemView.findViewById(R.id.item_avatar);
        name = itemView.findViewById(R.id.item_name);
        date = itemView.findViewById(R.id.item_date);
        content = itemView.findViewById(R.id.item_content);
        tableRow = itemView.findViewById(R.id.item_tableRow);
        follow = itemView.findViewById(R.id.item_follow);
        image = itemView.findViewById(R.id.item_image);
        cardAvatar = itemView.findViewById(R.id.item_card_avatar);
    }

}
