package com.savar_computer.breaker.Classes;

import android.app.Application;

import com.savar_computer.breaker.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class ActivityFont extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(
                new CalligraphyConfig.Builder()
                        .setDefaultFontPath("Fonts/font.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build());
    }
}
