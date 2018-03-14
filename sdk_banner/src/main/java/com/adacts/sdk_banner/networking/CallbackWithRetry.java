package com.adacts.sdk_banner.networking;

import android.support.annotation.NonNull;
import android.util.Log;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by saurabhmistry on 01/03/18.
 */

public abstract class CallbackWithRetry<T> implements Callback<T>{
    private static final int TOTAL_RETRIES = 3;
     private final Call<T> call;
    private int retryCount = 0;

    protected CallbackWithRetry(Call<T> call) {
        this.call = call;
    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t){
        if (retryCount++ < TOTAL_RETRIES) {
            Log.d("SDK_LOG","RETRY COUNT ="+retryCount);
            Log.v("SDK_LOG", "RETRYING REQUEST... (" + retryCount + " out of " + TOTAL_RETRIES + ")");
            retry();
        }
    }

    private void retry() {
        call.clone().enqueue(this);
    }
}
