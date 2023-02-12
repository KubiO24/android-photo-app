package com.example.kubus001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

public class NotesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        DatabaseManager db = new DatabaseManager (
                NotesActivity.this, // activity z galerią zdjęć
                "NotatkiJakubKowal.db", // nazwa bazy
                null,
                1 //wersja bazy, po zmianie schematu bazy należy ją zwiększyć
        );

        NotesArrayAdapter adapter = new NotesArrayAdapter(
                NotesActivity.this,
                R.layout.notes_listview_row,
                db.getAll()
        );

        ListView notesListView = findViewById(R.id.notes_listview);
        notesListView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}