package com.example.kubus001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class CollageActivity extends AppCompatActivity {

    private final ArrayList<ImageData> list = new ArrayList<>();

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collage);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        size.y -= 100;

        ImageView leftCollage = findViewById(R.id.leftCollage);
        leftCollage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CollageActivity.this, CollageMakerActivity.class);

                list.clear();
                list.add(new ImageData(0,0, size.x,size.y/2));
                list.add(new ImageData(0,size.y/2, size.x,size.y/2));
                intent.putExtra("list", list);

                startActivity(intent);
            }
        });

        ImageView rightCollage = findViewById(R.id.rightCollage);
        rightCollage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CollageActivity.this, CollageMakerActivity.class);

                list.clear();
                list.add(new ImageData(0,0,size.x/3*2,size.y));
                list.add(new ImageData(size.x/3*2,0,size.x/3,size.y/3));
                list.add(new ImageData(size.x/3*2,size.y/3,size.x/3,size.y/3));
                list.add(new ImageData(size.x/3*2,size.y/3*2,size.x/3,size.y/3));
                intent.putExtra("list", list);

                startActivity(intent);
            }
        });
    }

}

