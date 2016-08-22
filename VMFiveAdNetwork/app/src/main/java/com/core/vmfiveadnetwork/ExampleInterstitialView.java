package com.core.vmfiveadnetwork;

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

import com.core.adnsdk.AdInterstitial;
import com.core.adnsdk.AdInterstitialType;
import com.core.adnsdk.AdInterstitialView;

public class ExampleInterstitialView extends FragmentActivity {
    private static final String TAG = "ExampleInterstitialView";

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

        private class VerticalPageTransformer implements ViewPager.PageTransformer {

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
        public boolean onInterceptTouchEvent(MotionEvent ev){
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
        private AdInterstitialView mAdInterstitialView;

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
        public void destroyItem(ViewGroup container, int position, Object object)   {
            if (position == 2) {
                AdInterstitialView adInterstitialView = (AdInterstitialView) object;
                adInterstitialView.onDestroy();
                mAdInterstitialView = null;
            }
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = null;
            if (position == 0) {
                view = new View(ExampleInterstitialView.this);
                view.setBackgroundColor(Color.BLUE);
            } else if (position == 1) {
                view = new View(ExampleInterstitialView.this);
                view.setBackgroundColor(Color.YELLOW);
            } else { // position == 2
                final AdInterstitialType adInterstitialType = AdInterstitialType.INTERSTITIAL_VIDEO;
                AdInterstitialView adInterstitialView  = new AdInterstitialView(
                          mActivity
                        , "5630c874cef2370b13942b8f"
                        , "placement(interstitial_view)"
                        , adInterstitialType);
                adInterstitialView.setTestMode(true);
                adInterstitialView.loadAd();
                mAdInterstitialView = adInterstitialView;
                view = adInterstitialView;
            }
            container.addView(view);
            return view;
        }

        public void onDestroy() {
            if (mAdInterstitialView != null) {
                mAdInterstitialView.onDestroy();
                mAdInterstitialView = null;
            }
        }
    }
}
