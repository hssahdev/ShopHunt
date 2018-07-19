package com.harshdeep.android.shophunt;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class StartupActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 500;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

//        View decorView = getWindow().getDecorView();
//// Hide the status bar.
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);
// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.
        getSupportActionBar().hide();

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        TextView tx = (TextView)findViewById(R.id.startup_text);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/ShadowsIntoLight.ttf");

        tx.setTypeface(custom_font);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(StartupActivity.this,SearchActivity.class);
                startActivity(mainIntent);
                StartupActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }
}
