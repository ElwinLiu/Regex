package com.hyphenate.easeim.section.moment.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.hyphenate.easeim.R;

public class ImageContainerView extends ConstraintLayout {
    private ImageView imageView;
    private ImageButton cancel;

    public ImageContainerView(Context context) {
        this(context, null);
    }

    public ImageContainerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageContainerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View inflate = LayoutInflater.from(context).inflate(R.layout.image_container, this);
        cancel = inflate.findViewById(R.id.image_cancel_btn);
        imageView = inflate.findViewById(R.id.imageView);
    }

    public void setOnCancelClick(OnClickListener click){
        cancel.setOnClickListener(click);
    }

    public void setOnImageClick(OnClickListener click){
        imageView.setOnClickListener(click);
    }

    public void setImageBitmap(Bitmap bitmap){
        imageView.setImageBitmap(bitmap);
    }



}
