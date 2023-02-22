package com.core.adnsdkdemo;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.core.adnsdk.AdCardType;
import com.core.adnsdk.AdCardView;
import com.core.adnsdk.AdListener;
import com.core.adnsdk.AdObject;
import com.core.adnsdk.AdProfile;
import com.core.adnsdk.AuthList;
import com.core.adnsdk.AuthListBuilder;
import com.core.adnsdk.CardAdRenderer;
import com.core.adnsdk.CardViewBinder;
import com.core.adnsdk.ErrorMessage;
import com.core.adnsdk.KeywordList;
import com.core.adnsdk.KeywordListBuilder;

public class ExampleCard extends FragmentActivity {
    private static final String TAG = "ExampleCard";

    private AdCardView mAdCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_example);

        ViewGroup adLayout = (ViewGroup) findViewById(R.id.example_adlayout);

        // native video layout builder
        CardViewBinder binder = new CardViewBinder.Builder(R.layout.card_ad_item)
                .loadingId(R.id.native_loading_image)
                .mainImageId(R.id.native_main_image)
                .titleId(R.id.native_title)
                .subTitleId(R.id.native_subtitle)
                .videoPlayerId(R.id.native_video_layout)
                .iconImageId(R.id.native_icon_image)
                .callToActionId(R.id.native_cta)
                .countDownId(R.id.native_count_down)
                .build();

        // set layout builder to adRenderer
        CardAdRenderer adRenderer = new CardAdRenderer(binder);

        final AdCardType adCardType = AdCardType.CARD_VIDEO;
        if (BuildConfig.ADN_MULTIKEY) {
            AuthList mAuthList = new AuthListBuilder()
                    .add("Test0", "placement(banner_admob)", "default", 1)
                    .add("Test1", "placement(banner_admob)", "default", 2)
                    .build();
            KeywordList mKeywordList = new KeywordListBuilder()
                    .add("3c")
                    .add("cosmetic")
                    .build();
            AdProfile mAdProfile = new AdProfile.AdProfileBuilder()
                    .setAuthList(mAuthList)
                    .setKeywordList(mKeywordList)
                    .setTestMode(true).build();
            mAdCardView = new AdCardView(this, mAdProfile, adCardType);
        } else {
            mAdCardView = new AdCardView(this, "5630c874cef2370b13942b8f", "placement(card_video)", adCardType);
            mAdCardView.setTestMode(true);
        }
        mAdCardView.setViewParent(adLayout);
        // if user don't provide renderer, it would use default renderer.
        mAdCardView.setAdRenderer(adRenderer);
        /**
         * Users are also capable of using {@link com.core.adnsdk.AdListenerAdapter}, default adapter design pattern of AdListener, to receive notification.
         * Therefore, users can focus on specific events they care about.
         */
        mAdCardView.setAdListener(new AdListener() {
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
        //adLayout.addView(mAdCardView);
        mAdCardView.loadAd();
    }

    @Override
    protected void onPause() {
        if (mAdCardView != null) {
            mAdCardView.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mAdCardView != null) {
            mAdCardView.onResume();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mAdCardView != null) {
            mAdCardView.onDestroy();
            mAdCardView = null;
        }
        super.onDestroy();
    }
}
