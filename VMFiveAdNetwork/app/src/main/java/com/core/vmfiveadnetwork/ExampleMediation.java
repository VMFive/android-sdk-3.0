package com.core.vmfiveadnetwork;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;


public class ExampleMediation extends FragmentActivity {
    private static final String TAG = "ExampleMediation";

    private static final String ADMOB_BANNER_UNIT_ID = "ca-app-pub-4125394451256992/1741888066";
    private static final String ADMOB_INTERSTITIAL_UNIT_ID = "ca-app-pub-4125394451256992/4695354464";

    private AdView mAdView = null;
    // use InterstitialAdView to avoid memory leaks of AdMob.InterstitialAd
    private InterstitialAdView mInterstitialAdView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mediation);

        if (mInterstitialAdView == null) {
            // if user have multiple instances of InterstitialAd, you need to specify InterstitialAdView.setInstances(instances);
            mInterstitialAdView = new InterstitialAdView(this);
            mInterstitialAdView.setAdUnitId(ADMOB_INTERSTITIAL_UNIT_ID);
        }

        if (mAdView == null) {
            mAdView = (AdView) findViewById(R.id.adview);
        }

        (findViewById(R.id.banner_mediation)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mAdView.isLoading()) {
                    mAdView.destroy();
                    mAdView.loadAd(new AdRequest.Builder().build());
                }
            }
        });

        final Button interstitial_button = (Button) (findViewById(R.id.interstitial_mediation));
        interstitial_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAdView.isLoaded()) {
                    mInterstitialAdView.show();
                    interstitial_button.setText("mediation request: interstitial");
                } else if (!mInterstitialAdView.isLoading()) {
                    mInterstitialAdView.loadAd(new AdRequest.Builder().build());
                    interstitial_button.setText("mediation show: interstitial");
                } else {
                    // do nothing
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdView != null) {
            mAdView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        destroyAdView();
        super.onDestroy();
    }

    private void destroyAdView() {
        if (mAdView != null) {
            ViewParent parent = mAdView.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(mAdView);
            }
            mAdView.removeAllViews();
            mAdView.setAdListener(null);
            mAdView.destroy();
            mAdView = null;
        }
        if (mInterstitialAdView != null) {
            mInterstitialAdView.destory();
        }
    }
}
