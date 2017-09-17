package com.savar_computer.breaker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import ir.adad.client.Adad;
import ir.adad.client.InterstitialAdListener;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Menu extends Activity {

    private RelativeLayout inner_layout;
    private RelativeLayout score_layout;

    private int ScreenW, ScreenH;
    private int inner_layout_width, inner_layout_height;

    private Button start_button;

    private ImageView setting;
    private ImageView amar;
    private ImageView help;

    private TextView high_Score_text;
    private TextView high_Score_Score;

    private SharedPreferences sharedPreferences;

    public static Context context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Adad.initialize(getApplicationContext());
        setContentView(R.layout.menu);

        //tabligh();

        Menu.context = this.getApplicationContext();

        RelativeLayout wholeLayout = (RelativeLayout) findViewById(R.id.menu_whole_layout);

        score_layout = (RelativeLayout) findViewById(R.id.menu_score_layout);
        inner_layout = (RelativeLayout) findViewById(R.id.menu_inner_layout);

        start_button = (Button) findViewById(R.id.menu_start_button);

        setting = (ImageView) findViewById(R.id.menu_setting_imageView);
        help = (ImageView) findViewById(R.id.menu_help_imageView);
        amar = (ImageView) findViewById(R.id.menu_amar_imageView);

        high_Score_text = (TextView) findViewById(R.id.menu_high_Score_text);
        high_Score_Score = (TextView) findViewById(R.id.menu_high_Score_Score);

        sharedPreferences = getApplicationContext().getSharedPreferences("data", MODE_PRIVATE);
        readyGraphics();

    }

    //TODO:NOT WORKING
    private void tabligh() {
        Adad.enableBannerAds();
        InterstitialAdListener mInterstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onAdLoaded() {
                // این بخش زمانی که تبلیغ در برنامه شما بارگزاری می‌شود فراخوانی خواهد شد
            }

            @Override
            public void onAdFailedToLoad() {
                // این بخش زمانی که مشکلی در بارگزاری تبلیغ وجود داشته باشد فراخوانی خواهد شد. به عنوان نمونه قطع شدن اینترنت و یا عدم وجود تبلیغ متناسب با برنامه شما در آن لحظه در سرور عدد
                Toast.makeText(getApplicationContext(),"HIIII",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onInterstitialAdDisplayed() {
                // این بخش زمانی که تبلیغ به حالت نمایش داده شدن درمی‌آید فراخوانی خواهد شد
            }

            @Override
            public void onMessageReceive(JSONObject message) {
            }

            @Override
            public void onRemoveAdsRequested() {
                // این بخش در صورتی که شما در برنامه خود امکان حذف تبلیغات را به کاربر داده باشید و کاربر آن گزینه را انتخاب کند فراخوانی خواهد شد. می‌توانید در این بخش کاربر را به صفحه فروشگاه برنامه خود هدایت کنید
            }

            @Override
            public void onInterstitialClosed() {
                // این بخش زمانی که تبلیغ تمام صفحه بسته شود فراخوانی خواهد شد
            }

        };

        Adad.prepareInterstitialAd(mInterstitialAdListener);
        Adad.showInterstitialAd(this);
    }

    private void readyGraphics() {
        //Get Screen Size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        ScreenH = displayMetrics.heightPixels;
        ScreenW = displayMetrics.widthPixels;

        //Calculate Brick Sizes
        int brickW = (ScreenW / 6);
        int margin_left_right = (ScreenW - 6 * brickW) / 2;
        if (margin_left_right < 20) {
            brickW = (ScreenW - 20) / 6;
            margin_left_right = (ScreenW - 6 * brickW) / 2;
        }
        float brickY = (float) (brickW / 1.6);

        //Calculate inner layout size and Set Size
        inner_layout_width = (int) brickW * 6;
        inner_layout_height = (int) brickY * 9;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(inner_layout_width, inner_layout_height);
        inner_layout.setLayoutParams(params);

        //Set Location of inner Layout
        inner_layout.setX(margin_left_right);
        inner_layout.setY((float) ((ScreenH - brickY * 9) / 2.6 * 1.6));

        //Set Setting of Start_btn
        //Calculate the Size of Start Button
        int start_btn_width = inner_layout_width - 70;
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(start_btn_width, start_btn_width / 8);
        start_button.setLayoutParams(params2);
        start_button.setX(35);
        start_button.setY(inner_layout_height - start_btn_width / 10 - ScreenH / 7);
        start_button.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Main.class);
            startActivity(intent);
        });

        //Set Size of ImageView
        RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(inner_layout_width / 10, inner_layout_width / 10);
        setting.setLayoutParams(params3);
        help.setLayoutParams(params3);
        amar.setLayoutParams(params3);

        //help imageView
        help.setY(inner_layout_height - inner_layout_height / 6);
        help.setX(35);
        help.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Help.class);
            startActivity(intent);
        });
        //amar imageView
        amar.setY(inner_layout_height - inner_layout_height / 6);
        amar.setX(35 + inner_layout_width / 10 + 10);
        amar.setOnClickListener(v ->
                CustomToast("این ویژگی در نسخه ی بعدی بازی فعال خواهد شد"));

        //setting imageView
        setting.setY(inner_layout_height - inner_layout_height / 6);
        setting.setX(inner_layout_width - 35 - inner_layout_width / 10 - 5);
        setting.setOnClickListener(v ->
                CustomToast("این ویژگی در نسخه ی بعدی بازی فعال خواهد شد"));

        //Set Location and Setting of textView
        high_Score_text.setText(R.string.high_Score);
        high_Score_Score.setText(sharedPreferences.getInt("score", 0) + "");
        high_Score_Score.setTextSize(Splash.dpFromPx(getApplicationContext(), ScreenW / 30));
        high_Score_text.setTextSize(Splash.dpFromPx(getApplicationContext(), ScreenW / 30));

        //Score Layout
        score_layout.setX(40);
        score_layout.setY(inner_layout_height - start_btn_width / 10 - ScreenH / 7 - 50);
        RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(inner_layout_width - 80, 40);
        score_layout.setLayoutParams(params4);

    }

    @Override
    protected void onResume() {
        super.onResume();
        high_Score_Score.setText(sharedPreferences.getInt("score", 0) + "");


    }

    protected void CustomToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.customtoast));

        TextView textView = (TextView) layout.findViewById(R.id.custom_toast_text);
        textView.setText(message);


        Toast t = new Toast(getApplicationContext());
        t.setView(layout);
        t.setDuration(Toast.LENGTH_SHORT);
        t.setGravity(Gravity.BOTTOM | Gravity.RIGHT, 100, 100);
        t.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // SimpleDialog simpleDialog=new SimpleDialog();
        // simpleDialog.setCancelable(true);
        // simpleDialog.show(getFragmentManager(),"CustomDialogFragment");
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}