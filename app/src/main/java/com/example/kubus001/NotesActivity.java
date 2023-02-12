package com.example.kubus001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class NotesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        ListView notesListView = findViewById(R.id.notes_listview);

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
        notesListView.setAdapter(adapter);

        notesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NotesActivity.this);
                AlertDialog alert = builder.create();
                alert.setTitle("Edycja notatek");

                LinearLayout alertView = new LinearLayout(NotesActivity.this);
                alert.setView(alertView);
                alertView.setOrientation(LinearLayout.VERTICAL);


                Button editButton = new Button(NotesActivity.this);
                editButton.setBackgroundResource(0);
                editButton.setText("edytuj");
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("XXX", "edit");
                        alert.cancel();
                    }
                });
                alertView.addView(editButton);

                Button deleteButton = new Button(NotesActivity.this);
                deleteButton.setBackgroundResource(0);
                deleteButton.setText("usuń");
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseManager db = new DatabaseManager (
                                NotesActivity.this,
                                "NotatkiJakubKowal.db",
                                null,
                                1
                        );
                        db.delete(db.getAll().get(i).get_id());

                        NotesArrayAdapter adapter = new NotesArrayAdapter(
                                NotesActivity.this,
                                R.layout.notes_listview_row,
                                db.getAll()
                        );
                        notesListView.setAdapter(adapter);

                        alert.cancel();
                    }
                });
                alertView.addView(deleteButton);

                Button titleSortButton = new Button(NotesActivity.this);
                titleSortButton.setBackgroundResource(0);
                titleSortButton.setText("sortuj wg tytułu");
                titleSortButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ArrayList<Note> sortedDb = db.getAll();

                        Collections.sort(sortedDb, new Comparator<Note>() {
                            @Override
                            public int compare(Note n1, Note n2) {
                                return n1.getTitle().compareTo(n2.getTitle());
                            }
                        });

                        NotesArrayAdapter adapter = new NotesArrayAdapter(
                                NotesActivity.this,
                                R.layout.notes_listview_row,
                                sortedDb
                        );
                        notesListView.setAdapter(adapter);
                        alert.cancel();
                    }
                });
                alertView.addView(titleSortButton);

                Button colorSortButton = new Button(NotesActivity.this);
                colorSortButton.setBackgroundResource(0);
                colorSortButton.setText("sortuj wg koloru");
                colorSortButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("XXX", "color sort");
                        alert.cancel();
                    }
                });
                alertView.addView(colorSortButton);

                alert.show();
                return false;
            }
        });
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