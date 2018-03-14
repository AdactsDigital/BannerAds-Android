package com.adacts.sdk_banner.helpers;

import java.security.MessageDigest;
import java.util.Locale;

/**
 * Created by saurabhmistry on 26/02/18.
 */

public class GetSHAkey {

    public static String sha1(String string) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] bytes = string.getBytes("UTF-8");
            digest.update(bytes, 0, bytes.length);
            bytes = digest.digest();

            for (final byte b : bytes) {stringBuilder.append(String.format("%02X", b));}
            return stringBuilder.toString().toLowerCase(Locale.US);
        } catch (Exception e) {
            return "NOT_FOUND";
        }
    }

}
