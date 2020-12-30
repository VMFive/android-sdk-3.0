/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.core.adnsdkdemo;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.core.adnsdk.ADN;
import com.core.adnsdk.AdInterstitial;
import com.core.adnsdk.AdInterstitialType;
import com.core.adnsdk.AdListener;
import com.core.adnsdk.AdObject;
import com.core.adnsdk.AdProfile;
import com.core.adnsdk.AdSplash;
import com.core.adnsdk.AdSplashType;
import com.core.adnsdk.AuthList;
import com.core.adnsdk.AdReward;
import com.core.adnsdk.AdRewardListener;
import com.core.adnsdk.AdRewardType;
import com.core.adnsdk.AuthListBuilder;
import com.core.adnsdk.ErrorMessage;
import com.core.adnsdk.KeywordList;
import com.core.adnsdk.KeywordListBuilder;
import com.core.adnsdk.SDKController;
import com.core.adnsdk.VersionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {
    private static final String TAG = "MainActivity";

    private static final int MENU_ABOUT = 0;
    private static final int MENU_CLEAR_RESOURCE_CACHE = 1;
    private static final int MENU_CLEAR_CONNECTION_CACHE = 2;
    private static final int MENU_CLEAR_PREFERENCES_CACHE = 3;
    private static final int MENU_EXIT = 4;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * three primary sections of the app. We use a {@link FragmentPagerAdapter}
     * derivative, which will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will display the three primary sections of the app, one at a
     * time.
     */
    ViewPager mViewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager(), this);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();

        if (actionBar != null) {
            // Specify that the Home/Up button should not be enabled, since there is no hierarchical
            // parent.
            actionBar.setHomeButtonEnabled(false);

            // Specify that we will be displaying tabs in the action bar.
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        }

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.

                if (actionBar != null)
                    actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.

            if (actionBar != null)
                actionBar.addTab(
                        actionBar.newTab()
                                .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                                .setTabListener(this));
        }

        // check permissions for M, if some permission denied, it would shut down activity
        checkRequiredPermissions();

        // need to enable app scan in backend, and prompt this dialog to notice user
        //ADN.showAppScanDialog(this, "是否同意讓我們收集App安裝資訊");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_ABOUT, 0, "About");
        menu.add(0, MENU_CLEAR_RESOURCE_CACHE, 0, "Clear File Cache");
        menu.add(0, MENU_CLEAR_CONNECTION_CACHE, 0, "Clear Connection Cache");
        menu.add(0, MENU_CLEAR_PREFERENCES_CACHE, 0, "Clear Preferences Cache");
        menu.add(0, MENU_EXIT, 0, "Exit");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case MENU_ABOUT:
                String title = "Information";
                String messages = "";
                messages += "Version Name: " + SDKController.getInstance(this).getSdkName() + "\n";
                messages += "Version Code: " + SDKController.getInstance(this).getSdkVersion() + "." + SDKController.getInstance(this).getSdkBuild() + "\n";
                messages += "IMEI: " + SDKController.getInstance(this).getDeviceInfo().getIMEI() + "\n";
                messages += "MAC addr: " + SDKController.getInstance(this).getDeviceInfo().getWifiMac() + "\n";
                messages += "IP addr: " + SDKController.getInstance(this).getDeviceInfo().getIPAddress() + "\n";
                messages += "Advertising Id: " + SDKController.getInstance(this).getDeviceInfo().getAdvertisingId();
                final AlertDialog alertDialog = getAlertDialog(title, messages);
                alertDialog.show();
                break;
            case MENU_CLEAR_RESOURCE_CACHE:
                ADN.clearResourceCache(this);
                Toast.makeText(this, "Clear resource caches.", Toast.LENGTH_LONG).show();
                break;
            case MENU_CLEAR_CONNECTION_CACHE:
                ADN.clearConnectionCache(this);
                Toast.makeText(this, "Clear connection caches.", Toast.LENGTH_LONG).show();
                break;
            case MENU_CLEAR_PREFERENCES_CACHE:
                ADN.clearPreferencesCache(this);
                Toast.makeText(this, "Clear preferences caches.", Toast.LENGTH_LONG).show();
                break;
            case MENU_EXIT:
                finish();
                break;
        }
        return true;
    }

    private AlertDialog getAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return builder.create();
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {
        private Context mContext;
        private final static Class[] pageFragmentClassList = new Class[]{
                Banner.class,
                Card.class,
                Interstitial.class,
//                Splash.class,
                Native.class,
//                Reward.class,
                Other.class,
        };

        public AppSectionsPagerAdapter(FragmentManager fm, Context ctx) {
            super(fm);
            mContext = ctx;
        }

        @Override
        public Fragment getItem(int i) {
            if (i >= 0 && i < pageFragmentClassList.length) {
//                return Fragment.instantiate(mContext, pageFragmentClassList[i].getCanonicalName());
                try {
                    return (Fragment) pageFragmentClassList[i].newInstance();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
            }

            // The other sections of the app are dummy placeholders.
            Fragment fragment = new DummySectionFragment();
            Bundle args = new Bundle();
            args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return pageFragmentClassList.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Fragment fragment = getItem(position);
            if (fragment instanceof MainActivityFragment) {
                return mContext.getString(((MainActivityFragment)fragment).getPageTitleResID());
            }

            return "Section " + (position + 1);
        }
    }

    abstract static class MainActivityFragment extends Fragment {
        public abstract int getPageTitleResID();
    }

    /**
     * A fragment that launches other parts of the demo application.
     */
    public static class Banner extends MainActivityFragment {

        @Override
        public int getPageTitleResID() {
            return R.string.banner_title;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_banner, container, false);

            rootView.findViewById(R.id.banner_video)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), ExampleBanner.class);
                            startActivity(intent);
                        }
                    });

            return rootView;
        }
    }


    /**
     * A fragment that launches other parts of the demo application.
     */
    public static class Card extends MainActivityFragment {

        @Override
        public int getPageTitleResID() {
            return R.string.card_title;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_card, container, false);

            rootView.findViewById(R.id.card_video)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), ExampleCard.class);
                            startActivity(intent);
                        }
                    });

            rootView.findViewById(R.id.view_video)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), ExampleView.class);
                            startActivity(intent);
                        }
                    });

            rootView.findViewById(R.id.custom_video)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), ExampleCustom.class);
                            startActivity(intent);
                        }
                    });

            return rootView;
        }
    }

    /**
     * A fragment that launches other parts of the demo application.
     */
    public static class Interstitial extends MainActivityFragment {

        private AdInterstitial mAdInterstitial;

        @Override
        public int getPageTitleResID() {
            return R.string.interstitial_title;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
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
                mAdInterstitial = new AdInterstitial(
                        getActivity()
                        , mAdProfile
                        , AdInterstitialType.INTERSTITIAL_VIDEO);
            } else {
                mAdInterstitial = new AdInterstitial(
                        getActivity()
                        , "5630c874cef2370b13942b8f"
                        , "placement(interstitial_video)"
                        , AdInterstitialType.INTERSTITIAL_VIDEO);
                mAdInterstitial.setTestMode(true);
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_interstitial, container, false);

            rootView.findViewById(R.id.interstitial_video)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {
                            /**
                             * Users are also capable of using {@link com.core.adnsdk.AdListenerAdapter}, default adapter design pattern of AdListener, to receive notification.
                             * Therefore, users can focus on specific events they care about.
                             */
                            mAdInterstitial.setAdListener(new AdListener() {
                                @Override
                                public void onAdLoaded(AdObject obj) {
                                    Log.d(TAG, "onAdLoaded(" + obj + ")");
                                    mAdInterstitial.showAd();
                                }

                                @Override
                                public void onError(ErrorMessage err) {
                                    Log.d(TAG, "onError : " + err + ")");
                                    Toast.makeText(view.getContext(), "Error: " + err, Toast.LENGTH_LONG).show();
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
                            mAdInterstitial.loadAd();
                        }
                    });

            rootView.findViewById(R.id.interstitial_view)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), ExampleInterstitialView.class);
                            startActivity(intent);
                        }
                    });

            return rootView;
        }

        @Override
        public void onResume() {
            if (mAdInterstitial != null) {
                mAdInterstitial.onResume();
            }
            super.onResume();
        }

        @Override
        public void onPause() {
            if (mAdInterstitial != null) {
                mAdInterstitial.onPause();
            }
            super.onPause();
        }

        @Override
        public void onDestroy() {
            if (mAdInterstitial != null) {
                mAdInterstitial.onDestroy();
                mAdInterstitial = null;
            }
            super.onDestroy();
        }
    }

    /**
     * A fragment that launches other parts of the demo application.
     */
    public static class Splash extends MainActivityFragment {


        private AdSplash mAdSplash;

        @Override
        public int getPageTitleResID() {
            return R.string.splash_title;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
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
                mAdSplash = new AdSplash(
                        getActivity()
                        , mAdProfile
                        , AdSplashType.SPLASH_VIDEO);
            } else {
                mAdSplash = new AdSplash(
                        getActivity()
                        , "5630c874cef2370b13942b8f"
                        , "placement(splash_video)"
                        , AdSplashType.SPLASH_VIDEO);
                mAdSplash.setTestMode(true);
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_splash, container, false);

            rootView.findViewById(R.id.splash_video)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {
                            /**
                             * Users are also capable of using {@link com.core.adnsdk.AdListenerAdapter}, default adapter design pattern of AdListener, to receive notification.
                             * Therefore, users can focus on specific events they care about.
                             */
                            mAdSplash.setAdListener(new AdListener() {
                                @Override
                                public void onAdLoaded(AdObject obj) {
                                    Log.d(TAG, "onAdLoaded(" + obj + ")");
                                    mAdSplash.showAd();
                                }

                                @Override
                                public void onError(ErrorMessage err) {
                                    Log.d(TAG, "onError : " + err + ")");
                                    Toast.makeText(view.getContext(), "Error: " + err, Toast.LENGTH_LONG).show();
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
                            mAdSplash.loadAd();
                        }
                    });

            rootView.findViewById(R.id.splash_view)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), ExampleSplashView.class);
                            startActivity(intent);
                        }
                    });

            return rootView;
        }

        @Override
        public void onResume() {
            if (mAdSplash != null) {
                mAdSplash.onResume();
            }
            super.onResume();
        }

        @Override
        public void onPause() {
            if (mAdSplash != null) {
                mAdSplash.onPause();
            }
            super.onPause();
        }

        @Override
        public void onDestroy() {
            if (mAdSplash != null) {
                mAdSplash.onDestroy();
                mAdSplash = null;
            }
            super.onDestroy();
        }
    }

    /**
     * A fragment that launches other parts of the demo application.
     */
    public static class Native extends MainActivityFragment {

        @Override
        public int getPageTitleResID() {
            return R.string.native_title;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_native, container, false);

            rootView.findViewById(R.id.native_listview)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), ExampleListView.class);
                            startActivity(intent);
                        }
                    });

            rootView.findViewById(R.id.native_listview_with_banner)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), ExampleListViewWithBanner.class);
                            startActivity(intent);
                        }
                    });

            rootView.findViewById(R.id.native_recyclerview)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), ExampleRecyclerView.class);
                            startActivity(intent);
                        }
                    });

            return rootView;
        }
    }

    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    public static class DummySectionFragment extends Fragment {

        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_dummy, container, false);
            Bundle args = getArguments();
            ((TextView) rootView.findViewById(android.R.id.text1)).setText(
                    getString(R.string.dummy_section_text, args.getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A fragment that launches other parts of the demo application.
     */
    public static class Reward extends MainActivityFragment {

        @Override
        public int getPageTitleResID() {
            return R.string.reward_title;
        }

        private AdReward mAdReward;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
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
                mAdReward = new AdReward(
                        getActivity()
                        , mAdProfile
                        , AdRewardType.REWARD);
            } else {
                mAdReward = new AdReward(
                        getActivity()
                        , "5630c874cef2370b13942b8f"
                        , "placement(reward_video)"
                        , AdRewardType.REWARD_VIDEO);
                mAdReward.setTestMode(true);
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_reward, container, false);

            rootView.findViewById(R.id.reward_video)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {
                            /**
                             * Users are also capable of using {@link com.core.adnsdk.AdRewardListenerAdapter}, default adapter design pattern of AdRewardListener, to receive notification.
                             * Therefore, users can focus on specific events they care about.
                             */
                            mAdReward.setAdListener(new AdRewardListener() {
                                @Override
                                public void onAdLoaded(AdObject obj) {
                                    Log.d(TAG, "onAdLoaded(" + obj + ")");
                                    mAdReward.showAd();
                                }

                                @Override
                                public void onError(ErrorMessage err) {
                                    Log.d(TAG, "onError : " + err + ")");
                                    Toast.makeText(view.getContext(), "Error: " + err, Toast.LENGTH_LONG).show();
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

                                @Override
                                public String onAdRewarded(AdReward.RewardInfo rewardInfo) {
                                    Log.d(TAG, "onAdRewarded.");
                                    Toast.makeText(view.getContext(), "Got reward: currency: " + rewardInfo.getCurrency() + ", amount: " + rewardInfo.getAmount(), Toast.LENGTH_LONG).show();
                                    return null;
                                }

                                @Override
                                public void onAdReplayed() {
                                    Log.d(TAG, "onAdReplayed.");
                                }

                                @Override
                                public void onAdClosed() {
                                    Log.d(TAG, "onAdClosed.");
                                }
                            });
                            mAdReward.loadAd();
                        }
                    });

            return rootView;
        }

        @Override
        public void onResume() {
            if (mAdReward != null) {
                mAdReward.onResume();
            }
            super.onResume();
        }

        @Override
        public void onPause() {
            if (mAdReward != null) {
                mAdReward.onPause();
            }
            super.onPause();
        }

        @Override
        public void onDestroy() {
            if (mAdReward != null) {
                mAdReward.onDestroy();
                mAdReward = null;
            }
            super.onDestroy();
        }
    }

    /**
     * A fragment that launches other parts of the demo application.
     */
    public static class Other extends MainActivityFragment {

        @Override
        public int getPageTitleResID() {
            return R.string.other_title;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_other, container, false);

            rootView.findViewById(R.id.admob_mediation)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), ExampleMediation.class);
                            intent.putExtra("type", "AdMob");
                            startActivity(intent);
                        }
                    });

            rootView.findViewById(R.id.native_recyclerview_mopub)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), ExampleRecyclerViewMoPub.class);
                            intent.putExtra("type", "MoPub");
                            startActivity(intent);
                        }
                    });

            return rootView;
        }
    }

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    private void checkRequiredPermissions() {
        List<String> permissionsNeeded = new ArrayList<>();

        final List<String> permissionsList = new ArrayList<>();
        if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE)) {
            permissionsNeeded.add("Read Phone State");
        }
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissionsNeeded.add("GPS(Fine Location)");
        }
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            permissionsNeeded.add("GPS(Coarse Location)");
        }
        if (VersionUtils.hasJellyBean() && !addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            permissionsNeeded.add("Read External Storage");
        }
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            permissionsNeeded.add("Write External Storage");
        }

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++) {
                    message = message + ", " + permissionsNeeded.get(i);
                }
                showMessageOKCancel(message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
                                REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                    }
                });
                return;
            }
            ActivityCompat.requestPermissions(MainActivity.this, permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }

        okayed();
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            // https://inthecheesefactory.com/blog/things-you-need-to-know-about-android-m-permission-developer-edition/en
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();
                // Initial
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                if (VersionUtils.hasJellyBean()) {
                    perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                }
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++) {
                    perms.put(permissions[i], grantResults[i]);
                }
                // Check for ACCESS_FINE_LOCATION
                boolean isAllGranted = true;
                for (String key : perms.keySet()) {
                    if (perms.get(key) != PackageManager.PERMISSION_GRANTED) {
                        isAllGranted = false;
                        break;
                    }
                }
                if (isAllGranted) {
                    // All Permissions Granted
                    okayed();
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                    // Some Permissions Denied
                    failed();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void okayed() {

    }

    private void failed() {
        finish();
    }
}
