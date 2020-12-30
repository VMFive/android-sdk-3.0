package com.core.adnsdkdemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.core.adnsdk.AdBannerType;
import com.core.adnsdk.AdBannerView;
import com.core.adnsdk.AdListener;
import com.core.adnsdk.AdObject;
import com.core.adnsdk.AdProfile;
import com.core.adnsdk.AuthList;
import com.core.adnsdk.AuthListBuilder;
import com.core.adnsdk.ErrorMessage;
import com.core.adnsdk.KeywordList;
import com.core.adnsdk.KeywordListBuilder;
import com.core.adnsdk.TimeUnit;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventBanner;
import com.google.android.gms.ads.mediation.customevent.CustomEventBannerListener;

/**
 * Created by yangmingyi on 15/10/16.
 */
public class AdMobBanner implements CustomEventBanner {
    private final static String TAG = "AdMobBanner";

    private AdBannerView mAdBannerView;

    @Override
    public void requestBannerAd(final Context context,
                                final CustomEventBannerListener customEventBannerListener,
                                String serverParameter,
                                com.google.android.gms.ads.AdSize adSize,
                                MediationAdRequest mediationAdRequest,
                                Bundle bundle) {
        Log.d(TAG, "Request AdBannerView Ad");

        final AdBannerType adBannerType = AdBannerType.BANNER_VIDEO;
        if (mAdBannerView == null) {
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
                mAdBannerView = new AdBannerView((Activity) context, mAdProfile, adBannerType);
            } else {
                mAdBannerView = new AdBannerView(
                        (Activity) context
                        , "5630c874cef2370b13942b8f"
                        , "placement(banner_admob)"
                        , adBannerType);
                mAdBannerView.setTestMode(true);
            }
        }
        // because AdMob can control rotation frequency, it disable auto rotation here
        mAdBannerView.setRotation(false); // for banner
        mAdBannerView.setRotationTimeUnit(TimeUnit.STOP);
        mAdBannerView.setReloadAd(false);
        /**
         * Users are also capable of using {@link com.core.adnsdk.AdListenerAdapter}, default adapter design pattern of AdListener, to receive notification.
         * Therefore, users can focus on specific events they care about.
         */
        mAdBannerView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded(AdObject obj) {
                Log.i(TAG, "onAdLoaded(" + obj + ")");
                customEventBannerListener.onAdLoaded(mAdBannerView);
            }

            @Override
            public void onError(ErrorMessage err) {
                Log.i(TAG, "onError : " + err);
                switch (err) {
                    case GENERIC:
                        customEventBannerListener.onAdFailedToLoad(AdRequest.ERROR_CODE_INTERNAL_ERROR);
                        break;
                    case NOADS:
                        customEventBannerListener.onAdFailedToLoad(AdRequest.ERROR_CODE_NO_FILL);
                        break;
                    case RESOURCES_DOWNLOAD_FAIL:
                        customEventBannerListener.onAdFailedToLoad(AdRequest.ERROR_CODE_NETWORK_ERROR);
                        break;
                    case NOTVISIBLE:
                        customEventBannerListener.onAdFailedToLoad(AdRequest.ERROR_CODE_NO_FILL);
                        break;
                    default:
                        customEventBannerListener.onAdFailedToLoad(AdRequest.ERROR_CODE_INVALID_REQUEST);
                }
            }

            @Override
            public void onAdClicked() {
                Log.i(TAG, "onAdClicked!");
                customEventBannerListener.onAdClicked();
            }

            @Override
            public void onAdFinished() {
                customEventBannerListener.onAdOpened();
            }

            @Override
            public void onAdReleased() {
                customEventBannerListener.onAdClosed();
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
        mAdBannerView.loadAd();
    }

    @Override
    public void onDestroy() {
        if (mAdBannerView != null) {
            mAdBannerView.onDestroy();
            mAdBannerView = null;
        }
    }

    @Override
    public void onPause() {
        if (mAdBannerView != null) {
            mAdBannerView.onPause();
        }
    }

    @Override
    public void onResume() {
        if (mAdBannerView != null) {
            mAdBannerView.onResume();
        }
    }
}
