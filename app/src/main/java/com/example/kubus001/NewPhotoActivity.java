package com.example.kubus001;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bikomobile.multipart.Multipart;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class NewPhotoActivity extends AppCompatActivity {
    boolean showControls = false;
    ImageView image;
    String selectedEffect;
    Bitmap originalBitmap;
    SeekBar seekBar;
    ProgressDialog pDialog;
    String imagepath;
    Boolean isCrop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_photo);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        pDialog = new ProgressDialog(NewPhotoActivity.this);
        pDialog.setMessage("Wysy??anie zdj??cia na serwer");
        pDialog.setCancelable(false); // nie da si?? zamkn???? klikaj??c w ekran

        Intent myIntent = getIntent();
        imagepath = myIntent.getStringExtra("imagepath") ;
        originalBitmap = betterImageDecode(imagepath);    // w??asna funkcja betterImageDecode opisana jest poni??ej

        RelativeLayout settings = findViewById(R.id.photoSettings);
        RelativeLayout controls = findViewById(R.id.photoControls);
        image = findViewById(R.id.newPhotoImageView);
        image.setImageBitmap(originalBitmap);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(showControls){
                    settings.animate().translationY(convertDpToPx(-80));
                    controls.animate().translationY(convertDpToPx(90));
                    showControls = false;
                    seekBar.setAlpha(0f);
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
                animateText("brightness");
            }
        });

        ImageView contrast = findViewById(R.id.newPhotoContrast);
        contrast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateText("contrast");
            }
        });

        ImageView saturation = findViewById(R.id.newPhotoSaturation);
        saturation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateText("saturation");
            }
        });

        seekBar = findViewById(R.id.newPhotoSeekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                changeEffect(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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
                image.setImageBitmap(originalBitmap);
            }
        });

        ImageView upload = findViewById(R.id.newPhotoUpload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        EditText ipEditText = findViewById(R.id.network_ip_editText);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(NewPhotoActivity.this);

        if (preferences.getString("ip", null) != null) {
            ipEditText.setText(preferences.getString("ip", null));
        }else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("ip", "192.168.0.1");
            editor.commit();
        }

        Button saveIpButton = findViewById(R.id.network_saveIp_button);
        saveIpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("ip", String.valueOf(ipEditText.getText()));
                editor.commit();
                Toast.makeText(NewPhotoActivity.this, "IP saved", Toast.LENGTH_SHORT).show();
            }
        });
        List<String> networkArray = new ArrayList<String>();
        networkArray.add("upload");
        networkArray.add("share");
        networkArray.add("effects");

        NetworkArrayAdapter adapter = new NetworkArrayAdapter(
                NewPhotoActivity.this,
                R.layout.network_listview_row,
                networkArray
        );
        ListView networkListView = findViewById(R.id.network_listView);
        networkListView.setAdapter(adapter);

        networkListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i) {
                    case 0:
                        if(!Networking.isConnectedToWifi(NewPhotoActivity.this)) {
                            new AlertDialog.Builder(NewPhotoActivity.this)
                                    .setTitle("Brak internetu")
                                    .setMessage("Nie mo??na wys??a?? zdj??cia na serwer")
                                    .setPositiveButton("Ok", null)
                                    .show();
                            return;
                        }

                        new AlertDialog.Builder(NewPhotoActivity.this)
                                .setTitle("Upload")
                                .setMessage("Czy wys??a?? zdj??cie?")

                                .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        pDialog.show();

                                        Bitmap bmp = ((BitmapDrawable)image.getDrawable()).getBitmap();
                                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                        byte[] byteArray = stream.toByteArray();

                                        Multipart multipart = new Multipart(NewPhotoActivity.this);
                                        multipart.addFile("image/jpeg", "file", imagepath, byteArray);
                                        String uploadAddress = "http://" + preferences.getString("ip", null) + ":3000/upload";
                                        multipart.launchRequest(uploadAddress,
                                                response -> {
                                                    pDialog.dismiss();
                                                },
                                                error -> {
                                                    pDialog.dismiss();
                                                });
                                    }
                                })
                                .setNegativeButton("Nie", null)
                                .show();
                        break;

                    case 1:
                        Uri uri = null;
                        try {
                            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "to-share.png");
                            FileOutputStream stream = new FileOutputStream(file);
                            Bitmap b = ((BitmapDrawable)image.getDrawable()).getBitmap();
                            b.compress(Bitmap.CompressFormat.PNG, 90, stream);
                            stream.close();
                            uri = Uri.fromFile(file);
                        } catch (IOException e) {
                            break;
                        }

                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("image/png");
                        share.putExtra(Intent.EXTRA_STREAM, uri);
                        startActivity(Intent.createChooser(share, "Share this image!"));
                        break;

                    case 2:
                        Bitmap b = ((BitmapDrawable) image.getDrawable()).getBitmap();
                        ImageEdition.myBitmap = b;
                        Intent intent = new Intent(NewPhotoActivity.this, EffectsActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });

        RelativeLayout mainLayout = findViewById(R.id.newPhotoMainLayout);
        MyImageView myImg = new MyImageView(this);
        ImageView cropButton = findViewById(R.id.cropButton);
        cropButton.setOnClickListener(v -> {
            myImg.repaintAll();
            seekBar.setAlpha(0f);
            myImg.invalidate();
            if(isCrop) {
                return;
            }
            isCrop = true;

            mainLayout.addView(myImg);
        });

        ImageView cropDone = findViewById(R.id.cropDone);
        cropDone.setOnClickListener(v -> {
            mainLayout.removeView(myImg);
            isCrop = false;

            Integer x = Math.round(myImg.startX);
            Integer y = Math.round(myImg.startY);
            Integer w = Math.round(myImg.endX - myImg.startX);
            Integer h = Math.round(myImg.endY - myImg.startY);

            Log.d("xxx", "x: " + x + " | y: " + y + " | w: " + w + " | h:" + h);

            if(x>0 && y>0 && w>0 && h>0) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                Float screenHeight = (float) displayMetrics.heightPixels;
                Float screenWidth = (float) displayMetrics.widthPixels;

                Bitmap b = ((BitmapDrawable) image.getDrawable()).getBitmap();
                Float imageWidth = (float) b.getWidth();
                Float imageHeight = (float) b.getHeight();

                Float ratioX = (float) (imageWidth / screenWidth);
                Float ratioY = (float) (imageHeight / screenHeight);

                int finalX = (int) (x * ratioX);
                int finalY = (int) (y * ratioY);
                int finalW = (int) (w * ratioX);
                int finalH = (int) (h * ratioY);

//                Log.d("xxx", "x: " + finalX + " | y: " + finalY + " | w: " + finalW + " | h: " + finalH);

                Bitmap croppedBitmap = Bitmap.createBitmap(b, finalX, finalY, finalW, finalH);
                image = findViewById(R.id.newPhotoImageView);
                image.setImageBitmap(croppedBitmap);
            }
        });
    }

    @Override
    public void onRestart() {
        super.onRestart();

        image.setImageBitmap(ImageEdition.myBitmap);
        ImageEdition.myBitmap = null;
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private Bitmap betterImageDecode(String filePath) {
        Bitmap myBitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();    //opcje przekszta??cania bitmapy
        options.inSampleSize = 4; // zmniejszenie jako??ci bitmapy 4x
        //
        myBitmap = BitmapFactory.decodeFile(filePath, options);
        return myBitmap;
    }

    private int convertDpToPx(int dp){
        return Math.round(dp*(getResources().getDisplayMetrics().xdpi/DisplayMetrics.DENSITY_DEFAULT));
    }

    private void animateText(String text) {
        RelativeLayout mainLayout = findViewById(R.id.newPhotoMainLayout);
        TextView textView = new TextView(this);
        textView.setLayoutParams(
            new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
        );
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) textView.getLayoutParams();
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        textView.setLayoutParams(lp);
        textView.setText(text);
        textView.setTextColor(0xFFFFFFFF);
        textView.setTextSize(24);
        textView.setAlpha(0f);
        mainLayout.addView(textView);
        seekBar.setAlpha(0f);
        textView.animate()
                .alpha(1f)
                .setInterpolator(new AccelerateInterpolator())
                .setDuration(500)
                .withEndAction(()->{
                    new Handler().postDelayed(() -> {
                        textView.animate()
                                .alpha(0f)
                                .setInterpolator(new AccelerateInterpolator())
                                .setDuration(500)
                                .withEndAction(()->{
                                    mainLayout.removeView(textView);
                                    if(!Objects.equals(selectedEffect, text)) {
                                        originalBitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
                                    }
                                    selectedEffect = text;
                                    seekBar.setAlpha(1f);
                                    seekBar.setProgress(0);
                                })
                                .start();
                    }, 500);
                })
                .start();
    }

    private void changeEffect(Integer value) {
        Float floatValue = (value/100f) + 1;
        switch(selectedEffect) {
            case "brightness":
                Bitmap brightnessBitmap = ImageEdition.changeBrightness(originalBitmap, value);
                image.setImageBitmap(brightnessBitmap);
                break;

            case "contrast":
                Bitmap contrastBitmap = ImageEdition.changeContrast(originalBitmap, floatValue);
                image.setImageBitmap(contrastBitmap);
                break;

            case "saturation":
                Bitmap saturationBitmap = ImageEdition.changeSaturation(originalBitmap, floatValue);
                image.setImageBitmap(saturationBitmap);
                break;
        }
    }
}