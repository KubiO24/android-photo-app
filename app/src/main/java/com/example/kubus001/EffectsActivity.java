package com.example.kubus001;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Objects;

public class EffectsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effects);

        ImageView image = findViewById(R.id.effectsImageView);
        image.setImageBitmap(ImageEdition.myBitmap);
        Bitmap b = ImageEdition.myBitmap;
        ImageEdition.myBitmap = null;

        LinearLayout effectLayout = findViewById(R.id.effectsLayout);

        Bitmap smallBmp = Bitmap.createScaledBitmap(b, 60, 60, true);

        ImageView normal = new ImageView(this);
        normal.setImageBitmap(smallBmp);
        normal.setOnClickListener(v -> changeEffect("normal", b));
        effectLayout.addView(normal, 400, 400);

        ImageView blackWhite = new ImageView(this);
        blackWhite.setImageBitmap(ImageEdition.changeColor(smallBmp, "blackWhite"));
        blackWhite.setOnClickListener(v -> changeEffect("blackWhite", b));
        effectLayout.addView(blackWhite, 400, 400);

        ImageView negative = new ImageView(this);
        negative.setImageBitmap(ImageEdition.changeColor(smallBmp, "negative"));
        negative.setOnClickListener(v -> changeEffect("negative", b));
        effectLayout.addView(negative, 400, 400);

        ImageView red = new ImageView(this);
        red.setImageBitmap(ImageEdition.changeColor(smallBmp, "red"));
        red.setOnClickListener(v -> changeEffect("red", b));
        effectLayout.addView(red, 400, 400);

        ImageView green = new ImageView(this);
        green.setImageBitmap(ImageEdition.changeColor(smallBmp, "green"));
        green.setOnClickListener(v -> changeEffect("green", b));
        effectLayout.addView(green, 400, 400);

        ImageView blue = new ImageView(this);
        blue.setImageBitmap(ImageEdition.changeColor(smallBmp, "blue"));
        blue.setOnClickListener(v -> changeEffect("blue", b));
        effectLayout.addView(blue, 400, 400);

        ImageView done = findViewById(R.id.effectDone);
        done.setOnClickListener(v -> {
            Bitmap newBitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
            ImageEdition.myBitmap = newBitmap;
            finish();
        });
    }

    private void changeEffect(String text, Bitmap b) {
        RelativeLayout mainLayout = findViewById(R.id.effectsMainLayout);
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
                                    ImageView image = findViewById(R.id.effectsImageView);
                                    image.setImageBitmap(ImageEdition.changeColor(b, text));
                                })
                                .start();
                    }, 500);
                })
                .start();
    }
}