# BANNERADS INTEGRATION GUIDE

Android Library for Banner Ads Integration in Android

### STEP-1 : Add this dependency in your `build.gradle (Module:app)` file.
------------------------------------------------------------------------------------------------------------------------------


```
dependencies {
 ...
 implementation 'com.github.AdactsDigital:BannerAds-Android:v2.4'
 }
```

### STEP-2 : Add this to your `build.gradle (Project:YourAppName)` file.
------------------------------------------------------------------------------------------------------------------------------

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

### STEP-3 :  Add `android:largeHeap="true"` in `application` tag of `AndroidManifest.xml` 
------------------------------------------------------------------------------------------------------------------------------

   ```xml
   <?xml version="1.0" encoding="utf-8"?>
   
   <manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.app">
      
    <application
       ...
        android:largeHeap="true"
       ... >

    </application>
    
  </manifest>
   ```
   
### STEP-4 : Add this BannerAdView in your activity's layout XML file
---------------------------------------------------------------------
```xml
 <com.adacts.sdk_banner.ads.BannerAdView
        android:id="@+id/bannerAd"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:adspace_id="YOUR_ADSPACE_ID"  
        app:ad_size="BANNER"/>
```

### STEP-5: In Your Activity
---------------------------------------------------------------------

 1. Create BannerAdView Object, 
 2. Initialize ADACTS_SDK,
 3. findView By ID For BannerAdView Object,
 4. Call LoadAd Method On Activity's onResume Method.


```java
    public class MainActivity extends AppCompatActivity {

    BannerAdView bannerAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity
        
        
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
