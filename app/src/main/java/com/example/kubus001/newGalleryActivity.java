package com.example.kubus001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class newGalleryActivity extends AppCompatActivity {
    ImagesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_gallery);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent myIntent = getIntent();
        String folderName = myIntent.getStringExtra("name");

        LinearLayout linearLayout = findViewById(R.id.newAlbumsFolderLayout);

        File mainDir = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES + "/JakubKowal/" + folderName );
        ArrayList<File> filesArray = new ArrayList<>(Arrays.asList(mainDir.listFiles()));


        adapter = new ImagesAdapter (
                newGalleryActivity.this,
                R.layout.new_gallery_listview_row,
                filesArray
        );

        ListView listView = findViewById(R.id.newGalleryListView);
        listView.invalidateViews();

        listView.setAdapter(adapter);
    }

    private Bitmap betterImageDecode(String filePath) {
        Bitmap myBitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();    //opcje przekształcania bitmapy
        options.inSampleSize = 4; // zmniejszenie jakości bitmapy 4x
        //
        myBitmap = BitmapFactory.decodeFile(filePath, options);
        return myBitmap;
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