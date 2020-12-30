package com.core.adnsdkdemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.core.adnsdk.AdBannerType;
import com.core.adnsdk.AdBannerView;
import com.core.adnsdk.AdProfile;
import com.core.adnsdk.AuthList;
import com.core.adnsdk.AdViewType;
import com.core.adnsdk.AdObject;
import com.core.adnsdk.AuthListBuilder;
import com.core.adnsdk.CardAdRenderer;
import com.core.adnsdk.CardViewBinder;
import com.core.adnsdk.ErrorMessage;
import com.core.adnsdk.AdListener;
import com.core.adnsdk.KeywordList;
import com.core.adnsdk.KeywordListBuilder;
import com.core.adnsdk.NativeAdAdapter;
import com.core.adnsdk.AdPoolListener;

public class ExampleListViewWithBanner extends Activity {
    private static final String TAG = "ExampleListViewWithBanner";

    private NativeAdAdapter mNativeAdAdapter;
    private AdBannerView mAdBannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_listview_with_banner);

        final ListView listView = (ListView) findViewById(R.id.native_list_view);

        // original adapter of user
        final ArrayAdapter<String> originalAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1);
        // just for demo
        for (int i = 0; i < 100; ++i) {
            originalAdapter.add("Item " + i);
        }

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

        final Context context = getApplication();
        final AdViewType adViewType = AdViewType.CARD_VIDEO;
        // create NativeAdAdapter, and given original adapter of user
        // given a placement tag for different advertisement section
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
            mNativeAdAdapter = new NativeAdAdapter(
                    this
                    , listView
                    , originalAdapter
                    , mAdProfile
                    , adViewType);
        } else {
            mNativeAdAdapter = new NativeAdAdapter(
                    this
                    , listView
                    , originalAdapter
                    , "5630c874cef2370b13942b8f"
                    , "placement(list_banner)"
                    , adViewType);
            mAdBannerView.setTestMode(true);
        }
        // if user don't provide renderer, it would use default renderer.
        mNativeAdAdapter.setAdRenderer(adRenderer, AdViewType.CARD_VIDEO); // for Video type
        mNativeAdAdapter.setFrequency(1, 3);
        /**
         * Users are also capable of using {@link com.core.adnsdk.AdPoolListenerAdapter}, default adapter design pattern of AdPoolListener, to receive notification.
         * Therefore, users can focus on specific events they care about.
         */
        mNativeAdAdapter.setAdListener(new AdPoolListener() {
            @Override
            public void onError(int index, ErrorMessage err) {
                Log.d(TAG, "onError : " + err);
                if (err != ErrorMessage.NOTREADY) {
                    Toast.makeText(context, "Error: " + err, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onAdLoaded(int index, AdObject obj) {
                Log.d(TAG, "onAdLoaded!");
            }

            @Override
            public void onAdClicked(int index) {
                Log.d(TAG, "onAdClicked!");
            }

            @Override
            public void onAdFinished(int index) {
                Log.d(TAG, "onAdFinished.");
            }

            @Override
            public void onAdReleased(int index) {
                Log.d(TAG, "onAdReleased.");
            }

            @Override
            public boolean onAdWatched(int index) {
                Log.d(TAG, "onAdWatched.");
                return false;
            }

            @Override
            public void onAdImpressed(int index) {
                Log.d(TAG, "onAdImpressed.");
            }
        });
        // (Optional) sdk already set listener to ListView, if user want to set listener of ListView,
        // please set to NativeAdAdapter
        mNativeAdAdapter.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        // (Optional) sdk already set listener to ListView, if user want to set listener of ListView,
        // please set to NativeAdAdapter
        mNativeAdAdapter.setRecyclerListener(new AbsListView.RecyclerListener() {
            @Override
            public void onMovedToScrapHeap(View view) {

            }
        });
        listView.setAdapter(mNativeAdAdapter);

        ViewGroup adLayout = (ViewGroup) findViewById(R.id.example_adlayout);

        final AdBannerType adBannerType = AdBannerType.BANNER_VIDEO;
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
            mAdBannerView = new AdBannerView(
                    this
                    , mAdProfile
                    , adBannerType);
        } else {
            mAdBannerView = new AdBannerView(
                    this
                    , "5630c874cef2370b13942b8f"
                    , "placement(banner_video)"
                    , adBannerType);
            mAdBannerView.setTestMode(true);
        }
        mAdBannerView.setViewParent(adLayout);
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
    protected void onPause() {
        if (mAdBannerView != null) {
            mAdBannerView.onPause();
        }
        if (mNativeAdAdapter != null) {
            mNativeAdAdapter.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mAdBannerView != null) {
            mAdBannerView.onResume();
        }
        if (mNativeAdAdapter != null) {
            mNativeAdAdapter.onResume();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mAdBannerView != null) {
            mAdBannerView.onDestroy();
            mAdBannerView = null;
        }
        if (mNativeAdAdapter != null) {
            mNativeAdAdapter.onDestroy();
            mNativeAdAdapter = null;
        }
        super.onDestroy();
    }
}
