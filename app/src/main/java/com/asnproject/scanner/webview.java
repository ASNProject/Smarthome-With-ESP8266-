package com.asnproject.scanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class webview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        WebView web = (WebView)findViewById(R.id.webview);
        web.getSettings().setJavaScriptEnabled(true);
        web.setWebViewClient(new  MyBrowser());
        web.loadUrl("http://192.168.4.1/");
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView web, String url){
            web.loadUrl(url);
            return true;
        }
    }
}
