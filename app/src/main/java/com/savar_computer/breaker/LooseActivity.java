package com.savar_computer.breaker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class LooseActivity extends Activity {

    private TextView textView;
    private Button yes,no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loose_activity);

        textView=(TextView)findViewById(R.id.loose_textview);
        no=(Button)findViewById(R.id.loose_no);
        yes=(Button)findViewById(R.id.loose_yes);

        Typeface typeface=Typeface.createFromAsset(getAssets(),"Fonts/font.ttf");
        textView.setTypeface(typeface);
        yes.setTypeface(typeface);
        no.setTypeface(typeface);

        yes.setOnClickListener(v -> {
            Intent intent=new Intent(getApplicationContext(),Main.class);
            startActivity(intent);
            finish();
        });

        no.setOnClickListener(v -> {
            Intent intent=new Intent(getApplicationContext(),Menu.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        //this.finish();
    }
}
