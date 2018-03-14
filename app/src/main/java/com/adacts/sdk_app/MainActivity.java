package com.adacts.sdk_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.adacts.sdk_banner.ADACTS_SDK;
import com.adacts.sdk_banner.ads.BannerAdView;

public class MainActivity extends AppCompatActivity {

    BannerAdView bannerAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ADACTS_SDK.init(MainActivity.this);
        bannerAdView=findViewById(R.id.bannerAd);
    }

    @Override
    public void onResume(){
        super.onResume();
            bannerAdView.loadAd();
    }
}
