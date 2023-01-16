package com.example.kubus001;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class NewPhotoActivity extends AppCompatActivity {
    boolean showControls = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_photo);

        Intent myIntent = getIntent();
        String imagepath = myIntent.getStringExtra("imagepath") ;
        Bitmap bmp = betterImageDecode(imagepath);    // własna funkcja betterImageDecode opisana jest poniżej

        RelativeLayout settings = findViewById(R.id.photoSettings);
        RelativeLayout controls = findViewById(R.id.photoControls);
        ImageView image = findViewById(R.id.newPhotoImageView);
        image.setImageBitmap(bmp);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(showControls){
                    settings.animate().translationY(convertDpToPx(-80));
                    controls.animate().translationY(convertDpToPx(90));
                    showControls = false;
                }else{
                    settings.animate().translationY(0);
                    controls.animate().translationY(convertDpToPx(10));
                    showControls = true;
                }
            }
        });

        ImageView brightness = findViewById(R.id.newPhotoBrightness);
        brightness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("XXX", "brightness");
            }
        });

        ImageView contrast = findViewById(R.id.newPhotoContrast);
        contrast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("XXX", "contrast");
            }
        });

        ImageView saturation = findViewById(R.id.newPhotoSaturation);
        saturation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("XXX", "saturation");
            }
        });

        ImageView back = findViewById(R.id.newPhotoBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageView revert = findViewById(R.id.newPhotoRevert);
        revert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("XXX", "revert");
            }
        });

        ImageView upload = findViewById(R.id.newPhotoUpload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("XXX", "upload");
            }
        });
    }

    private Bitmap betterImageDecode(String filePath) {
        Bitmap myBitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();    //opcje przekształcania bitmapy
        options.inSampleSize = 4; // zmniejszenie jakości bitmapy 4x
        //
        myBitmap = BitmapFactory.decodeFile(filePath, options);
        return myBitmap;
    }

    private int convertDpToPx(int dp){
        return Math.round(dp*(getResources().getDisplayMetrics().xdpi/DisplayMetrics.DENSITY_DEFAULT));
    }
}