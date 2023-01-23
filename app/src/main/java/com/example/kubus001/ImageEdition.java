package com.example.kubus001;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

public class ImageEdition {
    public static Bitmap changeBrightness(Bitmap oldBitmap, Integer brightness) {
        float[] bightness_tab = {
                1, 0, 0, 0, brightness,
                0, 1, 0, 0, brightness,
                0, 0, 1, 0, brightness,
                0, 0, 0, 1, 0
        };

        ColorMatrix cMatrix = new ColorMatrix();
        cMatrix.set(bightness_tab);
        Paint paint = new Paint();
        Bitmap newBitmap = Bitmap.createBitmap(oldBitmap.getWidth(), oldBitmap.getHeight(), oldBitmap.getConfig());
        paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(oldBitmap, 0, 0, paint);
        return newBitmap;
    }
}
