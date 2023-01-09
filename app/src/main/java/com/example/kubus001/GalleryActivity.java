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
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;

public class GalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent myIntent = getIntent();
        String folderName = myIntent.getStringExtra("name");

        LinearLayout linearLayout = findViewById(R.id.albumsFolderLayout);

        File mainDir = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES + "/JakubKowal/" + folderName );
        File[] files = mainDir.listFiles();

        for (File file: files) {
            ImageView imageView = new ImageView(this);
            linearLayout.addView(imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500);
            params.setMargins(0, 0, 0, 20);
            imageView.setLayoutParams(params);

            //jeśli File jest plikiem
            String imagepath = file.getPath();                               // pobierz scieżkę z obiektu File
            Bitmap bmp = betterImageDecode(imagepath);    // własna funkcja betterImageDecode opisana jest poniżej
            imageView.setImageBitmap(bmp);                // wstawienie bitmapy do ImageView

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(GalleryActivity.this, PhotoActivity.class);
                    intent.putExtra("imagepath", imagepath);
                    startActivity(intent);
                }
            });
        }
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