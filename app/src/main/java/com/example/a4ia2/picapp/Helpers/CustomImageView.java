package com.example.a4ia2.picapp.Helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a4ia2.picapp.Activites.AlbumsActivity;
import com.example.a4ia2.picapp.Activites.ImageActivity;
import com.example.a4ia2.picapp.R;

import java.io.File;

/**
 * Created by 4ia2 on 2017-10-09.
 */
    public class CustomImageView extends ImageView implements View.OnLongClickListener, View.OnClickListener  {

    private int bgColor;
    private String imagePath;
    public DatabaseManager db;
    private EditText titleEditText;
    private EditText descriptionEditText;

    private String chosenColor;
    private TextView textTextView;
    private TextView colorTextView;

    public CustomImageView(Context context, File CurrentImage, @NonNull DatabaseManager Db) {
        super(context);
        this.imagePath = CurrentImage.getAbsolutePath();
        this.db = Db;
//        this.bgColor = bgColor;
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(200,200);
//        lp.setMargins(10,0,10,10);
//        this.setLayoutParams( lp);
//        this.setImageResource(R.mipmap.ic_launcher);
//        this.setBackgroundColor(0xff0000ff);
        this.setScaleType(ScaleType.CENTER_CROP);

        setOnClickListener(this);
        setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.setBackgroundColor(0xffffff00);
        Intent intent = new Intent(v.getContext(), ImageActivity.class);
        intent.putExtra("imagePath", imagePath);
        v.getContext().startActivity(intent);

    }

    @Override
    public boolean onLongClick(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
        alert.setTitle("Add new note");
        final View view = View.inflate(v.getContext(), R.layout.note_dialog, null);
        alert.setView(view);

        final Button btRedColor = view.findViewById(R.id.noteRedButton);
        final Button btBlueColor = view.findViewById(R.id.noteBlueButton);
        final Button btGreenColor = view.findViewById(R.id.noteGreenButton);


        btRedColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onColorChoose(v);
            }
        });
        btGreenColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onColorChoose(v);
            }
        });
        btBlueColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onColorChoose(v);
            }
        });

        titleEditText = view.findViewById(R.id.et1);
        descriptionEditText = view.findViewById(R.id.et2);
        textTextView = view.findViewById(R.id.text_textview);
        colorTextView = view.findViewById(R.id.color_textview);


        alert.setNeutralButton("OK", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //save to

                String title = titleEditText.getText().toString();

                String description = descriptionEditText.getText().toString();

                db.insert(title, description, chosenColor, imagePath);
            }
        });
        alert.show();
        return false;
    }

    private void onColorChoose(View view) {
        // get color in int
        int intColor = ((ColorDrawable) view.getBackground()).getColor();
        setColorForDialog(intColor);
    }

    private void setColorForDialog(int color) {
        // update color of text to inform user
        titleEditText.setTextColor(color);
        descriptionEditText.setTextColor(color);
        textTextView.setTextColor(color);
        colorTextView.setTextColor(color);

        // convert color from int format to HEX -> to save in db
        chosenColor = String.format("#%06X", (0xFFFFFF & color));
    }
}
