package com.example.kubus001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
                        alert.cancel();

                        String oldTitle = db.getAll().get(i).getTitle();
                        String oldText = db.getAll().get(i).getText();
                        String oldColor = db.getAll().get(i).getColor();

                        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(NotesActivity.this);
                        View editView = View.inflate(NotesActivity.this, R.layout.note_inputs, null);
                        alert.setView(editView);

                        EditText titleText = (EditText) editView.findViewById(R.id.titleText);
                        titleText.setText(oldTitle);

                        EditText textText = (EditText) editView.findViewById(R.id.textText);
                        textText.setText(oldText);

                        final int[] selectedColor = {(int) Long.parseLong(oldColor, 16)};
                        titleText.setTextColor(selectedColor[0]);
                        titleText.getBackground().mutate().setColorFilter(selectedColor[0], PorterDuff.Mode.SRC_ATOP);

                        int[] colors = new int[]{0xff0099ff, 0xffff5100, 0xffeb2f58, 0xffa841d1, 0xfff765d0, 0xff7fe3bd, 0xff2fa134, 0xff074f0a};
                        LinearLayout colorsLayout = (LinearLayout) editView.findViewById(R.id.colorsLayout);

                        for(int color :colors){
                            ImageView x = new ImageView(NotesActivity.this);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
                            x.setLayoutParams(layoutParams);
                            x.setBackgroundColor(color);
                            colorsLayout.addView(x);
                            x.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    selectedColor[0] = color;
                                    titleText.setTextColor(selectedColor[0]);
                                    titleText.getBackground().mutate().setColorFilter(selectedColor[0], PorterDuff.Mode.SRC_ATOP);
                                }
                            });
                        }

                        alert.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String id = db.getAll().get(i).get_id();
                                String title = titleText.getText().toString();
                                String text = textText.getText().toString();
                                String color = Integer.toHexString(selectedColor[0]);

                                db.edit(id, title, text, color);

                                NotesArrayAdapter adapter = new NotesArrayAdapter(
                                        NotesActivity.this,
                                        R.layout.notes_listview_row,
                                        db.getAll()
                                );
                                notesListView.setAdapter(adapter);

                                dialog.cancel();
                            }

                        });

                        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        alert.show();
                    }
                });
                alertView.addView(editButton);

                Button deleteButton = new Button(NotesActivity.this);
                deleteButton.setBackgroundResource(0);
                deleteButton.setText("usuń");
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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
                        ArrayList<Note> sortedDb = db.getAll();

                        Collections.sort(sortedDb, new Comparator<Note>() {
                            @Override
                            public int compare(Note n1, Note n2) {
                                return n1.getColor().compareTo(n2.getColor());
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