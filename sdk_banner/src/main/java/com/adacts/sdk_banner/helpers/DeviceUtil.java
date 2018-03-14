package com.adacts.sdk_banner.helpers;

import android.os.Build;

/**
 * Created by saurabhmistry on 05/03/18.
 */

public class DeviceUtil {

    public static boolean isEmulator() {
        String buildDetails = (Build.FINGERPRINT + Build.DEVICE + Build.MODEL + Build.BRAND + Build.PRODUCT + Build.MANUFACTURER + Build.HARDWARE).toLowerCase();

        if (buildDetails.contains("generic") || buildDetails.contains("unknown") || buildDetails.contains("emulator") || buildDetails.contains("sdk") || buildDetails.contains("genymotion") || buildDetails.contains("x86") || buildDetails.contains("goldfish")|| buildDetails.contains("test-keys")){
            return true;
        }else{
            return false;
        }
    }
}
