package com.example.kubus001;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amitshekhar.DebugDB;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    private LinearLayout cameraButton, albumsButton, collageButton, networkButton, notesButton, newAlbumsButton;
    private ArrayList<Item> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void onRestart() {
        super.onRestart();

        refreshRecycler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Uprawnienia
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 100);
        checkPermission(Manifest.permission.CAMERA, 101);
        checkPermission(Manifest.permission.INTERNET, 101);
        checkPermission(Manifest.permission.ACCESS_NETWORK_STATE, 101);

        // Recycler View
        refreshRecycler();

        // Buttons
        cameraButton = findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                AlertDialog alert = builder.create();
                alert.setTitle("Wybierz źródło zdjęcia!");

                LinearLayout alertView = new LinearLayout(MainActivity.this);
                alert.setView(alertView);
                alertView.setOrientation(LinearLayout.VERTICAL);

                Button camera = new Button(MainActivity.this);
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

                Button gallery = new Button(MainActivity.this);
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


        albumsButton = findViewById(R.id.albumsButton);
        albumsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AlbumsActivity.class);
                startActivity(intent);
            }
        });

        collageButton = findViewById(R.id.collageButton);
        collageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CollageActivity.class);
                startActivity(intent);
            }
        });

        networkButton = findViewById(R.id.networkButton);
        networkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NetworkActivity.class);
                startActivity(intent);
            }
        });

        notesButton = findViewById(R.id.notesButton);
        notesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NotesActivity.class);
                startActivity(intent);
            }
        });

        newAlbumsButton = findViewById(R.id.newAlbums);
        newAlbumsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewAlbumsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void refreshRecycler() {
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String url = "http://" + preferences.getString("ip", null) + ":3000/json";

        JsonArrayRequest jsonRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    list.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject responseObj =  response.getJSONObject(i);

                            Item listItem = new Item(
                                    responseObj.getString("name"),
                                    responseObj.getString("url"),
                                    "czas zapisu: " + responseObj.getString("creationTime"),
                                    "wielkość zdjęcia: " + responseObj.getString("size") + " B"
                            );

                            list.add(listItem);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    adapter = new RecAdapter(MainActivity.this, list);
                    recyclerView.setAdapter(adapter);
                },
                error -> {
                    Log.d("XXX", "error" + error.getMessage());
                }
        );
        Volley.newRequestQueue(MainActivity.this).add(jsonRequest);
    }

    public void checkPermission(String permission, int requestCode) {
        // jeśli nie jest przyznane to zażądaj
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
        } else {
            Toast.makeText(MainActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
            createDir();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //tak
                    createDir();
                }
                checkPermission(Manifest.permission.CAMERA, 101);
                break;
            case 101:
                break;
        }
    }

    public void createDir() {
        // Utworznie folderu JakubKowal a w nim: miejsca, ludzie, rzeczy
        File picturesDir = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES );
        File mainDir = new File(picturesDir, "JakubKowal");
        mainDir.mkdir();

        String[] albums = {"miejsca", "ludzie", "rzeczy"};
        for (String element : albums) {
            File tempDir = new File(picturesDir + "/JakubKowal", element);
            tempDir.mkdir();
        }
    }

    // Saving photo from gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK && data != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                AlertDialog alert = builder.create();
                alert.setTitle("W którym folderze zapisać to zdjęcie?");

                LinearLayout alertView = new LinearLayout(MainActivity.this);
                alert.setView(alertView);
                alertView.setOrientation(LinearLayout.VERTICAL);

                File mainDir = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES + "/JakubKowal" );
                File[] files = mainDir.listFiles();
                String[] dirArray = new String[files.length];
                for (File file : files) {
                    if(!file.isDirectory()) continue;

                    Button b = new Button(MainActivity.this);
                    b.setBackgroundResource(0);
                    b.setText(file.getName());
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //odbiór danych z galerii
                            Uri imgData = data.getData();
                            InputStream stream = null;
                            try {
                                stream = getContentResolver().openInputStream(imgData);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            Bitmap b = BitmapFactory.decodeStream(stream);

                            // konwersja danych typu Bitmap na zapisywalną do pliku tablicę byte[]
                            ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
                            b.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayStream); // kompresja, typ pliku jpg, png
                            byte[] byteArray = byteArrayStream.toByteArray();

                            // nazwa zdjęcia - data bieżąca
                            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
                            String currentData = df.format(new Date());

                            //  zapis danych byte[] do pliku na urządzeniu
                            FileOutputStream fs = null;
                            File picturesDir = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES + "/JakubKowal/" + file.getName() + "/" + currentData + ".jpg" );

                            try {
                                fs = new FileOutputStream(picturesDir);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            try {
                                fs.write(byteArray);
                                fs.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            alert.cancel();
                        }
                    });
                    alertView.addView(b);
                }

                alert.show();
            }
        }

        if (requestCode == 200) {
            if (resultCode == RESULT_OK) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                AlertDialog alert = builder.create();
                alert.setTitle("W którym folderze zapisać to zdjęcie?");

                LinearLayout alertView = new LinearLayout(MainActivity.this);
                alert.setView(alertView);
                alertView.setOrientation(LinearLayout.VERTICAL);

                File mainDir = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES + "/JakubKowal" );
                File[] files = mainDir.listFiles();
                String[] dirArray = new String[files.length];
                for (File file : files) {
                    if(!file.isDirectory()) continue;

                    Button b = new Button(MainActivity.this);
                    b.setBackgroundResource(0);
                    b.setText(file.getName());
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Bundle extras = data.getExtras();
                            Bitmap b = (Bitmap) extras.get("data");

                            // konwersja danych typu Bitmap na zapisywalną do pliku tablicę byte[]
                            ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
                            b.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayStream); // kompresja, typ pliku jpg, png
                            byte[] byteArray = byteArrayStream.toByteArray();

                            // nazwa zdjęcia - data bieżąca
                            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
                            String currentData = df.format(new Date());

                            //  zapis danych byte[] do pliku na urządzeniu
                            FileOutputStream fs = null;
                            File picturesDir = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES + "/JakubKowal/" + file.getName() + "/" + currentData + ".jpg" );

                            try {
                                fs = new FileOutputStream(picturesDir);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            try {
                                fs.write(byteArray);
                                fs.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            alert.cancel();
                        }
                    });
                    alertView.addView(b);
                }

                alert.show();
            }
        }
    }
}