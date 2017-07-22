package com.savar_computer.breaker.Classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.View;

import com.savar_computer.breaker.Main;
import com.savar_computer.breaker.Splash;

public class DrawingView extends View {

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;
    private Paint mPaint;

    private int drawScreenH;
    private static boolean drawn = false;

    public DrawingView(Context c, int DrawScreenWidth, int DrawScreenHeight) {
        super(c);
        drawScreenH = DrawScreenHeight;
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //Set Size of Pen
        mPaint.setStrokeWidth(Splash.dpFromPx(c, DrawScreenWidth / 200));
        //Help to Draw Dashed Lines
        mPaint.setPathEffect(new DashPathEffect(new float[]{DrawScreenWidth / 90, DrawScreenWidth / 45}, 0));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath(mPath, mPaint);
    }

    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
    }

    private void touch_move(float x, float y) {
        if (drawScreenH - y > drawScreenH / 13) {
            //Clear Panel
            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            //Draw Line
            float x2, y2;
            float nesbat = Math.abs(Main.startX - x) / Math.abs(Main.startY - y);
            y2 = 0;
            x2 = nesbat * drawScreenH;
            if (Main.startX > x)
                x2 = Main.startX - x2;
            else
                x2 = Main.startX + x2;
            //TODO: Code to check line crash with bricks
            /*for(int i=0;i<Main.bricksCount;i++){
            }*/
            mCanvas.drawLine(Main.startX, Main.startY - Main.ball_radius / 2, x2, y2, mPaint);
            drawn = true;
        }
    }

    private void touch_up(float x, float y) {
        //Clean Panel
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        //Shoot Balls
        if (drawn) {
            Main.releaseBalls(x, y);
            drawn = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (Main.gameStatue == Main.Status.readyToShot) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    if (drawScreenH - y < drawScreenH / 13)
                        touch_up(x, drawScreenH - drawScreenH / 13);
                    else
                        touch_up(x, y);
                    invalidate();
                    break;
            }
        }
        invalidate();
        return true;
    }
}