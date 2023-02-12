package com.example.kubus001;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseManager extends SQLiteOpenHelper {
    public DatabaseManager(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE notes (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 'title' TEXT, 'text' TEXT, 'color' TEXT, 'path' TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS notes");
        onCreate(sqLiteDatabase);
    }

    public boolean insert(String title, String text, String color, String path){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("text", text);
        contentValues.put("color", color);
        contentValues.put("path", path);

        db.insertOrThrow("notes", null, contentValues); // gdy insert się nie powiedzie, będzie błąd
        db.close();
        return true;
    }

    @SuppressLint("Range")
    public ArrayList<Note> getAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Note> notes = new ArrayList<>();
        Cursor result = db.rawQuery("SELECT * FROM notes" , null);
        while(result.moveToNext()){
            notes.add( new Note(
                    result.getString(result.getColumnIndex("_id")),
                    result.getString(result.getColumnIndex("title")),
                    result.getString(result.getColumnIndex("text")),
                    result.getString(result.getColumnIndex("color")),
                    result.getString(result.getColumnIndex("path"))
            ));

        }
        return notes;
    }
}
