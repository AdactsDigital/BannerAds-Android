package com.adacts.sdk_banner.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.provider.Settings;

import com.adacts.sdk_banner.ADACTS_SDK;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;
import java.util.HashMap;


/**
 * Created by saurabhmistry on 26/02/18.
 */

public class GetGAID extends AsyncTask<Void, Void, HashMap<String, String>> {

    String deviceId="NOT_FOUND";
    String GAID="NOT_FOUND";

    @SuppressLint("HardwareIds")
    @Override
    protected HashMap<String, String> doInBackground(Void... voids) {
            HashMap<String,String> ids  = new HashMap<String, String>();
            SharedPreferences sharedPreferences= ADACTS_SDK.mContext.getSharedPreferences("ADACTS_SDK", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();

            deviceId = Settings.Secure.getString(ADACTS_SDK.mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
            if(deviceId == null){
                deviceId = GetSHAkey.sha1(deviceId);
            }
              AdvertisingIdClient.Info idInfo = null;
            try {
                idInfo = AdvertisingIdClient.getAdvertisingIdInfo(ADACTS_SDK.mContext);
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try{
                GAID = idInfo.getId();
            }catch (Exception e){
                GAID = "NOT_FOUND";
                e.printStackTrace();
            }
            ids.put("deviceid", ""+deviceId);
            ids.put("gaid",""+GAID);
            editor.putString("did",deviceId);
            editor.putString("gid",GAID);
            editor.apply();
            return ids;
        }
}
