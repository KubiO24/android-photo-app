package com.example.kubus001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class PhotoActivity extends AppCompatActivity {
    boolean x = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent myIntent = getIntent();
        String imagepath = myIntent.getStringExtra("imagepath") ;
        Bitmap bmp = betterImageDecode(imagepath);    // własna funkcja betterImageDecode opisana jest poniżej

        ImageView imageView = findViewById(R.id.photoImageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(bmp);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!x){
                    imageView.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    imageView.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    imageView.requestLayout();
                    x = true;
                }else{
                    imageView.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
                    imageView.getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;
                    imageView.requestLayout();
                    x = false;
                }
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}