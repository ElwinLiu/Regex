package com.hyphenate.easeim.section.follow.view_holder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.hyphenate.easeim.R;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class UserViewHolder extends RecyclerView.ViewHolder{
    public CardView cardAvatar;
    public ImageView avatar;
    public TextView name;
    public TextView infomation;
    public Button follow;


    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        cardAvatar = itemView.findViewById(R.id.item_user_card);
        avatar = itemView.findViewById(R.id.item_user_avatar);
        name = itemView.findViewById(R.id.item_user_name);
        infomation = itemView.findViewById(R.id.item_user_infomation);
        follow = itemView.findViewById(R.id.item_user_follow);
    }
}
