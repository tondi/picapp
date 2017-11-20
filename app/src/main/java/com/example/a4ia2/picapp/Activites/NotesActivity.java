package com.example.a4ia2.picapp.Activites;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.a4ia2.picapp.Adapters.NoteArrayAdapter;
import com.example.a4ia2.picapp.Helpers.DatabaseManager;
import com.example.a4ia2.picapp.Helpers.Note;
import com.example.a4ia2.picapp.R;

import java.util.ArrayList;

import static com.example.a4ia2.picapp.R.id.notesList;

public class NotesActivity extends AppCompatActivity {

    private DatabaseManager db;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        db = new DatabaseManager(
                NotesActivity.this, // activity z galerią zdjęć
                "NotesTondosMateusz.db", // nazwa bazy
                null,
                6 //wersja bazy, po zmianie schematu bazy należy ją zwiększyć
        );
        displayNotes();
    }

    private void onOptionSelect(int which, Note selectedNote) {
        switch (which) {
            case 0:
                db.deleteNote(selectedNote.getId());
                displayNotes();
                return;
            case 1:
                Intent intent = new Intent(NotesActivity.this, EditNoteActivity.class);
                intent.putExtra("selectedNoteId", which);
                startActivity(intent);
                return;
            default:
        }
    }

    private void displayNotes() {

        final ArrayList<Note> notes = db.getAll();

        final NoteArrayAdapter adapter = new NoteArrayAdapter(
                NotesActivity.this,
                R.layout.note,
                notes
        );

        listView = (ListView) findViewById(notesList);

        listView.setAdapter(adapter);

        final String[] options = new String[]{"Delete", "Edit", "Sort by Title", "Sort by color", "Sort by Image Date"};
//        final ArrayList<String> options = (ArrayList<String>) Arrays.asList(optionsArr);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setTitle("Set note");
                AlertDialog.Builder builder = alert.setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Note selectedNote = notes.get(i);
                        onOptionSelect(which, selectedNote);
                    }
                });
                alert.show();
                return true;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        db.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
