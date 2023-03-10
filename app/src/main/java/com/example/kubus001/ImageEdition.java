package com.example.kubus001;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

public class ImageEdition {
    public static Bitmap myBitmap = null;

    private static Bitmap returnBitmap(Bitmap oldBitmap, ColorMatrix cMatrix) {
        Paint paint = new Paint();
        Bitmap newBitmap = Bitmap.createBitmap(oldBitmap.getWidth(), oldBitmap.getHeight(), oldBitmap.getConfig());
        paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(oldBitmap, 0, 0, paint);
        return newBitmap;
    }

    public static Bitmap changeBrightness(Bitmap oldBitmap, Integer value) {
        float[] bightness_tab = {
                1, 0, 0, 0, value,
                0, 1, 0, 0, value,
                0, 0, 1, 0, value,
                0, 0, 0, 1, 0
        };

        ColorMatrix cMatrix = new ColorMatrix();
        cMatrix.set(bightness_tab);

        return returnBitmap(oldBitmap, cMatrix);
    }

    public static Bitmap changeContrast(Bitmap oldBitmap, Float value) {
        float[] contrast_tab = {
                value, 0, 0, 0, 0,
                0, value, 0, 0, 0,
                0, 0, value, 0, 0,
                0, 0, 0, 1, 0
        };

        ColorMatrix cMatrix = new ColorMatrix();
        cMatrix.set(contrast_tab);

        return returnBitmap(oldBitmap, cMatrix);
    }

    public static Bitmap changeSaturation(Bitmap oldBitmap, Float value) {
        ColorMatrix cMatrix = new ColorMatrix();
        cMatrix.setSaturation(value);

        return returnBitmap(oldBitmap, cMatrix);
    }

    public static Bitmap changeColor(Bitmap oldBitmap, String type) {
        ColorMatrix cMatrix = new ColorMatrix();

        switch(type) {
            case "normal":
                cMatrix.set(new float[]{
                        1, 0, 0, 0, 0,
                        0, 1, 0, 0, 0,
                        0, 0, 1, 0, 0,
                        0, 0, 0, 1, 0
                });
                break;

            case "blackWhite":
                cMatrix.set(new float[]{
                    1, 1, 1, 0, 0,
                    1, 1, 1, 0, 0,
                    1, 1, 1, 0, 0,
                    0, 0, 0, 1, 0
                });
                break;

            case "negative":
                cMatrix.set(new float[]{
                        -1, 0, 0, 1, 0,
                        0, -1, 0, 1, 0,
                        0, 0, -1, 1, 0,
                        0, 0, 0, 1, 0
                });
                break;

            case "red":
                cMatrix.set(new float[]{
                        1, 0, 0, 0, 0,
                        0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0,
                        0, 0, 0, 1, 0
                });
                break;

            case "green":
                cMatrix.set(new float[]{
                        0, 0, 0, 0, 0,
                        0, 1, 0, 0, 0,
                        0, 0, 0, 0, 0,
                        0, 0, 0, 1, 0
                });
                break;

            case "blue":
                cMatrix.set(new float[]{
                        0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0,
                        0, 0, 1, 0, 0,
                        0, 0, 0, 1, 0
                });
                break;
        }

        return returnBitmap(oldBitmap, cMatrix);
    }
}
