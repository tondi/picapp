package com.example.a4ia2.picapp;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;

/**
 * Created by 4ia2 on 2017-10-09.
 */
    public class CustomImageView extends ImageView implements View.OnClickListener {

    private int bgColor;
    private String imagePath;

    public CustomImageView(Context context, File CurrentImage) {
        super(context);
        this.imagePath = CurrentImage.getAbsolutePath();
//        this.bgColor = bgColor;
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(200,200);
//        lp.setMargins(10,0,10,10);
//        this.setLayoutParams( lp);
//        this.setImageResource(R.mipmap.ic_launcher);
//        this.setBackgroundColor(0xff0000ff);
        this.setScaleType(ScaleType.CENTER_CROP);

        setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        this.setBackgroundColor(0xffffff00);
        Intent intent = new Intent(v.getContext(), ImageActivity.class);
        intent.putExtra("imagePath", imagePath);
        v.getContext().startActivity(intent);

    }
}
