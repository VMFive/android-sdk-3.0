package com.mopub.simpleadsdemo;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.core.adnsdk.ADN;
import com.core.adnsdk.AdDelegator;
import com.core.adnsdk.AdListener;
import com.core.adnsdk.AdObject;
import com.core.adnsdk.AdViewType;
import com.core.adnsdk.ErrorMessage;
import com.mopub.common.DataKeys;
import com.mopub.nativeads.MoPubCustomEventVideoNative;
import com.mopub.nativeads.NativeErrorCode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import static com.mopub.common.DataKeys.IMPRESSION_MIN_VISIBLE_PERCENT;
import static com.mopub.common.DataKeys.IMPRESSION_VISIBLE_MS;
import static com.mopub.common.DataKeys.MAX_BUFFER_MS;
import static com.mopub.common.DataKeys.PAUSE_VISIBLE_PERCENT;
import static com.mopub.common.DataKeys.PLAY_VISIBLE_PERCENT;

/**
 * Created by ChanYiChih on 2016/4/22.
 */
public class VM5NativeVideo extends MoPubCustomEventVideoNative implements AdListener {
    private static final String TAG = "VM5NativeVideo";

    private AdDelegator mAdDelegator;

    private Activity mActivity;
    private CustomEventNativeListener mCustomEventNativeListener;

    @Override
    protected void loadNativeAd(@NonNull Activity activity,
                                @NonNull CustomEventNativeListener customEventNativeListener,
                                @NonNull Map<String, Object> localExtras,
                                @NonNull Map<String, String> serverExtras) {
        Log.d(TAG, "Request native Ad");

        mActivity = activity;
        mCustomEventNativeListener = customEventNativeListener;

        String apiKey = "";
        if (serverExtras.containsKey("apiKey")) {
            apiKey = serverExtras.get("apiKey");
        } else {
            Log.e(TAG, "User should specify apiKey in serverExtras");
            customEventNativeListener.onNativeAdFailed(NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR);
            return;
        }

        String placementName = "";
        if (serverExtras.containsKey("placement")) {
            placementName = serverExtras.get("placement");
        } else {
            Log.e(TAG, "User should specify placement in serverExtras");
            customEventNativeListener.onNativeAdFailed(NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR);
            return;
        }

        boolean isTestMode = true;
        if (serverExtras.containsKey("test")) {
            isTestMode = serverExtras.get("test").equals("1");
        } else {
            Log.e(TAG, "User should specify test in serverExtras");
            customEventNativeListener.onNativeAdFailed(NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR);
            return;
        }

        AdDelegator adDelegator = new AdDelegator(activity, apiKey, placementName, AdViewType.BANNER_VIDEO);
        adDelegator.setTestMode(isTestMode);
        adDelegator.setAdListener(this);
        mAdDelegator = adDelegator;
        adDelegator.loadAd();
    }

    @Override
    public void onAdLoaded(AdObject adObject) {
        Map<String, Object> myLocalExtras = new HashMap<>();
        JSONObject json = new JSONObject();
        myLocalExtras.put(DataKeys.JSON_BODY_KEY, json);
        myLocalExtras.put(DataKeys.CLICK_TRACKING_URL_KEY, "clicktrackingurl");
        try {
            json.put("imptracker", new JSONArray());
            json.put("clktracker", "expected clktracker");
            if (adObject.getAdTitle() == null || adObject.getAdTitle().equals("")) {
                json.put("title", "title");
            } else {
                json.put("title", adObject.getAdTitle());
            }
            json.put("text", adObject.getAdDescription());
            if (adObject.getImageUrl() != null) {
                json.put("mainimage", adObject.getImageUrl());
            } else {
                json.remove("mainimage");
            }
            if (adObject.getIconUrl() != null) {
                json.put("iconimage", adObject.getIconUrl());
            } else {
                json.remove("iconimage");
            }
            json.put("clk", adObject.getAdTrigger().getActionUrl());
            if (adObject.getAdCtaText() == null || adObject.getAdCtaText().equals("")) {
                json.put("ctatext", "learn more");
            } else {
                json.put("ctatext", adObject.getAdCtaText());
            }
            json.put("video", adObject.getVastVideoXml());
            json.remove("extraimage");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Map<String, String> myServerExtras = new HashMap<>();
        myServerExtras.put(PLAY_VISIBLE_PERCENT, "10");
        myServerExtras.put(PAUSE_VISIBLE_PERCENT, "5");
        myServerExtras.put(IMPRESSION_MIN_VISIBLE_PERCENT, "15");
        myServerExtras.put(IMPRESSION_VISIBLE_MS, "100");
        myServerExtras.put(MAX_BUFFER_MS, "20");

        HttpURLConnection.setFollowRedirects(true);
        super.loadNativeAd(mActivity, mCustomEventNativeListener, myLocalExtras, myServerExtras);
        onDestroy();
    }

    @Override
    public void onError(ErrorMessage errorMessage) {
        Log.w(TAG, "onError : " + errorMessage);
        switch (errorMessage) {
            case GENERIC:
                mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.UNSPECIFIED);
                break;
            case NOADS:
                mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.NETWORK_NO_FILL);
                break;
            case RESOURCES_DOWNLOAD_FAIL:
                mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.NETWORK_INVALID_STATE);
                break;
            case NOTVISIBLE:
                mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.NETWORK_NO_FILL);
                break;
            default:
                mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.NETWORK_INVALID_STATE);
        }
    }

    @Override
    public void onAdClicked() {

    }

    @Override
    public void onAdFinished() {

    }

    @Override
    public void onAdReleased() {
        Log.d(TAG, "Native ad dismissed.");
        onDestroy();
    }

    @Override
    public boolean onAdWatched() {
        return true;
    }

    @Override
    public void onAdImpressed() {

    }

    public void onResume() {
        if (mAdDelegator != null) {
            mAdDelegator.onResume();
        }
    }

    public void onPause() {
        if (mAdDelegator != null) {
            mAdDelegator.onPause();
        }
    }

    public void onDestroy() {
        if (mAdDelegator != null) {
            mAdDelegator.onDestroy();
            mAdDelegator = null;
        }
    }
}
