package com.savar_computer.breaker.Classes;

import android.content.Context;
import android.graphics.Color;

import android.graphics.Typeface;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.savar_computer.breaker.Main;
import com.savar_computer.breaker.R;
import com.savar_computer.breaker.Splash;

public class Brick extends android.support.v7.widget.AppCompatTextView {

    public static final int margin = 4;
    public int amount = 1;
    public boolean destroyed = false;

    private Context context;


    public Brick(Context context, int width, int height, int amount) {
        super(context);
        this.context = context;
        //Set Size
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width - this.margin, height - this.margin);
        //params.setMargins(margin, margin, margin, margin);
        this.setLayoutParams(params);
        this.amount = amount;
        upgradeText();
        this.updateColor();
        Typeface typeface=Typeface.createFromAsset(context.getAssets(), "Fonts/font.ttf");
        this.setTypeface(typeface);
        this.setGravity(Gravity.CENTER);
        this.setTextSize(Splash.dpFromPx(context,(int)(width/3.5)));
    }

    public void decreaseAmount() {
        if (amount > 1) {
            this.amount--;
            upgradeText();
        } else {
            this.amount--;
            this.setText("");
            destroyed = true;
            destroyAnimation();
            Main.inner_layout.removeView(this);
            Main.bricksRemove(this);
        }
        updateColor();
    }

    private void destroyAnimation() {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.destroy_brick);
        this.startAnimation(animation);
    }

    public void nextLevelAnimation() {
        this.setY(this.getY() + margin + this.getHeight());
        if(this.getY()+margin+10+this.getHeight()>=Main.inner_layout_height && !destroyed){
            this.setText("");
            destroyed = true;
            destroyAnimation();
            Main.inner_layout.removeView(this);
            Toast.makeText(context,"باخیتد",Toast.LENGTH_LONG).show();
            Main.gameStatue =Main.Status.loosed;
        }
    }

    public void upgradeText() {
        this.setText(this.amount + "");
    }

    public void updateColor() {
        int a=amount%70;
        if(a==10)
            this.setBackgroundColor(Color.BLUE);
        if(a<10)
            this.setBackgroundColor(Color.rgb(155+a*10,0,0));
        else if(a<20)
            this.setBackgroundColor(Color.rgb(0,100+a*8,0));
        else if(a<30) {
            this.setBackgroundColor(Color.rgb(0, 0, 155 + a * 10));
            this.setTextColor(Color.rgb(255,255,255));
        }
        else if(a<40)
            this.setBackgroundColor(Color.rgb(0,155+a*10,155+a*5));
        else if(a<50)
            this.setBackgroundColor(Color.rgb(155+a*5,0,155+a*10));
        else if(a<60)
            this.setBackgroundColor(Color.rgb(155+a*10,155+a*5,0));
        else if(a<70)
            this.setBackgroundColor(Color.rgb(100+a*5,50+a*5,75+a*5));

    }
    
}
