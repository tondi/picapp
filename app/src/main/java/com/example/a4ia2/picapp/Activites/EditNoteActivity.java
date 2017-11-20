package com.example.a4ia2.picapp.Activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.a4ia2.picapp.Helpers.Note;
import com.example.a4ia2.picapp.R;

public class EditNoteActivity extends AppCompatActivity {

    private View titleEditText;
    private View descriptionEditText;
    private View colorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        Bundle bundle = getIntent().getExtras();
        int selectedNoteId = bundle.getInt("selectedNoteId");

        titleEditText = findViewById(R.id.et1);
        descriptionEditText = findViewById(R.id.et2);
        colorTextView = findViewById(R.id.color_textview);

//        String title = titleEditText.getText().toString();

//        String description = descriptionEditText.getText().toString();

//        db.insert(title, description, chosenColor, imagePath);


//        Log.d(selectedNote);
    }
}
