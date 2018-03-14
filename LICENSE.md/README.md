# ADACTS BANNER-ADS ANDROID-SDK INTEGRATION GUIDE

Android Library for Banner Ads Integration in Android

1. add following two permissions in `AndroidManifest.xml`

   ```
   <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
   ```
   
2.  add `android:largeHeap="true"` in `application` tag of `AndroidManifest.xml` 

   ```
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
   
3. Add this BannerAdView in your activity's layout XML file

```
 <com.adacts.sdk_banner.ads.BannerAdView
        android:id="@+id/bannerAd"
        android:layout_alignParentTop="true"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:adspace_id="YOUR_ADSPACE_ID"  
        app:ad_size="BANNER"/>
```





   


