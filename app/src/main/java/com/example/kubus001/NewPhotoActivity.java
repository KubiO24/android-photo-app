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
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NewPhotoActivity extends AppCompatActivity {
    boolean showControls = false;
    ImageView image;
    String selectedEffect;
    Bitmap originalBitmap;
    SeekBar seekBar;
    ProgressDialog pDialog;
    String imagepath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_photo);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        pDialog = new ProgressDialog(NewPhotoActivity.this);
        pDialog.setMessage("Wysyłanie zdjęcia na serwer");
        pDialog.setCancelable(false); // nie da się zamknąć klikając w ekran

        Intent myIntent = getIntent();
        imagepath = myIntent.getStringExtra("imagepath") ;
        originalBitmap = betterImageDecode(imagepath);    // własna funkcja betterImageDecode opisana jest poniżej

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
                Log.d("XXX", "revert");
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
        networkArray.add("crop");

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
                                    .setMessage("Nie można wysłać zdjęcia na serwer")
                                    .setPositiveButton("Ok", null)
                                    .show();
                            return;
                        }

                        new AlertDialog.Builder(NewPhotoActivity.this)
                                .setTitle("Upload")
                                .setMessage("Czy wysłać zdjęcie?")

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
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("image/jpeg");
                        share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+imagepath));
                        startActivity(Intent.createChooser(share, "Share this image!"));
                        break;

                    case 2:
                        // crop
                        break;
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
                                    selectedEffect = text;
                                    seekBar.setAlpha(1f);
                                })
                                .start();
                    }, 500);
                })
                .start();
    }

    private void changeEffect(Integer value) {
        switch(selectedEffect) {
            case "brightness":
                Bitmap newBitmap = ImageEdition.changeBrightness(originalBitmap, value);
                image.setImageBitmap(newBitmap);
                break;

            case "contrast":
                // code block
                break;

            case "saturation":
                // code block
                break;
        }
    }
}