package com.adacts.sdk_banner.ads;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.adacts.sdk_banner.ADACTS_SDK;
import com.adacts.sdk_banner.R;
import com.adacts.sdk_banner.networking.AdRequest;
import com.adacts.sdk_banner.networking.AdResponse;
import com.adacts.sdk_banner.networking.CallUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by saurabhmistry on 22/02/18.
 */

public class BannerAdView extends android.support.v7.widget.AppCompatImageView implements  View.OnClickListener,BannerAdListener{
    String ADSPACE_ID;
    private OnClickListener clickListener;
    final BannerAdView bannerAdView=this;
    AdRequest mAdRequest;
    Handler handler;

    private int DEFAULT_BANNER_SIZE=0;
    private int BANNER_SIZE;
    private int BANNER_HEIGHT;
    private int BANNER_WIDTH;

    private int SCREEN_WIDTH;
    private int SCREEN_HEIGHT;

    private int BANNER_H=50;
    private int BANNER_W=320;

    private JSONObject banner;
    private JSONObject ids;
    private boolean bannerIsVisible;

    private String CREATIVE_URL;
    private String AUCTION_ID;
    private String CLICK_URL;
    private int REFRESH_TIME=60;

    public BannerAdView(Context context) {
        super(context);
        setOnClickListener(this);
    }

