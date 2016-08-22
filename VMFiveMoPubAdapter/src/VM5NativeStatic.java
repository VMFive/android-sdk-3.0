package com.mopub.simpleadsdemo;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.core.adnsdk.ADN;
import com.core.adnsdk.AdDelegator;
import com.core.adnsdk.AdListener;
import com.core.adnsdk.AdObject;
import com.core.adnsdk.AdViewType;
import com.core.adnsdk.ErrorMessage;
import com.mopub.nativeads.CustomEventNative;
import com.mopub.nativeads.ImpressionTracker;
import com.mopub.nativeads.NativeClickHandler;
import com.mopub.nativeads.NativeErrorCode;
import com.mopub.nativeads.NativeImageHelper;
import com.mopub.nativeads.StaticNativeAd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.mopub.nativeads.NativeImageHelper.preCacheImages;

/**
 * Created by ChanYiChih on 2016/4/22.
 */
public class VM5NativeStatic extends CustomEventNative {
    private static final String TAG = "VM5Interstitial";

    private VM5StaticNativeAd mVM5StaticNativeAd;

    @Override
    protected void loadNativeAd(@NonNull Activity activity,
                                @NonNull CustomEventNativeListener customEventNativeListener,
                                @NonNull Map<String, Object> localExtras,
                                @NonNull Map<String, String> serverExtras) {
        Log.d(TAG, "Request native Ad");

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

        mVM5StaticNativeAd = new VM5StaticNativeAd(activity,
                new ImpressionTracker(activity),
                new NativeClickHandler(activity),
                customEventNativeListener);
        AdDelegator adDelegator = new AdDelegator(activity, apiKey, placementName, AdViewType.BANNER_VIDEO);
        adDelegator.setTestMode(isTestMode);
        adDelegator.setAdListener(mVM5StaticNativeAd);
        mVM5StaticNativeAd.setAdDelegator(adDelegator);
        adDelegator.loadAd();
    }

    class VM5StaticNativeAd extends StaticNativeAd implements AdListener {
        private static final int IMPRESSION_MIN_TIME_VIEWED = 0;

        private final Context mContext;
        private final CustomEventNativeListener mCustomEventNativeListener;
        private final ImpressionTracker mImpressionTracker;
        private final NativeClickHandler mNativeClickHandler;
        private AdDelegator mAdDelegator;

        VM5StaticNativeAd(final Context context,
                          final ImpressionTracker impressionTracker,
                          final NativeClickHandler nativeClickHandler,
                          final CustomEventNativeListener customEventNativeListener) {
            mContext = context.getApplicationContext();
            mImpressionTracker = impressionTracker;
            mNativeClickHandler = nativeClickHandler;
            mCustomEventNativeListener = customEventNativeListener;
        }

        void setAdDelegator(final AdDelegator adDelegator) {
            mAdDelegator = adDelegator;
        }

        // Lifecycle Handlers
        @Override
        public void prepare(final View view) {
            mImpressionTracker.addView(view, this);
            mNativeClickHandler.setOnClickListener(view, this);
        }

        @Override
        public void clear(final View view) {
            mImpressionTracker.removeView(view);
            mNativeClickHandler.clearOnClickListener(view);
        }

        @Override
        public void destroy() {
            onDestroy();
            mImpressionTracker.destroy();
        }

        // Event Handlers
        @Override
        public void recordImpression(final View view) {
            notifyAdImpressed();
        }

        @Override
        public void handleClick(final View view) {
            notifyAdClicked();
            mNativeClickHandler.openClickDestinationUrl(getClickDestinationUrl(), view);
            //mAdDelegator.handleClick(null);
        }

        @Override
        public void onAdLoaded(AdObject adObject) {
            Log.d(TAG, "Native ad loaded successfully.");

            if (mAdDelegator == null) {
                mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.NETWORK_INVALID_STATE);
                return;
            }

            if (adObject.getAdTitle() != null && !adObject.getAdTitle().equals("")) {
                setTitle(adObject.getAdTitle());
            } else {
                setTitle("title");
            }
            if (adObject.getAdDescription() != null && !adObject.getAdDescription().equals("")) {
                setText(adObject.getAdDescription());
            } else {
                setText("description");
            }

            if (adObject.getImageUrl() != null) {
                setMainImageUrl(adObject.getImageUrl());
            }

            if (adObject.getIconUrl() != null) {
                setIconImageUrl(adObject.getIconUrl());
            }

            setClickDestinationUrl(adObject.getAdTrigger().getActionUrl());
            if (adObject.getAdCtaText() != null && !adObject.getAdCtaText().equals("")) {
                setCallToAction(adObject.getAdCtaText());
            } else {
                setCallToAction("More");
            }

            setStarRating(1.0);
            setImpressionMinTimeViewed(IMPRESSION_MIN_TIME_VIEWED);

            final List<String> imageUrls = new ArrayList<String>();
            final String mainImageUrl = getMainImageUrl();
            if (mainImageUrl != null) {
                imageUrls.add(mainImageUrl);
            }

            final String iconUrl = getIconImageUrl();
            if (iconUrl != null) {
                imageUrls.add(iconUrl);
            }

            preCacheImages(mContext, imageUrls, new NativeImageHelper.ImageListener() {
                @Override
                public void onImagesCached() {
                    mCustomEventNativeListener.onNativeAdLoaded(VM5StaticNativeAd.this);
                }

                @Override
                public void onImagesFailedToCache(NativeErrorCode errorCode) {
                    mCustomEventNativeListener.onNativeAdFailed(errorCode);
                }
            });
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
            Log.d(TAG, "Native ad clicked.");
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
}
