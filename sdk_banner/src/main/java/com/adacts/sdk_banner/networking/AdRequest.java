package com.adacts.sdk_banner.networking;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.webkit.WebView;

import com.adacts.sdk_banner.ADACTS_SDK;
import com.adacts.sdk_banner.helpers.DeviceUtil;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;


import static android.net.ConnectivityManager.TYPE_ETHERNET;

/**
 * Created by saurabhmistry on 22/02/18.
 */

public final class AdRequest {

    private static JSONObject device ;
    private static JSONObject publisher_app;
    private static JSONObject network;

    public AdRequest(){}

    private static final JSONObject findBuildInfo(){
        WindowManager wm = (WindowManager) ADACTS_SDK.mContext.getSystemService(Context.WINDOW_SERVICE);
        boolean isEmulator= DeviceUtil.isEmulator();
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        try {
            device = new JSONObject();
            device.put("model", Build.MODEL);
            device.put("manufacturer", Build.MANUFACTURER);
            device.put("brand", Build.BRAND);
            device.put("version_release", Build.VERSION.RELEASE);
            device.put("version_sdk", "" + Build.VERSION.SDK_INT);
            device.put("product", Build.PRODUCT);
            device.put("serial", Build.SERIAL);
            device.put("fingerprint", Build.FINGERPRINT);
            device.put("language_code", Locale.getDefault().getLanguage());
            device.put("country_code", Locale.getDefault().getCountry());
            device.put("device_height", "" + height);
            device.put("device_width", "" + width);
            device.put("is_emulator", "" + isEmulator);
        }catch (Exception e){
            e.printStackTrace();
        }
        return device;
    }

    private static final JSONObject findAppInfo(){
        ApplicationInfo applicationInfo = null;
        String mAppVersion ="1.0";
        String mAppPackageName = ADACTS_SDK.mContext.getPackageName();
        String mAppName="NOT FOUND";
        int mAppVersionCode=1;

        PackageManager packageManager = ADACTS_SDK.mContext.getPackageManager();
        try {
            applicationInfo = packageManager.getApplicationInfo(mAppPackageName, 0);
            final PackageInfo packageInfo = packageManager.getPackageInfo(mAppPackageName, 0);
            mAppVersion=packageInfo.versionName;
            mAppVersionCode=packageInfo.versionCode;
        } catch (final PackageManager.NameNotFoundException e) {
            // swallow
        }

        if (applicationInfo != null) {
            mAppName = (String) packageManager.getApplicationLabel(applicationInfo);
        }
        try {
            publisher_app = new JSONObject();
            publisher_app.put("app_package", mAppPackageName);
            publisher_app.put("app_name", mAppName);
            publisher_app.put("app_version", mAppVersion);
            publisher_app.put("app_version_code", "" + mAppVersionCode);
            publisher_app.put("app_platform", "android");
        }catch (Exception e){
            e.printStackTrace();
        }
        return publisher_app;
    }

    public final JSONObject getBuildInfo(){
        return findBuildInfo();
    }

    public final JSONObject getPublisherAppInfo(){
        return findAppInfo();
    }

    public JSONObject getNetworkInfo(){
        return findNetworkInfo();
    }

    private static final JSONObject findNetworkInfo(){
        String mNetworkOperator;
        String mSimOperator;
        String mIsoCountryCode;
        String mNetworkOperatorName;

        final TelephonyManager telephonyManager = (TelephonyManager) ADACTS_SDK.mContext.getSystemService(ADACTS_SDK.mContext.TELEPHONY_SERVICE);
        mNetworkOperator = telephonyManager.getNetworkOperator();

        if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA && telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY) {
            mSimOperator = telephonyManager.getSimOperator();
        }

        mIsoCountryCode = telephonyManager.getNetworkCountryIso();
        //mSimIsoCountryCode = telephonyManager.getSimCountryIso();

        try {
            // Some Lenovo devices require READ_PHONE_STATE here.
            mNetworkOperatorName = telephonyManager.getNetworkOperatorName();

           /* if (telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY) {
                mSimOperatorName = telephonyManager.getSimOperatorName();
            }*/
        } catch (SecurityException e) {
            mNetworkOperatorName = null;
        }

        int networkType = 10;
        ConnectivityManager connectivityManager = (ConnectivityManager) ADACTS_SDK.mContext.getSystemService(ADACTS_SDK.mContext.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            networkType = activeNetworkInfo.getType();
        }
        String networkTypeStr=getNetworkType(networkType);
        try {
            network = new JSONObject();
            network.put("ua", new WebView(ADACTS_SDK.mContext).getSettings().getUserAgentString());
            network.put("connection_type", networkTypeStr);
            network.put("operator", mNetworkOperatorName);
            network.put("country", mIsoCountryCode);
        }catch (Exception e){
            e.printStackTrace();
        }
        return network;
    }

    private static final String getLocationInfo(){
        return "";
    }

    private static String getNetworkType(int type) {
        switch(type) {
            case TYPE_ETHERNET:
                return "ETHERNET";
            case ConnectivityManager.TYPE_WIFI:
                return "WIFI";
            case ConnectivityManager.TYPE_MOBILE:
            case ConnectivityManager.TYPE_MOBILE_DUN:
            case ConnectivityManager.TYPE_MOBILE_HIPRI:
            case ConnectivityManager.TYPE_MOBILE_MMS:
            case ConnectivityManager.TYPE_MOBILE_SUPL:
                return "MOBILE";
            case ConnectivityManager.TYPE_VPN:
                return "VPN";
            case ConnectivityManager.TYPE_DUMMY:
                return "DUMMY";
            default:
                return "UNKNOWN";
        }
    }
}
