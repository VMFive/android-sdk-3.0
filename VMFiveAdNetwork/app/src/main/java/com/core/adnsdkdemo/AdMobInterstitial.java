package com.core.adnsdkdemo;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.core.adnsdk.AdInterstitial;
import com.core.adnsdk.AdInterstitialType;
import com.core.adnsdk.AdListener;
import com.core.adnsdk.AdObject;
import com.core.adnsdk.AdProfile;
import com.core.adnsdk.AuthList;
import com.core.adnsdk.AuthListBuilder;
import com.core.adnsdk.ErrorMessage;
import com.core.adnsdk.KeywordList;
import com.core.adnsdk.KeywordListBuilder;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitial;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitialListener;


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
        if (BuildConfig.ADN_MULTIKEY) {
            AuthList mAuthList = new AuthListBuilder()
                    .add("Test0", "placement(banner_admob)", "default", 1)
                    .add("Test1", "placement(banner_admob)", "default", 2).build();
            KeywordList mKeywordList = new KeywordListBuilder()
                    .add("3c")
                    .add("cosmetic").build();
            AdProfile mAdProfile = new AdProfile.AdProfileBuilder()
                    .setAuthList(mAuthList)
                    .setKeywordList(mKeywordList)
                    .setTestMode(true).build();
            adInterstitial = new AdInterstitial(context, mAdProfile, adInterstitialType);
        } else {
            adInterstitial = new AdInterstitial(
                    context
                    , "5630c874cef2370b13942b8f"
                    , "placement(interstitial_admob)"
                    , adInterstitialType);
            adInterstitial.setTestMode(true);
        }
        AdInterstitial.addAdInterstitial(adInterstitial);

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
