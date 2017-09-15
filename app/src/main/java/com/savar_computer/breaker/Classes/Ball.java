package com.savar_computer.breaker.Classes;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.savar_computer.breaker.Main;

public class Ball extends android.support.v7.widget.AppCompatImageView implements ValueAnimator.AnimatorUpdateListener {

    public static boolean releasedAnyInLevel = false;

    public ValueAnimator animator;

    private float stepX = -1f, stepY = -1f;
    private long duration = 100;
    private int radius;


    public Ball(Context context, int radius) {
        super(context);
        this.radius = radius;
        //Set Size
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(this.radius, this.radius);
        params.setMargins(0,0,0,0);
        this.setLayoutParams(params);


        /* In the Previous Versions of app we Used An Image to Draw Ball
        but in the newer Versions we Had used OnDraw() method to do this */
        //setImageResource(R.drawable.ball);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(radius / 2, radius / 2, this.radius / 2, paint);
    }


    //--------------------------------------------------------------------------Animator Methods
    //Set Animation Ready

    private void readyAnimation(int startDelay) {
        //TODO: I still don't Know what this Method DO
        animator = null;
        animator = new ValueAnimator();
        animator.setDuration(duration);
        animator.setFloatValues(1);
        animator.setStartDelay(startDelay);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setTarget(this);
        //animator.addListener(this);
        animator.addUpdateListener(this);
        releasedAnyInLevel = true;
        animator.start();
    }

    // The Method Below was Used at Previous Versions But that Was useless SO I Commented Them
    /*  @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {

    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }*/

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        //To check the Crash with Walls and Bricks
        checkWalls();
        checkBricks();

        animator.setDuration(animator.getDuration() + duration);
        this.setX((this.getX() + stepX));
        this.setY((this.getY() + stepY));

        //This method help to have better Graphics and Will update Graphic of Layou
        Main.inner_layout.invalidate();
        this.invalidate();
    }

    private void checkWalls() {
        if (this.getX() + stepX < 0)
            this.stepX = -1 * this.stepX;
        if (this.getY() + stepY < 0)
            this.stepY = -1 * this.stepY;
        if (this.getX() + stepX + this.radius >= Main.inner_layout_width)
            this.stepX = -1 * this.stepX;
        if (this.getY() + stepY + this.radius >= Main.inner_layout_height) {
            animator.cancel();
            if (Main.newStartX == -1 && Main.newStartY == -1) {
                //TODO:This code has to be update and is wrong still
                Main.newStartY = this.getY() + stepY + radius / 2;
                Main.newStartX = this.getX() + this.radius / 2;
            }
            //this.setX(Main.newStartX - radius / 2);
            //this.setY(Main.newStartY - radius);
            if (ballsEnded()) {
                if (releasedAnyInLevel) {
                    Main.nextLevel();
                    releasedAnyInLevel = false;
                }
            }
        }
    }

    //TODO:not complete
    private void checkBricks() {
        boolean changedWay = false;
        for (int i = 0; i < Main.bricksCount; i++) {
            //changedWay boolean will help to don't change direction in order that two bricks are close
            if (!changedWay) {
                if (Main.bricks.get(i).destroyed)
                    continue;
                if (Main.bricks.get(i).getX() - this.radius <= 5+this.getX() && Main.bricks.get(i).getY() - this.radius <= 5+this.getY() && Main.bricks.get(i).getX() + Main.bricks.get(i).getWidth() + this.radius+5 >= this.getX() + this.radius && Main.bricks.get(i).getY() + Main.bricks.get(i).getHeight() + this.radius+5 >= this.getY() + this.radius) {
                    Log.e("ball","hited");
                    if (this.getX() - (Main.bricks.get(i).getX() - this.radius) < Math.abs(stepX)) {
                        this.stepX = -1 * this.stepX;
                    }
                    if (this.getY() - (Main.bricks.get(i).getY() - this.radius) < Math.abs(stepY)) {
                        this.stepY = -1 * this.stepY;
                    }
                    if ((Main.bricks.get(i).getY() + Main.bricks.get(i).getHeight()) - this.getY() < Math.abs(stepY)) {
                        this.stepY = -1 * this.stepY;
                    }
                    if ((Main.bricks.get(i).getX() + Main.bricks.get(i).getWidth()) - this.getX() < Math.abs(stepX)) {
                        this.stepX = -1 * this.stepX;
                    }
                    changedWay = true;
                    invalidate();
                    Main.bricks.get(i).decreaseAmount();
                }
            }
        }
    }

    //Checking that is all of balls stop or not
    private boolean ballsEnded() {
        for (int i = 0; i < Main.ballsCount; i++) {
            if (Main.balls.get(i) != null) {
                if (Main.balls.get(i).animator != null) {
                    if (Main.balls.get(i).animator.isRunning())
                        return false;
                }
            }
        }
        return true;
    }

    public void releaseBall(float x, float y, int startDelay) {
        this.stepX = x;
        this.stepY = y;
        readyAnimation(startDelay);
    }
}
