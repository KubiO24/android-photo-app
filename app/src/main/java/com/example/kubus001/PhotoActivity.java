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

        Intent myIntent = getIntent();
        String imagepath = myIntent.getStringExtra("imagepath") ;
        Bitmap bmp = betterImageDecode(imagepath); // własna funkcja betterImageDecode opisana jest poniżej

        ImageView image = findViewById(R.id.photoImageView);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        image.setImageBitmap(bmp);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!x){
                    image.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    image.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    image.requestLayout();
                    x = true;
                }else{
                    image.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
                    image.getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;
                    image.requestLayout();
                    x = false;
                }
            }
        });

        ImageView back = findViewById(R.id.photoBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private Bitmap betterImageDecode(String filePath) {
        Bitmap myBitmap;
        BitmapFactory.Options options = new BitmapFactory.Options(); // opcje przekształcania bitmapy
        options.inSampleSize = 4; // zmniejszenie jakości bitmapy 4x
        //
        myBitmap = BitmapFactory.decodeFile(filePath, options);
        return myBitmap;
    }
}