package com.example.a4ia2.picapp.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by mtond_000 on 18.11.2017.
 */

public class DatabaseManager extends SQLiteOpenHelper {
    public DatabaseManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE tabela1 (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 'title' TEXT, 'description' TEXT, 'color' TEXT, 'path' TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS tabela1");

        // increase database version
        db.setVersion(db.getVersion() + 1);
        // create new table
        onCreate(db);
    }

    public boolean insert(String title, String description, String color, String path){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("description", description);
        contentValues.put("color", color);
        contentValues.put("path", path);

        db.insertOrThrow("tabela1", null, contentValues); // gdy insert się nie powiedzie, będzie błąd
        db.close();
        return true;
    }

    public ArrayList<Note> getAll(){

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Note> notes= new ArrayList<>();
        Cursor result = db.rawQuery("SELECT * FROM tabela1" , null);
        while(result.moveToNext()){
            notes.add( new Note(
                    result.getInt(result.getColumnIndex("_id")),
                    result.getString(result.getColumnIndex("title")),
                    result.getString(result.getColumnIndex("description")),
                    result.getString(result.getColumnIndex("color")),
                    result.getString(result.getColumnIndex("path"))
            ));

        }
        return notes;
    }

    public int deleteNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String convertedId = String.valueOf(id);
        return db.delete("tabela1", "_id = ? ", new String[]{convertedId});
    }

    public void updateNote(int id, String newTitle, String newDescription, String newColor) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("title", newTitle);
        contentValues.put("description", newDescription);
        contentValues.put("color", newColor);

        db.update("tabela1", contentValues, "_id = ? ", new String[]{String.valueOf(id)});
        db.close();
    }

//    @Override
//    private void onDestroy(DatabaseManager db) {
//        db.close();
//    }
}
