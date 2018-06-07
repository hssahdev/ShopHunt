package com.harshdeep.android.shophunt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class WebsiteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebView webView = new WebView(this);
        setContentView(webView);
        Intent yo = getIntent();
//        Log.v("website",yo.getStringExtra("url"));
//        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
//        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(yo.getStringExtra("url"));

    }


}
