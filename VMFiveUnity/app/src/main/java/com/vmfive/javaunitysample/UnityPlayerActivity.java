package com.vmfive.javaunitysample;

import com.core.adnsdk.ADN;
import com.core.adnsdk.AdListener;
import com.core.adnsdk.AdObject;
import com.core.adnsdk.AdView;
import com.core.adnsdk.AdViewType;
import com.core.adnsdk.CardAdRenderer;
import com.core.adnsdk.CardViewBinder;
import com.core.adnsdk.ErrorMessage;
import com.unity3d.player.*;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class UnityPlayerActivity extends Activity
{
	private static final String TAG = "UnityPlayerActivity";
	
	protected UnityPlayer mUnityPlayer; // don't change the name of this variable; referenced from native code
    
	private RelativeLayout mRelativeLayout;
	private FrameLayout mFrameLayout;
    private AdView mAdView;

	// Setup activity layout
	@Override protected void onCreate (Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		getWindow().setFormat(PixelFormat.RGBX_8888); // <--- This makes xperia play happy

		mUnityPlayer = new UnityPlayer(this);
		
		setContentView(mUnityPlayer);
		
		mRelativeLayout = new RelativeLayout(UnityPlayerActivity.this);
		RelativeLayout.LayoutParams relativeLayoutLp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		mFrameLayout = new FrameLayout(UnityPlayerActivity.this);
	
		relativeLayoutLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		relativeLayoutLp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		mRelativeLayout.addView(mFrameLayout, relativeLayoutLp);
        
		ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
	    mUnityPlayer.currentActivity.getWindow().addContentView(mRelativeLayout, vlp);

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
        // set layout builder to renderer
        CardAdRenderer renderer = new CardAdRenderer(binder);

        final AdViewType adViewType = AdViewType.CARD_VIDEO;
        mAdView = new AdView(this,
                "5630c874cef2370b13942b8f",
                "placement(card_video)",
                adViewType);
        mAdView.setAdRenderer(renderer);
        mAdView.setViewParent(mFrameLayout);

        /**
         * Users are also capable of using {@link com.core.adnsdk.AdListenerAdapter}, default adapter design pattern of AdListener, to receive notification.
         * Therefore, users can focus on specific events they care about.
         */
        mAdView.setAdListener(new AdListener() {
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
        mAdView.setTestMode(true);
        mAdView.loadAd();
        
		
		mUnityPlayer.requestFocus();
	}

	// Quit Unity
	@Override protected void onDestroy ()
	{
		mUnityPlayer.quit();
		super.onDestroy();
		if (mAdView != null) {
            mAdView.onDestroy();
            mAdView = null;
        }
	}

	// Pause Unity
	@Override protected void onPause()
	{
		super.onPause();
		mUnityPlayer.pause();
		if (mAdView != null) {
            mAdView.onPause();
        }
	}

	// Resume Unity
	@Override protected void onResume()
	{
		super.onResume();
		mUnityPlayer.resume();
		if (mAdView != null) {
            mAdView.onResume();
        }
	}

	// This ensures the layout will be correct.
	@Override public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		mUnityPlayer.configurationChanged(newConfig);
		
		if (mAdView != null) {
			mAdView.onDestroy();
			mAdView = null;
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
        // set layout builder to renderer
        CardAdRenderer renderer = new CardAdRenderer(binder);

        final AdViewType adViewType = AdViewType.CARD_VIDEO;
        mAdView = new AdView(this,
                "5630c874cef2370b13942b8f",
                "placement(card_video)",
                adViewType);
        mAdView.setAdRenderer(renderer);
        mAdView.setViewParent(mFrameLayout);

        /**
         * Users are also capable of using {@link com.core.adnsdk.AdListenerAdapter}, default adapter design pattern of AdListener, to receive notification.
         * Therefore, users can focus on specific events they care about.
         */
        mAdView.setAdListener(new AdListener() {
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
        mAdView.setTestMode(true);
        mAdView.loadAd();
	}

	// Notify Unity of the focus change.
	@Override public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);
		mUnityPlayer.windowFocusChanged(hasFocus);
	}

	// For some reason the multiple keyevent type is not supported by the ndk.
	// Force event injection by overriding dispatchKeyEvent().
	@Override public boolean dispatchKeyEvent(KeyEvent event)
	{
		if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
			return mUnityPlayer.injectEvent(event);
		return super.dispatchKeyEvent(event);
	}

	// Pass any events not handled by (unfocused) views straight to UnityPlayer
	@Override public boolean onKeyUp(int keyCode, KeyEvent event)     { return mUnityPlayer.injectEvent(event); }
	@Override public boolean onKeyDown(int keyCode, KeyEvent event)   { return mUnityPlayer.injectEvent(event); }
	@Override public boolean onTouchEvent(MotionEvent event)          { return mUnityPlayer.injectEvent(event); }
	/*API12*/ public boolean onGenericMotionEvent(MotionEvent event)  { return mUnityPlayer.injectEvent(event); }
}
