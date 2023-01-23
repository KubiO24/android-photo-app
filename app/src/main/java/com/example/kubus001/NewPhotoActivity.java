package com.example.kubus001;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class NewPhotoActivity extends AppCompatActivity {
    boolean showControls = false;
    ImageView image;
    String selectedEffect;
    Bitmap originalBitmap;
    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_photo);

        Intent myIntent = getIntent();
        String imagepath = myIntent.getStringExtra("imagepath") ;
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
                        selectedEffect = text;
                        seekBar.setAlpha(1f);

                        textView.animate()
                                .alpha(0f)
                                .setInterpolator(new AccelerateInterpolator())
                                .setDuration(500)
                                .withEndAction(()->{
                                    mainLayout.removeView(textView);
                                })
                                .start();
                    }, 500);
                })
                .start();
    }

    private void changeEffect(Integer value) {
        switch(selectedEffect) {
            case "brightness":
                Log.d("XXX", value.toString());
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