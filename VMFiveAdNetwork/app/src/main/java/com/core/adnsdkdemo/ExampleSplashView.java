package com.core.adnsdkdemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.core.adnsdk.AdProfile;
import com.core.adnsdk.AdSplashType;
import com.core.adnsdk.AdSplashView;
import com.core.adnsdk.AuthList;
import com.core.adnsdk.AuthListBuilder;
import com.core.adnsdk.KeywordList;
import com.core.adnsdk.KeywordListBuilder;

public class ExampleSplashView extends FragmentActivity {
    private static final String TAG = "ExampleSplashView";

    private VerticalViewPager mViewPager;
    private MyPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewPager = new VerticalViewPager(this);
        mPagerAdapter = new MyPagerAdapter(this);
        mViewPager.setAdapter(mPagerAdapter);
        setContentView(mViewPager);
    }

    @Override
    protected void onDestroy() {
        if (mPagerAdapter != null) {
            mPagerAdapter.onDestroy();
            mPagerAdapter = null;
        }
        super.onDestroy();
    }

    public class VerticalViewPager extends ViewPager {

        public VerticalViewPager(Context context) {
            super(context);
            init();
        }

        public VerticalViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        private void init() {
            // The majority of the magic happens here
            setPageTransformer(true, new VerticalPageTransformer());
            // The easiest way to get rid of the overscroll drawing that happens on the left and right
            setOverScrollMode(OVER_SCROLL_NEVER);
        }

        private class VerticalPageTransformer implements PageTransformer {

            @Override
            public void transformPage(View view, float position) {

                if (position < -1) { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    view.setAlpha(0);

                } else if (position <= 1) { // [-1,1]
                    view.setAlpha(1);

                    // Counteract the default slide transition
                    view.setTranslationX(view.getWidth() * -position);

                    //set Y position to swipe in from top
                    float yPosition = position * view.getHeight();
                    view.setTranslationY(yPosition);

                } else { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    view.setAlpha(0);
                }
            }
        }

        /**
         * Swaps the X and Y coordinates of your touch event.
         */
        private MotionEvent swapXY(MotionEvent ev) {
            float width = getWidth();
            float height = getHeight();

            float newX = (ev.getY() / height) * width;
            float newY = (ev.getX() / width) * height;

            ev.setLocation(newX, newY);
            return ev;
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            boolean intercepted = super.onInterceptTouchEvent(swapXY(ev));
            swapXY(ev); // return touch coordinates to original reference frame for any child views
            return intercepted;
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            return super.onTouchEvent(swapXY(ev));
        }
    }

    public class MyPagerAdapter extends PagerAdapter {
        private Activity mActivity;
        private AdSplashView mAdSplashView;

        public MyPagerAdapter(Activity activity) {
            mActivity = activity;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (position == 2) {
                AdSplashView splashView = (AdSplashView) object;
                splashView.onDestroy();
                mAdSplashView = null;
            }
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = null;
            if (position == 0) {
                view = new View(ExampleSplashView.this);
                view.setBackgroundColor(Color.BLUE);
            } else if (position == 1) {
                view = new View(ExampleSplashView.this);
                view.setBackgroundColor(Color.YELLOW);
            } else { // position == 2
                final AdSplashType adSplashType = AdSplashType.SPLASH_VIDEO;
                AdSplashView adSplashView;
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
                    adSplashView = new AdSplashView(
                            mActivity
                            , mAdProfile
                            , adSplashType);
                } else {
                    adSplashView = new AdSplashView(
                            mActivity
                            , "5630c874cef2370b13942b8f"
                            , "placement(splash_view)"
                            , adSplashType);
                    adSplashView.setTestMode(true);
                }
                adSplashView.loadAd();
                mAdSplashView = adSplashView;
                view = adSplashView;
            }
            container.addView(view);
            return view;
        }

        public void onDestroy() {
            if (mAdSplashView != null) {
                mAdSplashView.onDestroy();
                mAdSplashView = null;
            }
        }
    }
}
