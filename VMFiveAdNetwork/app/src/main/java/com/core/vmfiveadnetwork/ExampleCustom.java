package com.core.vmfiveadnetwork;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.core.adnsdk.AdCustom;
import com.core.adnsdk.AdListener;
import com.core.adnsdk.AdObject;
import com.core.adnsdk.AdViewType;
import com.core.adnsdk.CardAdRenderer;
import com.core.adnsdk.CardViewBinder;
import com.core.adnsdk.ErrorMessage;

/**
 * Created by Shawn on 9/7/15.
 */
public class ExampleCustom extends FragmentActivity {
    private static final String TAG = "ExampleCustom";

    private AdCustom mAdCustom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_example);

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

        final ViewGroup mainContainer = (ViewGroup) findViewById(R.id.example_adlayout);
        final AdViewType adViewType = AdViewType.CARD_VIDEO;
        mAdCustom = new AdCustom(
                  this
                , "5630c874cef2370b13942b8f"
                , "placement(custom)"
                , adViewType);
        mAdCustom.setViewParent(mainContainer);
        // if user don't provide renderer, it would use default renderer.
        mAdCustom.setAdRenderer(adRenderer);
        /**
         * Users are also capable of using {@link com.core.adnsdk.AdListenerAdapter}, default adapter design pattern of AdListener, to receive notification.
         * Therefore, users can focus on specific events they care about.
         */
        mAdCustom.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded(AdObject ad) {
                Log.d(TAG, "onAdLoaded(" + ad + ")");
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
        mAdCustom.setTestMode(true);
        mAdCustom.loadAd();
    }

    @Override
    protected void onResume() {
        if (mAdCustom != null) {
            mAdCustom.onResume();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (mAdCustom != null) {
            mAdCustom.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mAdCustom != null) {
            mAdCustom.onDestroy();
            mAdCustom = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
