package com.example.kubus001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class NewGalleryActivity extends AppCompatActivity {
    ImagesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_gallery);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)  actionBar.setDisplayHomeAsUpEnabled(true);

        Intent myIntent = getIntent();
        String folderName = myIntent.getStringExtra("name");

        File mainDir = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES + "/JakubKowal/" + folderName );
        ArrayList<File> filesArray = new ArrayList<>(Arrays.asList(Objects.requireNonNull(mainDir.listFiles())));


        adapter = new ImagesAdapter (
                NewGalleryActivity.this,
            R.layout.new_gallery_listview_row,
            filesArray
        );

        ListView listView = findViewById(R.id.newGalleryListView);
        listView.invalidateViews();

        listView.setAdapter(adapter);
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