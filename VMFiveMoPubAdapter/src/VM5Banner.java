package com.mopub.simpleadsdemo;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.core.adnsdk.ADN;
import com.core.adnsdk.AdListener;
import com.core.adnsdk.AdObject;
import com.core.adnsdk.AdView;
import com.core.adnsdk.AdViewType;
import com.core.adnsdk.ErrorMessage;
import com.mopub.mobileads.CustomEventBanner;
import com.mopub.mobileads.MoPubErrorCode;

import java.util.Map;

/**
 * Created by ChanYiChih on 2016/4/22.
 */
public class VM5Banner extends CustomEventBanner implements AdListener {
    private static final String TAG = "VM5Banner";

    private AdView mAdView;
    private CustomEventBannerListener mBannerListener;

    @Override
    protected void loadBanner(Context context, CustomEventBannerListener customEventBannerListener, Map<String, Object> localExtras, Map<String, String> serverExtras) {
        mBannerListener = customEventBannerListener;

        Log.d(TAG, "Request Banner Ad");

        String apiKey = "";
        if (serverExtras.containsKey("apiKey")) {
            apiKey = serverExtras.get("apiKey");
        } else {
            Log.e(TAG, "User should specify apiKey in serverExtras");
            mBannerListener.onBannerFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        }

        String placementName = "";
        if (serverExtras.containsKey("placement")) {
            placementName = serverExtras.get("placement");
        } else {
            Log.e(TAG, "User should specify placement in serverExtras");
            mBannerListener.onBannerFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        }

        boolean isTestMode = true;
        if (serverExtras.containsKey("test")) {
            isTestMode = serverExtras.get("test").equals("1");
        } else {
            Log.e(TAG, "User should specify test in serverExtras");
            mBannerListener.onBannerFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        }

        mAdView = new AdView((Activity)context, apiKey, placementName, AdViewType.BANNER_VIDEO);
        mAdView.setTestMode(isTestMode);
        mAdView.setAdListener(this);
        mAdView.loadAd();
    }

    @Override
    protected void onInvalidate() {
        onDestroy();
    }

    @Override
    public void onAdLoaded(AdObject adObject) {
        Log.d(TAG, "Banner ad loaded successfully.");
        mBannerListener.onBannerLoaded(mAdView);
    }

    @Override
    public void onError(ErrorMessage errorMessage) {
        Log.w(TAG, "onError : " + errorMessage);
        switch (errorMessage) {
            case GENERIC:
                mBannerListener.onBannerFailed(MoPubErrorCode.UNSPECIFIED);
                break;
            case NOADS:
                mBannerListener.onBannerFailed(MoPubErrorCode.VIDEO_NOT_AVAILABLE);
                break;
            case RESOURCES_DOWNLOAD_FAIL:
                mBannerListener.onBannerFailed(MoPubErrorCode.NETWORK_INVALID_STATE);
                break;
            case NOTVISIBLE:
                mBannerListener.onBannerFailed(MoPubErrorCode.NETWORK_NO_FILL);
                break;
            default:
                mBannerListener.onBannerFailed(MoPubErrorCode.NETWORK_INVALID_STATE);
        }
    }

    @Override
    public void onAdClicked() {
        Log.d("MoPub", "Banner ad clicked.");
        mBannerListener.onBannerClicked();
    }

    @Override
    public void onAdFinished() {
    }

    @Override
    public void onAdReleased() {
        Log.d(TAG, "Banner ad dismissed.");
    }

    @Override
    public boolean onAdWatched() {
        return true;
    }

    @Override
    public void onAdImpressed() {

    }

    public void onResume() {
        if (mAdView != null) {
            mAdView.onResume();
        }
    }

    public void onPause() {
        if (mAdView != null) {
            mAdView.onPause();
        }
    }

    public void onDestroy() {
        if (mAdView != null) {
            mAdView.onDestroy();
            mAdView = null;
        }
    }
}
