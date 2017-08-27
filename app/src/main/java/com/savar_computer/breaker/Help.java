package com.savar_computer.breaker;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

public class Help extends Activity {

    private ImageSwitcher imageSwitcher;
    private Button next;

    private int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);

        imageSwitcher = (ImageSwitcher) findViewById(R.id.help_imageSwitcher);
        imageSwitcher.setFactory(() -> {
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ImageSwitcher.LayoutParams.MATCH_PARENT, ImageSwitcher.LayoutParams.MATCH_PARENT));
            imageView.setImageResource(R.drawable.help1);


            return imageView;
        });

        next = (Button) findViewById(R.id.help_next_btn);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"Fonts/font.ttf");
        next.setTypeface(typeface);
        next.setOnClickListener(v -> {
            if(count==4)
                next.setText("پابان");
            if (count < 5) {
                count++;
                String imageName = "help" + count;
                int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                imageSwitcher.setImageResource(resId);
            } else {
                onBackPressed();
            }
        });
    }
}
