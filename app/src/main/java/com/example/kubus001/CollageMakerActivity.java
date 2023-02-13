package com.example.kubus001;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
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
import android.widget.Toast;

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
    private ArrayList<ImageView> setImageViews;

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

        setImageViews = new ArrayList<ImageView>();

        ArrayList<ImageData> list = (ArrayList<ImageData>) getIntent().getSerializableExtra("list");
        FrameLayout fL = (FrameLayout)findViewById(R.id.frameLayout);
        ImageView flip = findViewById(R.id.collageFlip);
        ImageView rotate = findViewById(R.id.collageRotate);
        ImageView done = findViewById(R.id.collageDone);

        fL.setDrawingCacheEnabled(true);

        flip.setOnClickListener(v -> {
            if(selectedIv == null) return;

            Matrix matrix = new Matrix();
            matrix.postScale(-1.0f, 1.0f);

            Bitmap oryginal = ((BitmapDrawable) selectedIv.getDrawable()).getBitmap();
            Bitmap rotated = Bitmap.createBitmap(oryginal, 0, 0, oryginal.getWidth(), oryginal.getHeight(), matrix, true);

            selectedIv.setImageBitmap(rotated);
        });

        rotate.setOnClickListener(v -> {
            if(selectedIv == null) return;

            Matrix matrix = new Matrix();
            matrix.postRotate(90);

            Bitmap oryginal = ((BitmapDrawable) selectedIv.getDrawable()).getBitmap();
            Bitmap rotated = Bitmap.createBitmap(oryginal, 0, 0, oryginal.getWidth(), oryginal.getHeight(), matrix, true);

            selectedIv.setImageBitmap(rotated);
        });

        done.setOnClickListener(v -> {
            Bitmap b = fL.getDrawingCache(true);

            // Utworznie folderu collages
            File mainDir = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES );
            File dir = new File(mainDir + "/JakubKowal", "collages");
            dir.mkdir();

            // konwersja danych typu Bitmap na zapisywalną do pliku tablicę byte[]
            ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayStream); // kompresja, typ pliku jpg, png
            byte[] byteArray = byteArrayStream.toByteArray();

            // nazwa zdjęcia - data bieżąca
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String currentData = df.format(new Date());

            //  zapis danych byte[] do pliku na urządzeniu
            FileOutputStream fs = null;
            File collagesDir = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES + "/JakubKowal/collages/" + currentData + ".jpg" );

            try {
                fs = new FileOutputStream(collagesDir);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                fs.write(byteArray);
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Context context = getApplicationContext();
            CharSequence text = "Collage saved";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        });


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

                    if(setImageViews.contains(selectedIv)) return;

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

        setImageViews.add(selectedIv);
    }
}