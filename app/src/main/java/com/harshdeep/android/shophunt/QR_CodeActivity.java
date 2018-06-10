package com.harshdeep.android.shophunt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class QR_CodeActivity extends AppCompatActivity {

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr__code);
    }
}
