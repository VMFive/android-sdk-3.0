package com.core.adnsdkdemo;

import android.content.Context;

import com.core.adnsdk.AdInterstitial;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
//import com.google.android.gms.ads.purchase.InAppPurchaseListener;
//import com.google.android.gms.ads.purchase.PlayStorePurchaseListener;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

/**
 * Created by ChanYiChih on 2016/8/4.
 */
public class InterstitialAdView {
    private static final String TAG = "InterstitialAdView";

    // if user has multiple instances of interstitial, you need to change this constant
    private static final int DEFAULT_INTERSTITIAL_INSTANCES = 1;

    private InterstitialAd mInterstitialAd;
    private int mInstances;

    InterstitialAdView(Context context) {
        mInterstitialAd = new InterstitialAd(context);
        mInstances = DEFAULT_INTERSTITIAL_INSTANCES;
    }

    public AdListener getAdListener() {
        return mInterstitialAd.getAdListener();
    }

    public String getAdUnitId() {
        return mInterstitialAd.getAdUnitId();
    }

//    public InAppPurchaseListener getInAppPurchaseListener() {
//        return mInterstitialAd.getInAppPurchaseListener();
//    }

    public boolean isLoaded() {
        return mInterstitialAd.isLoaded();
    }

    public boolean isLoading() {
        return mInterstitialAd.isLoading();
    }

    public void loadAd(AdRequest adRequest) {
        // must call AdInterstitial.pruneAdInterstitials(mInstances) before loadAd()
        AdInterstitial.pruneAdInterstitials(mInstances);
        mInterstitialAd.loadAd(adRequest);
    }

    public void setAdListener(AdListener adListener) {
        mInterstitialAd.setAdListener(adListener);
    }

    public void setAdUnitId(String adUnitId) {
        mInterstitialAd.setAdUnitId(adUnitId);
    }

//    public void setInAppPurchaseListener(InAppPurchaseListener inAppPurchaseListener) {
//        mInterstitialAd.setInAppPurchaseListener(inAppPurchaseListener);
//    }
//
//    public void setPlayStorePurchaseParams(PlayStorePurchaseListener playStorePurchaseListener, String publicKey) {
//        mInterstitialAd.setPlayStorePurchaseParams(playStorePurchaseListener, publicKey);
//    }

    public String getMediationAdapterClassName() {
        return mInterstitialAd.getMediationAdapterClassName();
    }

    public void show() {
        mInterstitialAd.show();
    }

    public void setRewardedVideoAdListener(RewardedVideoAdListener rewardedVideoAdListener) {
        mInterstitialAd.setRewardedVideoAdListener(rewardedVideoAdListener);
    }

    public void setInstances(int instances) {
        mInstances = instances;
    }

    public void destory() {
        // clean all interstitial instances
        AdInterstitial.pruneAdInterstitials(0);
    }
}
