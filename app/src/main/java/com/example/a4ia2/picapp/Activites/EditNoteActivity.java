package com.example.a4ia2.picapp.Activites;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.a4ia2.picapp.Helpers.DatabaseManager;
import com.example.a4ia2.picapp.R;

public class EditNoteActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText descriptionEditText;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView colorTextView;
    private Button confirm;
    private String chosenColor;
    private DatabaseManager db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        Bundle bundle = getIntent().getExtras();
        final int selectedNoteId = bundle.getInt("selectedNoteId");

        db = new DatabaseManager(
                EditNoteActivity.this, // activity z galerią zdjęć
                "NotesTondosMateusz.db", // nazwa bazy
                null,
                6 //wersja bazy, po zmianie schematu bazy należy ją zwiększyć
        );

        titleEditText = (EditText) findViewById(R.id.et1);
        descriptionEditText = (EditText) findViewById(R.id.et2);
        colorTextView = (TextView) findViewById(R.id.colorChooseText);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);

        final Button btRedColor = (Button) findViewById(R.id.editRedButton);
        final Button btBlueColor = (Button) findViewById(R.id.editBlueButton);
        final Button btGreenColor = (Button) findViewById(R.id.editGreenButton);

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


        confirm = (Button) findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();

                db.updateNote(selectedNoteId, title, description, chosenColor);

                Intent intent = new Intent(EditNoteActivity.this, NotesActivity.class);
                startActivity(intent);
            }
        });


//        db.insert(title, description, chosenColor, imagePath);


//        Log.d(selectedNote);
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
        colorTextView.setTextColor(color);
        titleTextView.setTextColor(color);
        descriptionTextView.setTextColor(color);


        // convert color from int format to HEX -> to save in db
        chosenColor = String.format("#%06X", (0xFFFFFF & color));
    }
}
