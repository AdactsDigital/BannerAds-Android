package com.adacts.sdk_banner;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.adacts.sdk_banner.helpers.GetGAID;

import java.util.HashMap;



/**
 * Created by saurabhmistry on 22/02/18.
 */

public class ADACTS_SDK {
    public static Context mContext;
    public static final String ADACTS_SDK_LOG="ADACTS_SDK_LOG";

    private ADACTS_SDK() {}

    public static void init(Context context){
        initializeSDK(context);
    }

    private static void initializeSDK(Context context){
        if(context == null) {
            throw new IllegalArgumentException("CONTEXT CAN NOT NOT BE NULL");
        }else{
                mContext=context;
                getDeviceIds();
                Log.d(ADACTS_SDK_LOG,"ADACTS_SDK INITIALIZATION SUCCESSFUL");
        }
    }

    private static final AsyncTask<Void, Void, HashMap<String, String>> getDeviceIds(){
      return new GetGAID().execute();
    }


}
