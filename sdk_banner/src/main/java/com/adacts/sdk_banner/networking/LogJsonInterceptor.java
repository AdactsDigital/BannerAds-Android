package com.adacts.sdk_banner.networking;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by saurabhmistry on 13/03/18.
 */

public class LogJsonInterceptor implements Interceptor {
    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        String rawJson = response.body().string();

        try {
            Object object = new JSONTokener(rawJson).nextValue();
            String jsonLog = object instanceof JSONObject ? ((JSONObject) object).toString(4) : ((JSONArray) object).toString(4);
            Log.d("jsonLog", jsonLog);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Re-create the response before returning it because body can be read only once
        return response.newBuilder().body(ResponseBody.create(response.body().contentType(), rawJson)).build();
    }
}