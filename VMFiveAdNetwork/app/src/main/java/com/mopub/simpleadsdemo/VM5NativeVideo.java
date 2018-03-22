package com.mopub.simpleadsdemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.core.adnsdk.AdDelegator;
import com.core.adnsdk.AdListener;
import com.core.adnsdk.AdObject;
import com.core.adnsdk.AdProfile;
import com.core.adnsdk.AdViewType;
import com.core.adnsdk.AuthList;
import com.core.adnsdk.AuthListBuilder;
import com.core.adnsdk.ErrorMessage;
import com.core.adnsdk.KeywordList;
import com.core.adnsdk.KeywordListBuilder;
import com.mopub.common.DataKeys;
import com.mopub.nativeads.MoPubCustomEventVideoNative;
import com.mopub.nativeads.NativeErrorCode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private Context mActivity;
    private CustomEventNativeListener mCustomEventNativeListener;

    private Map<String, Object> mLocalExtras;
    private Map<String, String> mServerExtras;

    @Override
    protected void loadNativeAd(@NonNull Context context,
                                @NonNull CustomEventNativeListener customEventNativeListener,
                                @NonNull Map<String, Object> localExtras,
                                @NonNull Map<String, String> serverExtras) {
        Log.d(TAG, "Request native Ad");

        mActivity = context;
        mCustomEventNativeListener = customEventNativeListener;
        mLocalExtras = localExtras;
        mServerExtras = serverExtras;

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
        KeywordList keywordList = null;
        KeywordListBuilder keywordListBuilder = null;
        if (serverExtras.containsKey("keywords")) {
            try {
                String arrayStr = serverExtras.get("keywords");
                if (!TextUtils.isEmpty(arrayStr)) {
                     keywordListBuilder = new KeywordListBuilder();

                    if (arrayStr.startsWith("[")) {
                        Pattern p = Pattern.compile("\".*?\"");
                        Matcher matcher = p.matcher(arrayStr);
                        while (matcher.find()) {
                            Log.i(TAG, "keywords =>: " + matcher.group(0));
                            keywordListBuilder.add(matcher.group(0).replace("\"",""));
                        }
                    } else {
                        keywordListBuilder.add(arrayStr);
                    }
                    keywordList = keywordListBuilder.build();
                }
                Log.i(TAG, "keywords count: " + ((keywordList != null)? keywordList.getCount(): 0));
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Log.i(TAG, "keywords, not seen");
        }
        AuthList mAuthList = new AuthListBuilder()
                .add(apiKey, placementName, "default", 1).build();

        AdProfile mAdProfile = new AdProfile.AdProfileBuilder()
                .setAuthList(mAuthList)
                .setKeywordList(keywordList)
                .setTestMode(isTestMode).build();

        AdDelegator adDelegator = new AdDelegator(context, mAdProfile, AdViewType.CARD_VIDEO);
        adDelegator.setAdListener(this);
        mAdDelegator = adDelegator;
        adDelegator.loadAd();
    }

    @Override
    public void onAdLoaded(AdObject adObject) {
        Map<String, Object> myLocalExtras = new HashMap<>();
        JSONObject json = new JSONObject();
        myLocalExtras.put(DataKeys.JSON_BODY_KEY, json);
        myLocalExtras.put(DataKeys.CLICK_TRACKING_URL_KEY, mLocalExtras.get(DataKeys.CLICK_TRACKING_URL_KEY));
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
            json.put("uniqueID", adObject.getUUID());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Map<String, String> myServerExtras = new HashMap<>();
        myServerExtras.put(DataKeys.CLICKTHROUGH_URL_KEY, mServerExtras.get(DataKeys.CLICK_TRACKING_URL_KEY));
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
        return false;
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