    public BannerAdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BannerAdView);
        BANNER_SIZE= typedArray.getInt(R.styleable.BannerAdView_ad_size,DEFAULT_BANNER_SIZE);
        ADSPACE_ID=typedArray.getString(R.styleable.BannerAdView_adspace_id);
        typedArray.recycle();
        setOnClickListener(this);
    }

    public BannerAdView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnClickListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final float scale = getResources().getDisplayMetrics().density;
        switch (BANNER_SIZE) {
            case 0:
                //BANNER
                BANNER_WIDTH=320;
                BANNER_HEIGHT=50;
                break;
            case 1:
                //LARGE_BANNER
                BANNER_WIDTH=320;
                BANNER_HEIGHT=100;
            break;
            case 2:
                //MEDIUM_RECTANGLE
                BANNER_WIDTH=300;
                BANNER_HEIGHT=250;
                break;
            case 3:
                //FULL_BANNER
                BANNER_WIDTH=468;
                BANNER_HEIGHT=60;
            case 4:
                //LEADERBOARD
                BANNER_WIDTH=728;
                BANNER_HEIGHT=90;
            break;
        }
        BANNER_W= (int) (scale*BANNER_WIDTH);
        BANNER_H=(int) (scale*BANNER_HEIGHT);
        SCREEN_WIDTH= MeasureSpec.getSize(widthMeasureSpec);
        SCREEN_HEIGHT= MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(SCREEN_WIDTH,BANNER_H);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onVisibilityChanged (@NonNull View view, int visibility) {
        super.onVisibilityChanged(view, visibility);
        try {
            switch (visibility) {
                case VISIBLE:
                    bannerIsVisible=true;
                    //bannerAdView.onBannerVisible();
                    break;
                case INVISIBLE:
                case GONE:
                    bannerIsVisible=false;
                    bannerAdView.onBannerHidden();
                    break;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setAdUnitID(String adUnitID){ADSPACE_ID=adUnitID;}

    public void loadAd(){
        handler = new Handler();
        handler.postDelayed(new Runnable() {
           @Override
           public void run() {
               bannerAdView.onBannerVisible();
            }
        }, 50);
    }

        private void makeAdRequest(){
        //Log.d("SDK_LOG","REQUEST FOR NEW BANNER");
        mAdRequest=new AdRequest();
        if (ADACTS_SDK.mContext!=null){
            SharedPreferences sharedPreferences= ADACTS_SDK.mContext.getSharedPreferences("ADACTS_SDK",Context.MODE_PRIVATE);
                String GAID=sharedPreferences.getString("gid","NOT_FOUND");
                String DEVICEID=sharedPreferences.getString("did","NOT_FOUND");
                try {
                    banner = new JSONObject();
                    banner.put("height",""+BANNER_HEIGHT);
                    banner.put("width",""+BANNER_WIDTH);
                    banner.put("type","banner");

                    ids=new JSONObject();
                    ids.put("deviceid",DEVICEID);
                    ids.put("gaid",GAID);
                }catch (Exception e){
                    e.printStackTrace();
                }

            CallUtils.enqueueWithRetry(ApiInterface.ApiClient.getApiInterface().CallBannerApi(ADSPACE_ID,mAdRequest.getBuildInfo(),mAdRequest.getPublisherAppInfo(), ids, mAdRequest.getNetworkInfo(), banner), new Callback<AdResponse>() {
                @Override
                public void onResponse(@NonNull Call<AdResponse> call, @NonNull Response<AdResponse> response) {
                    //Log.d("SDK_LOG","RESPONSE CODE ="+response.code());
                    if (response.code()==200 && response.isSuccessful() && response.body()!=null){
                        try {
                            CREATIVE_URL=response.body().getCREATIVE_URL();
                            CLICK_URL=response.body().getCLICK_URL();
                            AUCTION_ID=response.body().getAUCTION_ID();
                            REFRESH_TIME=response.body().getREFRESH_TIME();
                            Glide.with(ADACTS_SDK.mContext)
                                    .asGif()
                                    .load(CREATIVE_URL)
                                    .listener(new RequestListener<GifDrawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                                            bannerAdView.onBannerError(e);
                                            return false;
                                        }
                                        @Override
                                        public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                                            bannerAdView.onBannerLoaded();
                                            return false;
                                        }
                                    })
                                    .into(bannerAdView);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        bannerAdView.onNoFill();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AdResponse> call, @NonNull Throwable t) {
                   bannerAdView.onBannerVisible();
                }
            });
        }
    }

    private void scheduleNextRequest(int refreshTime){
         int RefreshTimeInMillies=refreshTime*1000;
         handler = new Handler();
         handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bannerAdView.onBannerVisible();
            }
        }, RefreshTimeInMillies);
      }


    @Override
    public void setOnClickListener(OnClickListener l) {
        if (l == this) {
            super.setOnClickListener(l);
        } else {
            clickListener = l;
        }
    }

    @Override
    public void onBannerError(Exception paramException) {
        Log.d("SDK_LOG","ERROR LOADING BANNER AD");
        scheduleNextRequest(REFRESH_TIME);
    }

    @Override
    public void onBannerLoaded() {
        scheduleNextRequest(REFRESH_TIME);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                bannerAdView.onAdImpression();
            }
        }, 3000);
    }

    @Override
    public void onBannerFinished() {
        makeAdRequest();
    }

    @Override
    public void onAdImpression() {
        callImressionTracker(AUCTION_ID);
    }

    private void callImressionTracker(String AuctionId){
        ApiInterface.ApiClient.getImpressionTrackerApiInterface().CallImpressionTracker(AuctionId).enqueue(new Callback<AdResponse>() {
            @Override
            public void onResponse(Call<AdResponse> call, Response<AdResponse> response) {
            }
            @Override
            public void onFailure(Call<AdResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBannerClicked() {
        if (CLICK_URL!=null){
            try {
                Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(CLICK_URL));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                List<ResolveInfo> mainLauncherList = ADACTS_SDK.mContext.getPackageManager().queryIntentActivities(intent, 0);
                if (!mainLauncherList.isEmpty() && mainLauncherList.size()>0){
                    intent.setPackage(mainLauncherList.get(0).activityInfo.packageName);
                    intent.setClassName(mainLauncherList.get(0).activityInfo.packageName,mainLauncherList.get(0).activityInfo.name);
                    ADACTS_SDK.mContext.startActivity(intent);
                }else{
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(CLICK_URL));
                    ADACTS_SDK.mContext.startActivity(browserIntent);
                }
                this.onAdLeftApplication();
            }catch (Exception e){
                e.printStackTrace();
                bannerAdView.onBannerError(e);
            }
        }
    }

    @Override
    public void onAdLeftApplication() {
    }

    @Override
    public void onNoFill() {
        Log.d("SDK_LOG","NO FILL");
        scheduleNextRequest(REFRESH_TIME);
    }

    @Override
    public void onBannerVisible() {
       if (bannerIsVisible){
           makeAdRequest();
       }
    }

    @Override
    public void onBannerHidden() {
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onClick(View view) {
        this.onBannerClicked();
    }

    private interface ApiInterface {

        public static final String ADREQUEST_URL ="http://tr.smmpto.xyz/";
        public static final String IMPRESSION_URL ="http://it.smmpto.xyz/";

        @FormUrlEncoded
        @POST("request?sdk=1")
        public Call<AdResponse> CallBannerApi(@Field("id") String AdSpaceID,
                                              @Field("device") JSONObject DEVICE,
                                              @Field("publisher_app") JSONObject PUBLISHER_APP,
                                              @Field("ids") JSONObject IDs,
                                              @Field("network") JSONObject NETWORK,
                                              @Field("banner") JSONObject BANNER);

        @GET("api/impression/{subid}/")
        public Call<AdResponse> CallImpressionTracker(@Path("subid") String AuctionId);

        public class ApiClient {

            public static ApiInterface apiInterface;
            public static ApiInterface imressionApiInterface;


            public static ApiInterface getApiInterface() {
                final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        //.addInterceptor(new LogJsonInterceptor())
                        .retryOnConnectionFailure(true)
                        .connectTimeout(20, TimeUnit.SECONDS)
                        .writeTimeout(20, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();

                if (apiInterface == null) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .client(okHttpClient)
                            .baseUrl(ADREQUEST_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    apiInterface = retrofit.create(ApiInterface.class);
                    return apiInterface;
                } else {
                    return apiInterface;
                }
            }

            public static ApiInterface getImpressionTrackerApiInterface(){
                final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .retryOnConnectionFailure(true)
                        .connectTimeout(20, TimeUnit.SECONDS)
                        .writeTimeout(20, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();

                if (imressionApiInterface == null) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .client(okHttpClient)
                            .baseUrl(IMPRESSION_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    imressionApiInterface = retrofit.create(ApiInterface.class);
                    return imressionApiInterface;
                } else {
                    return imressionApiInterface;
                }
            }
        }
    }
}
