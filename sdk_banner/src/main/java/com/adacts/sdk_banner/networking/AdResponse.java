package com.adacts.sdk_banner.networking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by saurabhmistry on 28/02/18.
 */

public final class AdResponse {

    @SerializedName("AD_TYPE")
    @Expose
    private String AD_TYPE;

    @SerializedName("REFRESH_TIME")
    @Expose
    private int REFRESH_TIME;


    @SerializedName("AUCTION_ID")
    @Expose
    private String AUCTION_ID;


    @SerializedName("NETWORK_TYPE")
    @Expose
    private String NETWORK_TYPE;


    @SerializedName("CLICK_URL")
    @Expose
    private String CLICK_URL;

    @SerializedName("IMPRESSION_URL")
    @Expose
    private String IMPRESSION_URL;

    @SerializedName("CREATIVE_URL")
    @Expose
    private String CREATIVE_URL;


    @SerializedName("WIDTH")
    @Expose
    private int WIDTH;

    @SerializedName("HEIGHT")
    @Expose
    private int HEIGHT;

    public String getAD_TYPE() {return AD_TYPE;}

    public int getREFRESH_TIME() {return REFRESH_TIME;}

    public String getAUCTION_ID() {return AUCTION_ID;}

    public String getNETWORK_TYPE() {return NETWORK_TYPE;}

    public String getCLICK_URL() {return CLICK_URL;}

    public String getIMPRESSION_URL() {return IMPRESSION_URL;}

    public String getCREATIVE_URL() {return CREATIVE_URL;}

    public int getWIDTH() {return WIDTH;}

    public int getHEIGHT() {return HEIGHT;}

}
