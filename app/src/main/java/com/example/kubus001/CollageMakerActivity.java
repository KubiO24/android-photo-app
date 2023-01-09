package com.example.kubus001;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CollageMakerActivity extends AppCompatActivity {
    private ImageView selectedIv;

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
        setContentView(R.layout.activity_collage_maker);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        ImageView flip = findViewById(R.id.collageFlip);
        flip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Matrix matrix = new Matrix();
                matrix.postScale(-1.0f, 1.0f);

                Bitmap oryginal = ((BitmapDrawable) selectedIv.getDrawable()).getBitmap();
                Bitmap rotated = Bitmap.createBitmap(oryginal, 0, 0, oryginal.getWidth(), oryginal.getHeight(), matrix, true);

                selectedIv.setImageBitmap(rotated);
            }
        });

        ImageView rotate = findViewById(R.id.collageRotate);
        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Matrix matrix = new Matrix();
                matrix.postRotate(90);

                Bitmap oryginal = ((BitmapDrawable) selectedIv.getDrawable()).getBitmap();
                Bitmap rotated = Bitmap.createBitmap(oryginal, 0, 0, oryginal.getWidth(), oryginal.getHeight(), matrix, true);

                selectedIv.setImageBitmap(rotated);
            }
        });

        ImageView done = findViewById(R.id.collageDone);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("XXX", "done");
            }
        });

        ArrayList<ImageData> list = (ArrayList<ImageData>) getIntent().getSerializableExtra("list");

        FrameLayout fL = (FrameLayout)findViewById(R.id.frameLayout);
        for (ImageData img : list) {
            ImageView iv = new ImageView(this);
            iv.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24);
            iv.setX(img.getX());
            iv.setY(img.getY());
            iv.setLayoutParams(new FrameLayout.LayoutParams(img.getW(), img.getH()));

            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedIv = iv;
                    // return if image is already assinged
                    if(selectedIv.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.ic_baseline_add_photo_alternate_24).getConstantState()) return;
                    AlertDialog.Builder builder = new AlertDialog.Builder(CollageMakerActivity.this);
                    AlertDialog alert = builder.create();
                    alert.setTitle("Wybierz źródło zdjęcia!");

                    LinearLayout alertView = new LinearLayout(CollageMakerActivity.this);
                    alert.setView(alertView);
                    alertView.setOrientation(LinearLayout.VERTICAL);

                    Button camera = new Button(CollageMakerActivity.this);
                    camera.setBackgroundResource(0);
                    camera.setText("aparat");
                    camera.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            //jeśli jest dostępny aparat
                            if (intent.resolveActivity(getPackageManager()) != null) {
                                startActivityForResult(intent, 200); // 200 - stała wartość, która później posłuży do identyfikacji tej akcji
                                alert.cancel();
                            }
                        }
                    });

                    Button gallery = new Button(CollageMakerActivity.this);
                    gallery.setBackgroundResource(0);
                    gallery.setText("galeria");
                    gallery.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent, 100); // 100 - stała wartość, która później posłuży do identyfikacji tej akcji
                            alert.cancel();
                        }

                    });

                    alertView.addView(camera);
                    alertView.addView(gallery);

                    alert.show();
                }
            });

            fL.addView(iv);
        }
    }


    // Saving photo from gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK && data != null) {
                Uri imgData = data.getData();
                InputStream stream = null;
                try {
                    stream = getContentResolver().openInputStream(imgData);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap b = BitmapFactory.decodeStream(stream);

                selectedIv.setImageBitmap(b);
                selectedIv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

        }

        if (requestCode == 200) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap b = (Bitmap) extras.get("data");

                selectedIv.setImageBitmap(b);
                selectedIv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        }
    }
}