# ADACTS BANNER-ADS ANDROID-SDK INTEGRATION GUIDE

Android Library for Banner Ads Integration in Android

## STEP-1 :  Add `INTERNET` and `ACCESS_NETWORK_STATE` permissions and  `android:largeHeap="true"` in `application` tag of `AndroidManifest.xml` 
------------------------------------------------------------------------------------------------------------------------------
   ```xml
   <?xml version="1.0" encoding="utf-8"?>
   
   <manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.app">
    
         <uses-permission android:name="android.permission.INTERNET" />
         <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    
    <application
       ...
        android:largeHeap="true"
       ... >

    </application>
    
  </manifest>
   ```
   
## STEP-2 : Add this BannerAdView in your activity's layout XML file
---------------------------------------------------------------------
```xml
 <com.adacts.sdk_banner.ads.BannerAdView
        android:id="@+id/bannerAd"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:adspace_id="YOUR_ADSPACE_ID"  
        app:ad_size="BANNER"/>
```

## STEP-3: In Your Activity
---------------------------------------------------------------------
       1. Initialize BannerAdView , 
       2. Initialize ADACTS_SDK
       3. findView By ID For BannerAdView
       4. Call Load Ad Method On Activity's onResume Method.

```java
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
```



   


