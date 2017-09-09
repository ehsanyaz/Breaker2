package com.savar_computer.breaker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Splash extends Activity {

    private Handler handler;

    private int ScreenW, ScreenH;

    private ImageView breaker_icon;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        //get Size of Screen
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        ScreenH = displayMetrics.heightPixels;
        ScreenW = displayMetrics.widthPixels;

        //Set Splash Image Settings
        breaker_icon = (ImageView) findViewById(R.id.splash_breaker);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ScreenW - 70, ScreenW - 70);
        breaker_icon.setLayoutParams(params);
        breaker_icon.setX(35);
        breaker_icon.setY(ScreenH / 20);

        //Set Text of Splash Settings
        textView = (TextView) findViewById(R.id.splash_txt);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Fonts/font.ttf");
        textView.setTypeface(typeface);
        textView.setTextSize(dpFromPx(getApplicationContext(), ScreenW / 20));
        textView.setY((ScreenH / 20) + (ScreenW - 70) + 100);

        //Component Animations
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_breaker_icon);
        breaker_icon.startAnimation(animation);


        //After 1sec the Splash will Finish and Code below will run
        handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent;
            intent = new Intent(getApplicationContext(), Menu.class);
            //The Switch between Activities Will be with Animation
            this.overridePendingTransition(R.anim.menu_come, R.anim.splash_go);
            startActivity(intent);
            //When the user is in the Menu Activity when press back it will not return to
            //this Activity,in other words it remove this Activity from Activity Stack
            finish();
        }, 1000);
    }

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }
}
