package com.core.vmfiveadnetwork;

import com.core.adnsdk.AdBannerType;
import com.core.adnsdk.AdInterstitial;
import com.core.adnsdk.AdInterstitialType;
import com.core.adnsdk.AdListener;
import com.core.adnsdk.AdObject;
import com.core.adnsdk.ErrorMessage;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitial;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitialListener;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;


/**
 * Created by yangmingyi on 15/10/18.
 */
// TODO: still has some admob memory leaks
public class AdMobInterstitial implements CustomEventInterstitial {
    private static final String TAG = "AdMobInterstitial";

    public AdInterstitial adInterstitial;

    @Override
    public void requestInterstitialAd(final Context context,
                                      final CustomEventInterstitialListener listener,
                                      String serverParameter,
                                      MediationAdRequest mediationAdRequest,
                                      Bundle customEventExtras) {
        Log.d(TAG, "Request AdInterstitial Ad");

        final AdInterstitialType adInterstitialType = AdInterstitialType.INTERSTITIAL_VIDEO;
        adInterstitial = new AdInterstitial(
                  context
                , "5630c874cef2370b13942b8f"
                , "placement(interstitial_admob)"
                , adInterstitialType);

        AdInterstitial.addAdInterstitial(adInterstitial);

        adInterstitial.setTestMode(true);
        /**
         * Users are also capable of using {@link com.core.adnsdk.AdListenerAdapter}, default adapter design pattern of AdListener, to receive notification.
         * Therefore, users can focus on specific events they care about.
         */
        adInterstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded(AdObject obj) {
                Log.d(TAG, "onAdLoaded(" + obj + ")");
                listener.onAdLoaded();
            }

            @Override
            public void onError(ErrorMessage err) {
                Log.w(TAG, "onError : " + err);
                switch (err) {
                    case GENERIC:
                        listener.onAdFailedToLoad(AdRequest.ERROR_CODE_INTERNAL_ERROR);
                        break;
                    case NOADS:
                        listener.onAdFailedToLoad(AdRequest.ERROR_CODE_NO_FILL);
                        break;
                    case RESOURCES_DOWNLOAD_FAIL:
                        listener.onAdFailedToLoad(AdRequest.ERROR_CODE_NETWORK_ERROR);
                        break;
                    case NOTVISIBLE:
                        listener.onAdFailedToLoad(AdRequest.ERROR_CODE_NO_FILL);
                        break;
                    default:
                        listener.onAdFailedToLoad(AdRequest.ERROR_CODE_INVALID_REQUEST);
                }
            }

            @Override
            public void onAdClicked() {
                Log.d(TAG, "onAdClicked!");
                listener.onAdClicked();
            }

            @Override
            public void onAdFinished() {
                Log.d(TAG, "onAdFinished.");
                listener.onAdOpened();
            }

            @Override
            public void onAdReleased() {
                Log.d(TAG, "onAdReleased.");
                listener.onAdClosed();
                onDestroy();
            }

            @Override
            public boolean onAdWatched() {
                Log.d(TAG, "onAdWatched.");
                return false;
            }

            @Override
            public void onAdImpressed() {
                Log.d(TAG, "onAdImpressed.");
            }
        });
        adInterstitial.loadAd();
    }

    @Override
    public void onResume() {
        if (adInterstitial != null) {
            adInterstitial.onResume();
        }
    }

    @Override
    public void onPause() {
        if (adInterstitial != null) {
            adInterstitial.onPause();
        }
    }

    @Override
    public void onDestroy() {
        if (adInterstitial != null) {
            AdInterstitial.removeAdInterstitial(adInterstitial);
            adInterstitial.onDestroy();
            adInterstitial = null;
        }
    }

    @Override
    public void showInterstitial() {
        // Show your interstitial ad.
        adInterstitial.showAd();
    }
}
