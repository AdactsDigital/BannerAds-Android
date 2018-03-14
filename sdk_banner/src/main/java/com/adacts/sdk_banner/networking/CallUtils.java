package com.adacts.sdk_banner.networking;

import android.support.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by saurabhmistry on 01/03/18.
 */

public class CallUtils {
    public static <T> void enqueueWithRetry(Call<T> call, final Callback<T> callback){
        call.enqueue(new CallbackWithRetry<T>(call) {
            @Override
            public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                callback.onResponse(call,response);
            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                callback.onFailure(call,t);
            }
        });
    }

}
