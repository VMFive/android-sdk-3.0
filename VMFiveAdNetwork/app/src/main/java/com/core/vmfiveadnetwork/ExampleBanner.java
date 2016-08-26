package com.core.vmfiveadnetwork;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.core.adnsdk.AdBannerType;
import com.core.adnsdk.AdBannerView;
import com.core.adnsdk.AdListener;
import com.core.adnsdk.AdObject;
import com.core.adnsdk.BannerAdRenderer;
import com.core.adnsdk.ErrorMessage;
import com.core.adnsdk.AdViewType;
import com.core.adnsdk.BannerViewBinder;
import com.core.adnsdk.AdView;

public class ExampleBanner extends FragmentActivity {
    private static final String TAG = "ExampleBanner";

    private AdBannerView mAdBannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_example);

        ViewGroup adLayout = (ViewGroup) findViewById(R.id.example_adlayout);

        // native video layout builder
        BannerViewBinder binder = new BannerViewBinder.Builder(R.layout.banner_ad_item)
                .loadingId(R.id.native_loading_image)
                .mainImageId(R.id.native_video_background_image)
                .videoPlayerId(R.id.native_video_player_layout)
                .build();

        // set layout builder to adRenderer
        BannerAdRenderer adRenderer = new BannerAdRenderer(binder);

        final AdBannerType adBannerType = AdBannerType.BANNER_VIDEO;
        mAdBannerView = new AdBannerView(
                  this
                , "5630c874cef2370b13942b8f"
                , "placement(banner_video)"
                , adBannerType);
        mAdBannerView.setViewParent(adLayout);
        // if user don't provide renderer, it would use default renderer.
        mAdBannerView.setAdRenderer(adRenderer);
        /**
         * Users are also capable of using {@link com.core.adnsdk.AdListenerAdapter}, default adapter design pattern of AdListener, to receive notification.
         * Therefore, users can focus on specific events they care about.
         */
        mAdBannerView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded(AdObject obj) {
                Log.d(TAG, "onAdLoaded(" + obj + ")");
            }

            @Override
            public void onError(ErrorMessage err) {
                Log.d(TAG, "onError : " + err);
                if (err != ErrorMessage.NOTREADY) {
                    Toast.makeText(getApplication(), "Error: " + err, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onAdClicked() {
                Log.d(TAG, "onAdClicked!");
            }

            @Override
            public void onAdFinished() {
                Log.d(TAG, "onAdFinished.");
            }

            @Override
            public void onAdReleased() {
                Log.d(TAG, "onAdReleased.");
            }

            @Override
            public boolean onAdWatched() {
                Log.d(TAG, "onAdWatched.");
                return true;
            }

            @Override
            public void onAdImpressed() {
                Log.d(TAG, "onAdImpressed.");
            }
        });
        //adLayout.addView(mAdBannerView);
        mAdBannerView.setTestMode(true);
        mAdBannerView.loadAd();
    }

    @Override
    protected void onPause() {
        if (mAdBannerView != null) {
            mAdBannerView.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mAdBannerView != null) {
            mAdBannerView.onResume();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mAdBannerView != null) {
            mAdBannerView.onDestroy();
            mAdBannerView = null;
        }
        super.onDestroy();
    }
}
