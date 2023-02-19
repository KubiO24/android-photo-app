package com.example.kubus001;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;

public class EffectsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effects);

        ImageView image = findViewById(R.id.effectsImageView);
        image.setImageBitmap(ImageEdition.myBitmap);
        ImageEdition.myBitmap = null;

        ImageView done = findViewById(R.id.effectDone);
        done.setOnClickListener(v -> {
            Bitmap b = ((BitmapDrawable) image.getDrawable()).getBitmap();
            ImageEdition.myBitmap = b;
            finish();
        });
    }
}