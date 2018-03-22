package com.mopub.simpleadsdemo;

import android.content.Context;
import android.util.Log;

import com.core.adnsdk.AdInterstitial;
import com.core.adnsdk.AdInterstitialType;
import com.core.adnsdk.AdListener;
import com.core.adnsdk.AdObject;
import com.core.adnsdk.ErrorMessage;
import com.mopub.mobileads.CustomEventInterstitial;
import com.mopub.mobileads.MoPubErrorCode;

import java.util.Map;

/**
 * Created by ChanYiChih on 2016/4/22.
 */
public class VM5Interstitial extends CustomEventInterstitial implements AdListener {
    private static final String TAG = "VM5Interstitial";

    private AdInterstitial mAdInterstitial;
    private CustomEventInterstitialListener mInterstitialListener;

    @Override
    protected void loadInterstitial(final Context context,
                                    final CustomEventInterstitialListener customEventInterstitialListener,
                                    final Map<String, Object> localExtras,
                                    final Map<String, String> serverExtras) {
        mInterstitialListener = customEventInterstitialListener;

        Log.d(TAG, "Request AdInterstitial Ad");

        String apiKey = "";
        if (serverExtras.containsKey("apiKey")) {
            apiKey = serverExtras.get("apiKey");
        } else {
            Log.e(TAG, "User should specify apiKey in serverExtras");
            mInterstitialListener.onInterstitialFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        }

        String placementName = "";
        if (serverExtras.containsKey("placement")) {
            placementName = serverExtras.get("placement");
        } else {
            Log.e(TAG, "User should specify placement in serverExtras");
            mInterstitialListener.onInterstitialFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        }

        boolean isTestMode = true;
        if (serverExtras.containsKey("test")) {
            isTestMode = serverExtras.get("test").equals("1");
        } else {
            Log.e(TAG, "User should specify test in serverExtras");
            mInterstitialListener.onInterstitialFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        }

        mAdInterstitial = new AdInterstitial(context, apiKey, placementName, AdInterstitialType.INTERSTITIAL_VIDEO);
        mAdInterstitial.setTestMode(isTestMode);
        mAdInterstitial.setAdListener(this);
        mAdInterstitial.loadAd();
    }

    @Override
    protected void showInterstitial() {
        mAdInterstitial.showAd();
    }

    @Override
    protected void onInvalidate() {
        onDestroy();
    }

    @Override
    public void onAdLoaded(AdObject adObject) {
        Log.d(TAG, "Interstitial ad loaded successfully.");
        mInterstitialListener.onInterstitialLoaded();
    }

    @Override
    public void onError(ErrorMessage errorMessage) {
        Log.w(TAG, "onError : " + errorMessage);
        switch (errorMessage) {
            case GENERIC:
                mInterstitialListener.onInterstitialFailed(MoPubErrorCode.UNSPECIFIED);
                break;
            case NOADS:
                mInterstitialListener.onInterstitialFailed(MoPubErrorCode.VIDEO_NOT_AVAILABLE);
                break;
            case RESOURCES_DOWNLOAD_FAIL:
                mInterstitialListener.onInterstitialFailed(MoPubErrorCode.NETWORK_INVALID_STATE);
                break;
            case NOTVISIBLE:
                mInterstitialListener.onInterstitialFailed(MoPubErrorCode.NETWORK_NO_FILL);
                break;
            default:
                mInterstitialListener.onInterstitialFailed(MoPubErrorCode.NETWORK_INVALID_STATE);
        }
    }

    @Override
    public void onAdClicked() {
        Log.d("MoPub", "Interstitial ad clicked.");
        mInterstitialListener.onInterstitialClicked();
    }

    @Override
    public void onAdFinished() {
    }

    @Override
    public void onAdReleased() {
        Log.d(TAG, "Interstitial ad dismissed.");
        mInterstitialListener.onInterstitialDismissed();
        onDestroy();
    }

    @Override
    public boolean onAdWatched() {
        return false;
    }

    @Override
    public void onAdImpressed() {

    }

    public void onResume() {
        if (mAdInterstitial != null) {
            mAdInterstitial.onResume();
        }
    }

    public void onPause() {
        if (mAdInterstitial != null) {
            mAdInterstitial.onPause();
        }
    }

    public void onDestroy() {
        if (mAdInterstitial != null) {
            mAdInterstitial.onDestroy();
            mAdInterstitial = null;
        }
    }
}
