package com.adacts.sdk_banner.ads;

/**
 * Created by saurabhmistry on 26/02/18.
 */

public interface BannerAdListener {

    public void onBannerError(Exception paramException);

    public abstract void onBannerLoaded();

    public abstract void onBannerFinished();

    public abstract void onAdImpression();

    public abstract void onBannerClicked();

    public abstract void onAdLeftApplication();

    public abstract void onNoFill();

    public abstract void onBannerVisible();

    public abstract void onBannerHidden();

}
