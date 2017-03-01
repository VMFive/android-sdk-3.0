# Android-SDK
- [Introduction](https://github.com/VMFive/android-sdk-3.0/blob/master/README_EN.md#introduction) 
- [Import SDK](https://github.com/VMFive/android-sdk-3.0/blob/master/README_EN.md#import-sdk)
  - [Android Studio](https://github.com/VMFive/android-sdk-3.0/blob/master/README_EN.md#android-studio)
  - [Eclipse](https://github.com/VMFive/android-sdk-3.0/blob/master/README_EN.md#eclipse)
- [Modify AndroidManifest.xml](https://github.com/VMFive/android-sdk-3.0/blob/master/README_EN.md#update-androidmanifestxml)
- [Ad Format](https://github.com/VMFive/android-sdk-3.0/blob/master/README_EN.md#ad-format)
  - [Video Card](https://github.com/VMFive/android-sdk-3.0/blob/master/README_EN.md#native-video-card)
    * [Layout](https://github.com/VMFive/android-sdk-3.0/blob/master/README_EN.md#layout)
    * [Load Native Ads](https://github.com/VMFive/android-sdk-3.0/blob/master/README_EN.md#load-and-show-ad)
  - [Insert Native Ads in ListView/RecyclerView ](https://github.com/VMFive/android-sdk-3.0/blob/master/README_EN.md#native-ad-in-listview-and-recyclerview)
  - [Video Interstitial](https://github.com/VMFive/android-sdk-3.0/blob/master/README_EN.md#interstitial)
  - [Reward Video](https://github.com/VMFive/android-sdk-3.0/blob/master/README_EN.md#reward-video)
- [Custom Render Renderer](https://github.com/VMFive/android-sdk-3.0/blob/master/README_EN.md#customized-renderer)
- [Mediation](https://github.com/VMFive/android-sdk-3.0/blob/master/README_EN.md#mediation)
  - [AdMob](https://github.com/VMFive/android-sdk-3.0/blob/master/README_EN.md#admob)
  - [DFP](https://github.com/VMFive/android-sdk-3.0/blob/master/README_EN.md#dfp)
  - [MoPub](https://github.com/VMFive/android-sdk-3.0/blob/master/README_EN.md#mopub)
- [Unity](https://github.com/VMFive/android-sdk-3.0/blob/master/README_EN.md#unity)
  - [Reward Video](https://github.com/VMFive/android-sdk-3.0/blob/master/README_EN.md#reward-video)
  - [Video Card](https://github.com/VMFive/android-sdk-3.0/blob/master/README_EN.md#video-card)
- [FAQ](https://github.com/VMFive/android-sdk-3.0/blob/master/README_EN.md#faq)


## Introduction
Native Ads do not require fixed layout and sizes. Application developers arrange the ad layout that best fit into the user interface.

Native Ads **bring more revenue and friendly user experience** then typical banners.

Besides the place developers pf banners，developers can place ads in more places to **create more revenue**

<img src="https://github.com/applauseadn/android-sdk-3.0/blob/master/images/Video_Banner.png?raw=true" alt="Video_Banner" width="216" height="384"">
<img src="https://github.com/applauseadn/android-sdk-3.0/blob/master/images/Video_Interstitial.png?raw=true" alt="Video_Interstitial" width="216" height="384">
<img src="https://github.com/applauseadn/android-sdk-3.0/blob/master/images/Video_Card.png?raw=true" alt="Video_Card" width="216" height="384">
<img src="https://github.com/applauseadn/android-sdk-3.0/blob/master/images/Video_Native.png?raw=true" alt="Video_Native" width="216" height="384">

## Import SDK
#### Android Studio
----

* Automatic
    1. Modify ```build.gradle``` to import ```VMFiveADNSDK Maven config```, ```Google GMS```，your ```build.gradle``` should look like this：
    
        ```java
        android {
            repositories {
                maven {
                    url 'https://raw.githubusercontent.com/VMFive/android-sdk-3.0/master/VMFiveADNSDK/'
                }
            }
        }
        configurations.all {
            resolutionStrategy.cacheDynamicVersionsFor 0, 'seconds'
        }
        dependencies {
            compile fileTree(dir: 'libs', include: ['*.jar'])
            ...
            compile 'com.google.android.gms:play-services-ads:8.4.0'
            debugCompile 'com.vmfive:VMFiveADNSDK:+:debug@aar'
            releaseCompile 'com.vmfive:VMFiveADNSDK:+:release@aar'
        }
        ```
    
    > If developers concern about updating gradle dependencies might slow down compiling speed  , you can remove ```configurations.all``` , but due to Gradle's Cache, it takes a while to update dependencies, If you see the error message below, you can use ```./gradlew --refresh-dependencies``` to update automatically
    
        06-21 18:18:13.568 13385-13419/com.core.vmfiveadnetwork E/CentralManager: Please update ADN SDK to the most updated version!
        06-21 18:18:13.568 13385-13419/com.core.vmfiveadnetwork E/CentralManager: Current SDK version: 3.0.0
        06-21 18:18:13.568 13385-13419/com.core.vmfiveadnetwork E/CentralManager: The most updated SDK version: 3.0.1
    
    > You can also assign a[specific versiom](https://github.com/VMFive/android-sdk-3.0/tree/master/VMFiveADNSDK/com/vmfive/VMFiveADNSDK), ```'com.vmfive:VMFiveADNSDK:3.0.0:debug@aar' ```

* Manual
    1. [Download the latest SDK](https://github.com/VMFive/android-sdk-3.0/releases)
    2. Use ```Module Dependency``` in the menu to import the SDK  and check ```Gradle```  <TODO -這部分可以再說清楚一點>
    3. Modify ```build.gradle``` 引入 ```Google GMS```，your ```build.gradle``` should look like this：
    
        ```java
        dependencies {
            compile fileTree(dir: 'libs', include: ['*.jar'])
            ...
            compile 'com.google.android.gms:play-services-ads:8.4.0'
        }
        ```

#### Eclipse
----

1. [Download the latest SDK:jar](https://github.com/VMFive/android-sdk-3.0/tree/master/VMFiveADNSDK)
2. Drag the jar into ```libs``` folder
3. Import ```Google Play Service```  ```library project```

## Update AndroidManifest.xml
1. Modify ```AndroidManifest.xml``` to declare permissions and activity required for SDK

    ```java
    //Required permissions
    <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    ```

2.  ```Google GMS Activity``` , ```Meta-data```,and ```ExpandFullScreenActivity``` are required for SDK

    ```java
    <activity
        android:name="com.google.android.gms.ads.AdActivity"
        android:configChanges="keyboard|keyboardHidden|orientation|screenLayout|uiMode|screenSize|smallestScreenSize"
        android:hardwareAccelerated="true"/>
    <meta-data
        android:name="com.google.android.gms.version"
        android:value="@integer/google_play_services_version"/>
    <activity
            android:name="com.core.adnsdk.ExpandScreenVideoActivity"
            android:configChanges="keyboard|keyboardHidden|orientation|screenLayout|uiMode|screenSize|smallestScreenSize"
            android:hardwareAccelerated="true">
    </activity>
    ```
    
## Ad Format
#### Native Video Card
<p align="center">
<img src="https://github.com/applauseadn/android-sdk-3.0/blob/master/images/Video_Card.png?raw=true" alt="Video_Card" width="216" height="384">
</p>

###### Layout
------

You can apply the sample layout [```card_ad_item.xml```](https://github.com/VMFive/android-sdk-3.0/blob/master/VMFiveAdNetwork/app/src/main/res/layout/card_ad_item.xml) ，but for user experience and ad performance reason，**We strongly suggest arrange the layout to fit in your user interface perfectly**。
<TODO - Layout example>

For text and image creatives,  ```TextView``` and ```ImageView``` will do，but**you must use ```com.core.adnsdk.VideoPlayer``` for video playbacks**。

Also, you need to set up - ```Background``` property for CTA text，**For better Advertising effect and get more revenue, we suggest set this property**。
In our sample App ,[```native_video_cta_border.xml```](https://github.com/VMFive/android-sdk-3.0/blob/master/VMFiveAdNetwork/app/src/main/res/drawable/native_video_cta_border.xml) we add border lines for ```CTA Text``` and assign```android:background="@drawable/native_video_cta_border"```。

###### Sample code
---
Full sample code :[```ExampleCard.java```](https://github.com/VMFive/android-sdk-3.0/blob/master/VMFiveAdNetwork/app/src/main/java/com/core/vmfiveadnetwork/ExampleCard.java)

###### Load and show ad
---

Import the following objects
```java
import com.core.adnsdk.AdCardType;
import com.core.adnsdk.AdCardView;
import com.core.adnsdk.AdListener;
import com.core.adnsdk.AdObject;
import com.core.adnsdk.AdView;
import com.core.adnsdk.AdViewType;
import com.core.adnsdk.CardAdRenderer;
import com.core.adnsdk.CardViewBinder;
import com.core.adnsdk.ErrorMessage;
```

1. Create a  ```CardViewBinder``` ，and assign the UI view for each ad creative <TODO -上下交換順序，然後補充是在 onCreate 裡面加入>
    * ```public final Builder loadingId(final int loadingId)```：Assign the view to render loading image
    * ```public final Builder titleId(final int titleId)```：Assign the view to render title 
    * ```public final Builder subTitleId(final int subTitleId)```：Assign the view to render subtitle text
    * ```public final Builder descriptionId(final int descriptionId)```：Assign the view to render description text
    * ```public final Builder videoPlayerId(final int videoPlayerId)```：Assign the view to render video player
    * ```public final Builder iconImageId(final int iconImageId)```：Assign the view to render icon
    * ```public final Builder mainImageId(final int mainImageId)```：Assign the view to render main image
    * ```public final Builder callToActionId(final int callToActionId)```：Assign the view to render CTA text
    * ```public final Builder countDownId(final int countDownId)```：Assign the view to render timer
    
    Example：
    ```java
    // native video layout builder
    CardViewBinder binder = new CardViewBinder.Builder(R.layout.card_ad_item)
        .loadingId(R.id.native_loading_image) 
        .mainImageId(R.id.native_main_image) 
        .titleId(R.id.native_title) 
        .videoPlayerId(R.id.native_video_layout) 
        .iconImageId(R.id.native_icon_image)
        .callToActionId(R.id.native_cta) 
        .countDownId(R.id.native_count_down)
        .build();
    ```
    
2. Create ```CardAdRenderer``` object using a  ```CardViewBinder```

    ```java
    // set layout builder to renderer
    CardAdRenderer adRenderer = new CardAdRenderer(binder);
    ```
    
3. Create ```AdCardView``` Object

    * ```activity```：Activity context
    * ```apikey``` : A unique string 
    * ```placement```：A unique string
    * ```adCardType```: Ad type
    
    **Please confirm apiKey and placement are correct**
    
    Example：
    ```java
    final AdCardType adCardType = AdCardType.CARD_VIDEO;
    mAdCardView = new AdCardView(this, "5630c874cef2370b13942b8f", "placement(card_video)", adCardType);
    ```
    
4. Implement ```AdListener```：

    ```java
    public interface AdListener {
        void onAdLoaded(AdObject adObject); // Complete loading Ad
        void onError(ErrorMessage err); //SDK Error
        void onAdClicked(); //Ad is clicked
        void onAdFinished(); //Ad is clicked and  redirected to landing page
        void onAdReleased(); //Ad Released memory
        boolean onAdWatched(); //Video end. return true if you want to automatically load the next Ad.
        void onAdImpressed(); //Impression counted
    }
    ```
    > We suggest implement AdListenerAdapter. You can implement just the callbacks you need. 
    
5. Test mode
   When test mode is on, SDK will always load test campaigns. **After finishing testing, please turn off test mode。(set to false )**

    ```java
    mAdCardView.setTestMode(true)
    ```
6. Set Render and ViewGroup:
   	Before loading Ad, you have to assign the ViewGroup where the Ad will be displayed and  
   	```java
   	ViewGroup adLayout = (ViewGroup) findViewById(R.id.example_adlayout);
   	mAdCardView.setViewParent(adLayout);
    mAdCardView.setAdRenderer(adRenderer); 
    ```
    **SDK will use default render if no render is assigned**
    
7. Call ```loadAd``` 

    ```java
    mAdCardView.loadAd();
    ```

8. In ```LifeCycle``` functions，call ```AdView's```  ```LifeCycle``` functions accordingly to avoid memory leak.

    ```java
    @Override
    protected void onResume() {
        if (mAdCardView != null) {
            mAdCardView.onResume();
        }
        super.onResume();
    }
      
    @Override
    protected void onPause() {
        if (mAdCardView != null) {
            mAdCardView.onPause();
        }
        super.onPause();
    }
      
    @Override
    protected void onDestroy() {
        if (mAdCardView != null) {
            mAdCardView.onDestroy();
            mAdCardView = null;
        }
        super.onDestroy();
    }
    ```
    
#### Native Ad in ListView and RecyclerView 
<p align="center">
<img src="https://github.com/applauseadn/android-sdk-3.0/blob/master/images/Video_Native.png?raw=true" alt="Video_Native" width="216" height="384">
</p>

###### Sample code
---
Full sample code :
[```ExampleListView.java```](https://github.com/VMFive/android-sdk-3.0/blob/master/VMFiveAdNetwork/app/src/main/java/com/core/vmfiveadnetwork/ExampleListView.java), [```ExampleRecyclerView.java```](https://github.com/VMFive/android-sdk-3.0/blob/master/VMFiveAdNetwork/app/src/main/java/com/core/vmfiveadnetwork/ExampleRecyclerView.java), We will use ListView as an example to demonstrate

###### Load and show ad
---
```java
import com.core.adnsdk.AdObject;
import com.core.adnsdk.CardAdRenderer;
import com.core.adnsdk.CardViewBinder;
import com.core.adnsdk.ErrorMessage;
import com.core.adnsdk.AdViewType;
import com.core.adnsdk.NativeAdAdapter;
import com.core.adnsdk.AdPoolListener;
```

1. Create ```CardViewBinder``` and ```CardViewBinder```

    Example：
    ```java
    CardViewBinder binder = new CardViewBinder.Builder(R.layout.card_ad_item)
        .loadingId(R.id.native_loading_image)
        .mainImageId(R.id.native_main_image)
        .titleId(R.id.native_title)
        .videoPlayerId(R.id.native_video_layout)
        .iconImageId(R.id.native_icon_image)
        .callToActionId(R.id.native_cta)
        .countDownId(R.id.native_count_down)
        .build();
    ```
2. Create ```CardAdRenderer``` using ```CardViewBinder``` 

    ```java
    CardAdRenderer adRenderer = new CardAdRenderer(binder);
    ```
    
3. Create ```NativeAdAdapter``` object

    ```java
    public NativeAdAdapter(final Activity activity, final ListView listView, final Adapter originalAdapter, final String placement)
    ```
    * ```activity```：Activity context
    * ```listView```：The target list view
    * ```originalAdapter```：The target adapter 
    * ```apikey``` : A unique string
    * ```placement```：A unique string
    * ```adViewType```：Ad type 
    
    **Please make sure apiKey and placement are correct**
    
    Example：
    ```java
    final AdViewType adViewType = AdViewType.CARD_VIDEO;
    mNativeAdAdapter = new NativeAdAdapter(this, listView, originalAdapter, "5630c874cef2370b13942b8f", "placement(list)", adViewType);
    ```

4. Implement ```AdPoolListener```， ```AdPoolListener``` has an extra parameter ```index``` to indicate the Ad position

    ```java
    public interface AdPoolListener {
        void onAdLoaded(int index, AdObject adObject); // Complete loading Ad
        void onError(int index, ErrorMessage err); // SDK error
        void onAdClicked(int index); // Ad is clicked
        void onAdFinished(int index); //Ad is redirected to landing page
        void onAdReleased(int index); //All memory released
        boolean onAdWatched(int index); //Video ended. if you want to load next ad, return true.
        void onAdImpressed(int index); //Impression counted
    }
    ```
    > We suggest implement AdPoolListenerAdapter.

5. Test Mode

    When test mode is on, only test campaigns will be loaded. **Turn off test mode after finishing tests **

    ```java
    mNativeAdAdapter.setTestMode(true)
    ```
6. Set AdRender

    ```java
    mNativeAdAdapter.setAdRenderer(adRenderer, AdViewType.CARD_VIDEO); // for Video type
    ```

7. In ```LifeCycle``` functions，call```AdView LifeCycle``` accordingly to avoid memory leak

    ```java
    @Override
    public void onResume() {
        if (mAdAdapmNativeAdAdapterter != null) {
            mNativeAdAdapter.onResume();
        }
        super.onResume();
    }
    
    @Override
    public void onPause() {
        if (mNativeAdAdapter != null) {
            mNativeAdAdapter.onPause();
        }
        super.onPause();
    }
    
    @Override
    public void onDestroy() {
        if (mNativeAdAdapter != null) {
            mNativeAdAdapter.onDestroy();
            mNativeAdAdapter = null;
        }
        super.onDestroy();
    }
    ```

#### Interstitial
<p align="center">
<img src="https://github.com/VMFive/android-sdk-3.0/blob/master/images/Video_Interstitial.png?raw=true" alt="Video_Interstitial" width="216" height="384">
</p>

###### Full Sample Code
---
Full sample code [```MainActivity.java Interstitial fragment```](https://github.com/VMFive/android-sdk-3.0/blob/master/VMFiveAdNetwork/app/src/main/java/com/core/vmfiveadnetwork/MainActivity.java#L342)

###### Load and show ad
---
1. In ```AndroidManifest.xml```, declare  ```Actitivity ```
  
  
    ```java
    <activity
        android:name="com.core.adnsdk.InterstitialActivity"
        android:configChanges="keyboard|keyboardHidden|orientation|screenLayout|uiMode|screenSize|smallestScreenSize"
        android:hardwareAccelerated="true"
        android:theme="@style/Theme.Transparent">
    </activity>
    ```
    
    * style/Theme.Transparent
    
    ```java
    <resources>

        <!--
            Base application theme, dependent on API level. This theme is replaced
            by AppBaseTheme from res/values-vXX/styles.xml on newer devices.
        -->
        <style name="AppBaseTheme" parent="android:Theme.Light">
            <!--
                Theme customizations available in newer API levels can go in
                res/values-vXX/styles.xml, while customizations related to
                backward-compatibility can go here.
            -->
        </style>
    
        <!-- Application theme. -->
        <style name="AppTheme" parent="AppBaseTheme">
            <!-- All customizations that are NOT specific to a particular API-level can go here. -->
        </style>
    
        <style name="Theme.Transparent" parent="android:Theme">
            <item name="android:windowIsTranslucent">true</item>
            <item name="android:windowBackground">@android:color/transparent</item>
            <item name="android:windowContentOverlay">@null</item>
            <item name="android:windowNoTitle">true</item>
            <item name="android:windowIsFloating">true</item>
            <item name="android:backgroundDimEnabled">false</item>
        </style>
    
    </resources>
    ```
    
    * Landscape ```Activity```:
  
    ```java
    <activity
        android:name="com.core.adnsdk.FullScreenVideoActivity"
        android:configChanges="keyboard|keyboardHidden|orientation|screenLayout|uiMode|screenSize|smallestScreenSize"
        android:screenOrientation="sensorLandscape"
        android:hardwareAccelerated="true">
    </activity>
    ```
2. Create ```AdInterstitial```  Object

  
    
    * ```activity```：Activity context
    * ```apikey``` : Unique String
    * ```placement```：Unique String
    * ```adInterstitialType```：AdInterstitialType.INTERSTITIAL_VIDEO
      
    ```java
    mAdInterstitial  = new AdInterstitial(this,
                "5630c874cef2370b13942b8f",
                "placement(interstitial_view)",
                AdInterstitialType.INTERSTITIAL_VIDEO);
    ```

3. Test mode - Turn on test mode when testing.**After finishing tests, turn off test mode**

    ```mAdInterstitial.setTestMode(true); ```

4. Implement ```AdListener()```

    ```java
    public interface AdListener {
        void onAdLoaded(AdObject adObject); // Ad loading complete
        void onError(ErrorMessage err); // SDK error
        void onAdClicked(); //Ad is clicked
        void onAdFinished(); //Ad is finished
        void onAdReleased(); //Ad memory released
        boolean onAdWatched(); //Video ended. Return true if you want to load next Ad
        void onAdImpressed(); //Impression counted
    }
    ```
    > We suggest implement AdListenerAdapter
5. Call loadAd()

    ```mAdInterstitial.loadAd(); ```
    
6. Call showAd in onAdLoaded callback

    ```mAdInterstitial.showAd(); ```

7. Handle interstitial Life Cycle，release memory

    ```java
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
    ```

#### Reward Video 
<p align="center">
<img src="https://github.com/applauseadn/android-sdk-3.0/blob/master/images/Video_Reward_Front.png?raw=true" alt="Video_Reward" width="216" height="384">
<img src="https://github.com/applauseadn/android-sdk-3.0/blob/master/images/Video_Reward_Back.png?raw=true" alt="Video_Reward" width="216" height="384">
</p>

###### Sample Code
---
Full Sample code [```MainActivity.java Reward fragment```](https://github.com/VMFive/android-sdk-3.0/blob/master/VMFiveAdNetwork/app/src/main/java/com/core/vmfiveadnetwork/MainActivity.java#L522)

###### Load and Show Ad
---
1. In```AndroidManifest.xml``` declare ```Actitivity ```
    * Landscape ```Activity```：
  
    ```java
    <activity
        android:name="com.core.adnsdk.RewardActivity"
        android:configChanges="keyboard|keyboardHidden|orientation|screenLayout|uiMode|screenSize|smallestScreenSize"
        android:screenOrientation="sensorLandscape"
        android:hardwareAccelerated="true">
    </activity>
    ```

2. Create ```mAdReward``` object

    **AdRewardType.REWARD_VIDEO**
    
    * ```activity```：Activity context
    * ```apikey``` : A unique string
    * ```placement```：A unique string
    * ```adRewardType```：AdRewardType.REWARD_VIDEO
    
    ```java
   mAdReward = new AdReward(
                    getActivity(),
                    "5630c874cef2370b13942b8f",
                    "placement(reward_video)",
                    AdRewardType.REWARD_VIDEO);
    ```

3. Test Mode - Turn on when testing，**After finishing testing, please turn off test mode.**

    ```mAdReward.setTestMode(true); ```

4. Implement ```AdListener()```

    ```java
    public interface AdRewardListener {
        void onAdLoaded(AdObject adObject); // Ad is loaded
        void onError(ErrorMessage err); // SDK error
        void onAdClicked(); //Ad is clicked
        void onAdFinished(); //Ad is redirected to landing page
        void onAdReleased(); //Memory rleased
        boolean onAdWatched(); //Video ended. If you want to load next ad, return true.
        void onAdImpressed(); //Impression counted
        String onAdRewarded(AdReward.RewardInfo rewardInfo); //Video end, give the reward here.
        void onAdReplayed(); //Video is replayed
        void onAdClosed(); //Video is closed
    }
    ```
    > We suggest implement AdRewardListenerAdapter

5. Call loadAd()，SDK will call ```onAdLoaded``` when the ad is loaded.

    ```mAdReward.loadAd(); ```
    
6. Call showAd() in onAdLoaded()

    ```mAdReward.showAd(); ```
 
    ```String onAdRewarded(AdReward.RewardInfo rewardInfo)```
  
  > You can use onAdRewarded to interact with your backend and give rewards.
   
7. Handle Life Cycle，and release memory

    ```java
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
    ```

## Customized Renderer

  You can modify [CustomCardAdRenderer.java](https://github.com/VMFive/android-sdk-3.0/blob/master/VMFiveAdNetwork/app/src/main/java/com/core/vmfiveadnetwork/renderer/CustomCardAdRenderer.java) and use this renderer.
  
  You can use customized render to
  
  1. Disable full screen button
  ```java
  videoLayout.setExpandEnabled(false);
  ```

  2. Disable timer
  ```java
  videoLayout.setCountDownEnabled(false);
  ```

## Mediation

  Video interstitial supports DFP,AdMob, and Mopub.
  Native video ad only supports Mopub 

#### AdMob
----
  * [Banner](https://github.com/VMFive/android-sdk-3.0/blob/master/VMFiveAdNetwork/app/src/main/java/com/core/vmfiveadnetwork/AdMobBanner.java)
  * [Interstitial](https://github.com/VMFive/android-sdk-3.0/blob/master/VMFiveAdNetwork/app/src/main/java/com/core/vmfiveadnetwork/AdMobInterstitial.java)

#### DFP
----
  Same with AdMob 
  * [Banner](https://github.com/VMFive/android-sdk-3.0/blob/master/VMFiveAdNetwork/app/src/main/java/com/core/vmfiveadnetwork/AdMobBanner.java)
  * [Interstitial](https://github.com/VMFive/android-sdk-3.0/blob/master/VMFiveAdNetwork/app/src/main/java/com/core/vmfiveadnetwork/AdMobInterstitial.java)

#### MoPub
----
  **Use 3.0.5 and higher version VM5 SDK for MoPub**
  
1. Mopub data format
  
  > Data: {"test": 1, "apiKey": "5630c874cef2370b13942b8f", "placement": "placement(native_mopub)"}
  
2. MopPub Integration Sample Code

  * [Banner](https://github.com/VMFive/android-sdk-3.0/blob/master/VMFiveMoPubAdapter/src/VM5Banner.java)
  * [Interstitial](https://github.com/VMFive/android-sdk-3.0/blob/master/VMFiveMoPubAdapter/src/VM5Interstitial.java)
  * [Native Static](https://github.com/VMFive/android-sdk-3.0/blob/master/VMFiveMoPubAdapter/src/VM5NativeStatic.java)
  * [Native Video](https://github.com/VMFive/android-sdk-3.0/blob/master/VMFiveMoPubAdapter/src/VM5NativeVideo.java)
  * [Reward Video](https://github.com/VMFive/android-sdk-3.0/blob/master/VMFiveMoPubAdapter/src/VM5Reward.java)

3. Customized Render

	Mopub suppots customized render. You don't have to use MoPubVideoNativeAdRenderer. We suggest use VM5MoPubVideoNativeAdRenderer to make ad more beautiful.
  
    ```java
    // Set up a renderer for a video native ad.
    videoAdRenderer = new VM5MoPubVideoNativeAdRenderer(
            new VM5MediaViewBinder.Builder(R.layout.vm5_video_ad_list_item)
                    .titleId(R.id.native_title)
                    .textId(R.id.native_text)
                    .mediaLayoutId(R.id.native_media_layout)
                    .iconImageId(R.id.native_icon_image)
                    .callToActionId(R.id.native_cta)
                    .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                    .build());

    // Register the renderers with the MoPubAdAdapter and then set the adapter on the ListView.
    mAdAdapter.registerAdRenderer(videoAdRenderer);
    ```
  
## Unity

#### Reward Video
----
  * [Reward](https://github.com/VMFive/android-sdk-3.0/tree/master/VMFiveUnity#串接-reward)
  
#### Video Card
----
  * [Card](https://github.com/VMFive/android-sdk-3.0/blob/master/VMFiveUnity#串接-card)

## FAQ
1. CoverImage is displayed, but black screen when video is playing.

    ``` java
    /**
     * Make sure in AndroidManifest.xml, hardwareAccelerated is enabled in application/activity context
    */
    android:hardwareAccelerated="true"
    ```
    
2. Ad creatives cannot display

    ``` java
    /**
     * Make sure AndroidManifest.xml, read/write storage permissions is declared
    */
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    ```
    > If your Application is above Android M(23), you need request permission dialog, 
    [VMFiveAdNetwork Demo App's implementation](https://github.com/VMFive/android-sdk-3.0/blob/master/VMFiveAdNetwork/app/src/main/java/com/core/vmfiveadnetwork/MainActivity.java)
    ``` java
    // check permissions for M, if some permission denied, it would shut down activity
    checkRequiredPermissions();
    ```
    
    > If the issue is still there,probably is because the user disables the permission. Go to Settings / Application Info / your package name / read or write permission to activate

3. How to  print log in release mode SDK?
    ``` java
    adb logcat -c
    adb shell setprop log.tag.adnsdk VERBOSE
    adb logcat > adnsdk.log
    ```

    > Logs are default shown in debug mode SDK
    
4. Collect data about Apps installed on client device

    If you're willing to collect data for us, we will share more revenue with you. You need to use a SDK built in dialog to inform users.[reference: MainActivity.java](https://github.com/VMFive/android-sdk-3.0/blob/master/VMFiveAdNetwork/app/src/main/java/com/core/vmfiveadnetwork/MainActivity.java#L135)
    
    ```java
    // need to enable app scan in backend, and prompt this dialog to notice user
    ADN.showAppScanDialog(this, "Are you willing to let us to detect the apps you have in your phone?");
    ```

    If you have already asked for permissions, simply enable the app scan function.
    
    ```java
    ADN.setAppScanEnable(this, true);
    ADN.setAppScanConfigured(this, true);
    ```
VMFive
