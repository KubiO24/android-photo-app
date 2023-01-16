package com.example.kubus001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.File;

public class NewAlbumsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_albums);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        File mainDir = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES + "/JakubKowal" );
        File[] files = mainDir.listFiles();
        String[] dirArray = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            dirArray[i] = files[i].getName();
        }
        ListView listView = findViewById(R.id.newAlbumsListView);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                NewAlbumsActivity.this,       // tzw Context
                R.layout.newlistview_row,     // nazwa pliku xml naszego wiersza na liście
                R.id.newAlbumsListView_text, // id pola txt w wierszu
                dirArray );                 // tablica przechowująca testowe dane

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                File mainDir = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES + "/JakubKowal" );
                File[] files = mainDir.listFiles();
                Intent intent = new Intent(NewAlbumsActivity.this, NewGalleryActivity.class);
                intent.putExtra("name", files[i].getName());
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder alert = new AlertDialog.Builder(NewAlbumsActivity.this);
                alert.setTitle("Usuwanie Folderu");
                alert.setMessage("Czy na pewno usunąć?");

                //delete button
                alert.setPositiveButton("Usuń", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        File mainDir = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES + "/JakubKowal" );
                        File[] files = mainDir.listFiles();
                        File dirToDelete = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES + "/JakubKowal/" + files[i].getName());
                        for (File file : dirToDelete.listFiles()){
                            file.delete();
                        }
                        dirToDelete.delete();

                        files = mainDir.listFiles();
                        String[] dirArray = new String[files.length];
                        for (int i = 0; i < files.length; i++) {
                            dirArray[i] = files[i].getName();
                        }
                        ListView listView = findViewById(R.id.newAlbumsListView);

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                NewAlbumsActivity.this,       // tzw Context
                                R.layout.newlistview_row,     // nazwa pliku xml naszego wiersza na liście
                                R.id.newAlbumsListView_text, // id pola txt w wierszu
                                dirArray );                 // tablica przechowująca testowe dane

                        listView.setAdapter(adapter);
                    }

                });

                //cancel button
                alert.setNegativeButton("Anuluj", null);
                alert.show();
                return true;
            }
        });

        ImageView addButton = findViewById(R.id.newAlbumsAddButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(NewAlbumsActivity.this);
                alert.setTitle("Nowy Folder");
                alert.setMessage("Podaj nazwę nowego folderu:");

                //input
                EditText input = new EditText(NewAlbumsActivity.this);
                input.setText("test");
                alert.setView(input);

                //add button
                alert.setPositiveButton("Dodaj", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        File picturesDir = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES );
                        File newAlbum = new File(picturesDir + "/JakubKowal", String.valueOf(input.getText()));
                        newAlbum.mkdir();

                        File mainDir = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES + "/JakubKowal" );
                        File[] files = mainDir.listFiles();
                        String[] dirArray = new String[files.length];
                        for (int i = 0; i < files.length; i++) {
                            dirArray[i] = files[i].getName();
                        }
                        ListView listView = findViewById(R.id.newAlbumsListView);

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                NewAlbumsActivity.this,       // tzw Context
                                R.layout.newlistview_row,     // nazwa pliku xml naszego wiersza na liście
                                R.id.newAlbumsListView_text, // id pola txt w wierszu
                                dirArray );                 // tablica przechowująca testowe dane

                        listView.setAdapter(adapter);
                    }

                });

                //cancel button
                alert.setNegativeButton("Anuluj", null);
                alert.show();
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