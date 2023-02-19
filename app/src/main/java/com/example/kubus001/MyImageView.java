package com.example.kubus001;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

public class MyImageView extends androidx.appcompat.widget.AppCompatImageView {
    private Paint paint;
    public Float startX;
    public Float startY;
    public Float endX;
    public Float endY;

    public MyImageView(@NonNull Context context) {
        super(context);

        this.setBackgroundColor(0x00000000); // zielony kolor tła ImageView
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        this.setLayoutParams(layoutParams);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG); // obiekt Paint z atrybutami do rysowania
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL); // wypełnienie rysowanego na View elementu
        paint.setColor(0xffffff00);
        paint.setStrokeWidth(10);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE); // krawędź rysowanego na View elementu
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(startX != null && startY != null && endX != null && endY != null) {
            canvas.drawRect(startX, startY, endX, endY, paint);
        }else {
            Paint clearPaint = new Paint();
            clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            canvas.drawRect(0, 0, 0, 0, clearPaint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("xxx", "DOWN -> pozycja: " + event.getX() + "-" + event.getY());
                startX = event.getX();
                startY = event.getY();
                endX = null;
                endY = null;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
//                Log.d("xxx", "MOVE -> pozycja: " + event.getX() + "-" + event.getY());
                endX = event.getX();
                endY = event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                Log.d("xxx", "UP -> pozycja: " + event.getX() + "-" + event.getY());
                break;
        }
        return true;
    }

    public void repaintAll(){
        startX = null;
        startY = null;
        endX = null;
        endY = null;

        invalidate();
    }
}
