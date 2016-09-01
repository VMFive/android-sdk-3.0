# Android-SDK
- [概論](https://github.com/applauseadn/android-sdk-3.0/blob/master/README.md#概論) 
- [導入 SDK](https://github.com/applauseadn/android-sdk-3.0/blob/master/README.md#導入-sdk)
  - [Android Studio](https://github.com/applauseadn/android-sdk-3.0/blob/master/README.md#android-studio)
  - [Eclipse](https://github.com/applauseadn/android-sdk-3.0/blob/master/README.md#eclipse)
- [更新 AndroidManifest.xml](https://github.com/applauseadn/android-sdk-3.0/blob/master/README.md#更新-androidmanifestxml)
- [初始化 SDK](https://github.com/applauseadn/android-sdk-3.0/blob/master/README.md#初始化sdk)
- [廣告格式](https://github.com/applauseadn/android-sdk-3.0/blob/master/README.md#廣告格式)
  - [卡片型原生影音廣告](https://github.com/applauseadn/android-sdk-3.0/blob/master/README.md#卡片型原生影音廣告)
    * [Layout](https://github.com/applauseadn/android-sdk-3.0/blob/master/README.md#layout)
    * [載入並且展示原生影音廣告](https://github.com/applauseadn/android-sdk-3.0/blob/master/README.md#載入並且展示原生影片廣告)
  - [ListView/RecyclerView 型原生影音廣告](https://github.com/applauseadn/android-sdk-3.0/blob/master/README.md#listviewrecyclerview-型原生影片廣告)
  - [影音插頁廣告(Interstitial)](https://github.com/applauseadn/android-sdk-3.0/blob/master/README.md#影音插頁廣告interstitial)
  - [獎勵型廣告(Reward)](https://github.com/applauseadn/android-sdk-3.0/blob/master/README.md#獎勵型廣告reward)
- [客製化 Renderer](https://github.com/applauseadn/android-sdk-3.0/blob/master/README.md#客製化-renderer)
- [輪播(Mediation)](https://github.com/applauseadn/android-sdk-3.0/blob/master/README.md#輪播mediation)
  - [AdMob](https://github.com/applauseadn/android-sdk-3.0/blob/master/README.md#admob)
  - [DFP](https://github.com/applauseadn/android-sdk-3.0/blob/master/README.md#dfp)
  - [MoPub](https://github.com/applauseadn/android-sdk-3.0/blob/master/README.md#mopub)
- [Unity](https://github.com/applauseadn/android-sdk-3.0/blob/master/README.md#unity)
  - [串接 Reward](https://github.com/applauseadn/android-sdk-3.0/blob/master/README.md#串接-reward)
  - [串接 Card](https://github.com/applauseadn/android-sdk-3.0/blob/master/README.md#串接-card)
- [問題排解](https://github.com/applauseadn/android-sdk-3.0/blob/master/README.md#問題排解)


## 概論
原生廣告沒有固定的規格大小，需要透過應用程式開發者的巧思將廣告的素材重新設計與編排後融合到使用者介面之中。

原生廣告帶來 **比傳統橫幅或是插頁廣告更友善的體驗與更好的成效**；

除此之外在原本的橫幅和插頁廣告版位外，可以在更多版位擺放廣告 **創造更多收入來源。**

<img src="https://github.com/applauseadn/android-sdk-3.0/blob/master/images/Video_Banner.png?raw=true" alt="Video_Banner" width="216" height="384"">
<img src="https://github.com/applauseadn/android-sdk-3.0/blob/master/images/Video_Interstitial.png?raw=true" alt="Video_Interstitial" width="216" height="384">
<img src="https://github.com/applauseadn/android-sdk-3.0/blob/master/images/Video_Card.png?raw=true" alt="Video_Card" width="216" height="384">
<img src="https://github.com/applauseadn/android-sdk-3.0/blob/master/images/Video_Native.png?raw=true" alt="Video_Native" width="216" height="384">

Applause SDK 3.0主要是因應新版廣告投放後台的效能與彈性提升而修改,提供更精準的投放提升廣告的成效與創造更多的收益.大部分的API命名與用法與2.0維持一致,已經串接2.0的開發者只需要小幅修改即可.

## 導入 SDK
#### Android Studio
----

* 自動
    1. 修改 ```build.gradle``` 引入 ```VMFiveADNSDK Maven config```, ```Google GMS```，您的 ```build.gradle``` 最後應該看起來類似這樣：
    
        ```java
        android {
            repositories {
                maven {
                    url 'https://raw.githubusercontent.com/applauseadn/android-sdk-3.0/master/VMFiveADNSDK/'
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
    
    > 若開發者擔心 gradle 自動更新 dependencies 會影響編譯速度, 可將 ```configurations.all``` 移除, 但因Gradle有Cache機制, 一段時間後才會更新 dependencies, 若發生底下 error message, 可使用 ```./gradlew --refresh-dependencies``` 強制更新
    
        06-21 18:18:13.568 13385-13419/com.core.vmfiveadnetwork E/CentralManager: Please update ADN SDK to the most updated version!
        06-21 18:18:13.568 13385-13419/com.core.vmfiveadnetwork E/CentralManager: Current SDK version: 3.0.0
        06-21 18:18:13.568 13385-13419/com.core.vmfiveadnetwork E/CentralManager: The most updated SDK version: 3.0.1
    
    > 也可使用[指定版本](https://github.com/applauseadn/android-sdk-3.0/tree/master/VMFiveADNSDK/com/vmfive/VMFiveADNSDK), ```'com.vmfive:VMFiveADNSDK:3.0.0:debug@aar' ```

* 手動
    1. [下載最新版 SDK](https://github.com/applauseadn/android-sdk-3.0/releases)
    2. 將 SDK 以新增 ```Module Dependency``` 的方式加入 ```Gradle```  <TODO -這部分可以再說清楚一點>
    3. 修改 ```build.gradle``` 引入 ```Google GMS```，您的 ```build.gradle``` 最後應該看起來類似這樣：
    
        ```java
        dependencies {
            compile fileTree(dir: 'libs', include: ['*.jar'])
            ...
            compile 'com.google.android.gms:play-services-ads:8.4.0'
        }
        ```

#### Eclipse
----

1. [下載最新版 SDK](https://github.com/applauseadn/android-sdk-3.0/releases)
2. 將 SDK 的 JAR 檔拖至 ```libs``` 目錄下
3. 加入 ```Google Play Service``` 的 ```library project```

## 更新 AndroidManifest.xml
1. 修改 ```AndroidManifest.xml``` 加入必要的權限

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

2. 加入 ```Google GMS Activity``` 和 ```Meta-data```以及因預設會有影片全屏播放功能, 需要在 AndroidManifest.xml 宣告 ```ExpandFullScreenActivity```

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
    
## 廣告格式
#### 卡片型原生影音廣告
<p align="center">
<img src="https://github.com/applauseadn/android-sdk-3.0/blob/master/images/Video_Card.png?raw=true" alt="Video_Card" width="216" height="384">
</p>
###### Layout
------
您可以直接套用範例專案中的 [```card_ad_item.xml```](https://github.com/applauseadn/android-sdk-3.0/blob/master/VMFiveAdNetwork/app/src/main/res/layout/card_ad_item.xml) ，但是為了使用者體驗以及廣告成效，**強烈建議您根據 app 排版自行設計適合的廣告排版**。
<TODO - Layout example>

文字和圖片等素材使用標準的 ```TextView``` 和 ```ImageView``` 呈現即可，但**用來播放影音廣告的元件請務必使用 ```com.core.adnsdk.VideoPlayer```**。

另外需要注意的是 -  CTA 文字需要設定 ```Background``` 屬性，**好提高廣告成效並取得更佳的分潤請務必使用**。
例如我們的範例 App 就在 [```native_video_cta_border.xml```](https://github.com/applauseadn/android-sdk-3.0/blob/master/VMFiveAdNetwork/app/src/main/res/drawable/native_video_cta_border.xml) 為 ```CTA Text``` 加上了邊框且指定 ```android:background="@drawable/native_video_cta_border"```。

###### 完整代碼演示
---
開始撰寫代碼之前，需要先引入以下的物件，完整的程式碼請參考 [```ExampleCard.java```](https://github.com/applauseadn/android-sdk-3.0/blob/master/VMFiveAdNetwork/app/src/main/java/com/core/vmfiveadnetwork/ExampleCard.java)

###### 載入並且展示廣告
---
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

1. 創建 ```CardViewBinder``` ，透過 ```CardViewBinder``` 指定廣告素材和 UI 元件的關係 <TODO -上下交換順序，然後補充是在 onCreate 裡面加入>
    * ```public final Builder loadingId(final int loadingId)```：綁定 Loading image 與 UI 元件
    * ```public final Builder titleId(final int titleId)```：綁定標題文字與 UI 元件
    * ```public final Builder subTitleId(final int subTitleId)```：綁定副標題文字與 UI 元件
    * ```public final Builder descriptionId(final int descriptionId)```：綁定描述文字與 UI 元件
    * ```public final Builder videoPlayerId(final int videoPlayerId)```：綁定影片與 Video Player
    * ```public final Builder iconImageId(final int iconImageId)```：綁定圖示與 UI 元件
    * ```public final Builder mainImageId(final int mainImageId)```：綁定圖片與 UI 元件
    * ```public final Builder callToActionId(final int callToActionId)```：綁定 CTA 文字與 UI 元件
    * ```public final Builder countDownId(final int countDownId)```：綁定倒數計時與 UI 元件
    
    範例：
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
    
2. 創建 ```CardAdRenderer``` 物件，利用上一步的 ```CardViewBinder```

    ```java
    // set layout builder to renderer
    CardAdRenderer adRenderer = new CardAdRenderer(binder);
    ```
    
3. 創建 ```AdCardView``` 物件

    * ```activity```：Activity context
    * ```apikey``` : 後台產生的獨特字串,用來請求廣告
    * ```placement```：按照placement命名規則產生的字串
    * ```adCardType```: 廣告類型,如AdCardType.CARD_VIDEO
    
    **apikey和placement請務必填入正確的值,否則無法載入廣告**
    
    範例：
    ```java
    final AdCardType adCardType = AdCardType.CARD_VIDEO;
    mAdCardView = new AdCardView(this, "5630c874cef2370b13942b8f", "placement(card_video)", adCardType);
    ```
    
4. 設定並且實作 ```AdListener```：

    ```java
    public interface AdListener {
        void onAdLoaded(AdObject adObject); // 廣告完成載入
        void onError(ErrorMessage err); // SDK出現錯誤
        void onAdClicked(); //廣告被點擊
        void onAdFinished(); //廣告點擊完成跳轉後
        void onAdReleased(); //廣告完成卸載並且釋放所有資源
        boolean onAdWatched(); //影片播放完畢,要自動載入下一檔廣告請回傳true,否則回傳false
        void onAdImpressed(); //廣告曝光
    }
    ```
    > 建議使用 AdListenerAdapter, 可以只實作一部份的 event callbacks
    
5. 設定測試模式
    當打開測試模式的時候，SDK 會接受到測試用的廣告。測試廣告並沒有分潤，因此**測試完成後 App 上線前請一定要關閉測試模式。(設成 false )**

    ```java
    mAdCardView.setTestMode(true)
    ```
6. 設定Render與ViewGroup:
   	在載入廣告之前,必須先指定Render以及要插入廣告的ViewGroup
   	```java
   	ViewGroup adLayout = (ViewGroup) findViewById(R.id.example_adlayout);
   	mAdCardView.setViewParent(adLayout);
    mAdCardView.setAdRenderer(adRenderer); 
    ```
    **如果沒有指定Render,則會使用SDK預設的廣告Layout**
    
7. 呼叫 ```loadAd``` 載入廣告

    ```java
    mAdCardView.loadAd();
    ```

8. 在 ```LifeCycle``` 的函式中，呼叫對應的 ```AdView``` 的 ```LifeCycle``` 方法避免內存洩漏

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
    
#### ListView/RecyclerView 型原生影片廣告
<p align="center">
<img src="https://github.com/applauseadn/android-sdk-3.0/blob/master/images/Video_Native.png?raw=true" alt="Video_Native" width="216" height="384">
</p>

###### 完整代碼演示
---
開始撰寫代碼之前，需要先引入以下的物件，完整的程式碼請參考 [```ExampleListView.java```](https://github.com/applauseadn/android-sdk-3.0/blob/master/VMFiveAdNetwork/app/src/main/java/com/core/vmfiveadnetwork/ExampleListView.java), [```ExampleRecyclerView.java```](https://github.com/applauseadn/android-sdk-3.0/blob/master/VMFiveAdNetwork/app/src/main/java/com/core/vmfiveadnetwork/ExampleRecyclerView.java), 以下舉 ListView 例子說明

###### 載入並且展示廣告
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

1. 新增一個 ```CardViewBinder``` 物件，將 ```Layout``` 裡的 UI 元件 id 透過 ```CardViewBinder``` 與綁定廣告素材的關聯與規則

    範例：
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
2. 建立一個 ```CardAdRenderer```，並且將定義好素材與排版關聯的 ```CardViewBinder``` 傳入

    ```java
    CardAdRenderer adRenderer = new CardAdRenderer(binder);
    ```
    
3. 建立一個 ```NativeAdAdapter``` 物件

    傳入 ```Activity``` ，要插入原生廣告的 ```ListView``` 和 ```Adapter``` ，以及一個任意的字串。這個字串會在後台顯示作為廣告版位的 TAG；當在很多不同的版位插入廣告的時候，就可以利用版位的 TAG 觀察與分析各個版位的廣告收益。

    ```java
    public NativeAdAdapter(final Activity activity, final ListView listView, final Adapter originalAdapter, final String placement)
    ```
    * ```activity```：Activity context
    * ```listView```：要插入原生廣告的 ListView
    * ```originalAdapter```：要插入原生廣告的 adapter
    * ```apikey``` : 一個用來請求廣告的獨特字串
    * ```placement```：依照placement命名規則產生的字串
    * ```adViewType```：廣告類型,如AdViewType.CARD_VIDEO
    
    **apikey和placement請務必填入正確的值,否則無法載入廣告**
    
    範例：
    ```java
    final AdViewType adViewType = AdViewType.CARD_VIDEO;
    mNativeAdAdapter = new NativeAdAdapter(this, listView, originalAdapter, "5630c874cef2370b13942b8f", "placement(list)", adViewType);
    ```

4. 設定與實作 ```AdPoolListener```， ```AdPoolListener``` 的事件會多帶一個 ```index``` 參數表示插入廣告的位置

    ```java
    public interface AdPoolListener {
        void onAdLoaded(int index, AdObject adObject); // 廣告完成載入
        void onError(int index, ErrorMessage err); // SDK出現錯誤
        void onAdClicked(int index); //廣告被點擊
        void onAdFinished(int index); //廣告點擊完成跳轉後
        void onAdReleased(int index); //廣告完成卸載並且釋放所有資源
        boolean onAdWatched(int index); //影片播放完畢,要自動載入下一檔廣告請回傳true
        void onAdImpressed(int index); //廣告曝光
    }
    ```
    > 建議使用 AdPoolListenerAdapter, 可以只實作一部份的 event callbacks

5. 設定測試模式
    當打開測試模式的時候，SDK 會接受到測試用的廣告。測試廣告並沒有分潤，因此**測試完成後 App 上線前請一定要關閉測試模式。(設成 false )**

    ```java
    mNativeAdAdapter.setTestMode(true)
    ```
6. 設定AdRender
    指定Render,如果沒有指定的話會使用SDK預設的廣告Layout 
    ```java
    mNativeAdAdapter.setAdRenderer(adRenderer, AdViewType.CARD_VIDEO); // for Video type
    ```

7. 在 ```LifeCycle``` 的函式中，呼叫對應的 ```AdView``` 的 ```LifeCycle``` 方法避免內存洩漏

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

#### 影音插頁廣告(Interstitial)
<p align="center">
<img src="https://github.com/applauseadn/android-sdk-3.0/blob/master/images/Video_Interstitial.png?raw=true" alt="Video_Interstitial" width="216" height="384">
</p>

###### 完整代碼演示
---
完整的程式碼請參考 [```MainActivity.java Interstitial fragment```](https://github.com/applauseadn/android-sdk-3.0/blob/master/VMFiveAdNetwork/app/src/main/java/com/core/vmfiveadnetwork/MainActivity.java#L342)

###### 載入並且展示廣告
---
1. 在開始撰寫程式碼之前,請先在 ```AndroidManifest.xml``` 中宣告插頁廣告的 ```Actitivity ```
    * 直屏的 ```Activity```：
  
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
    
    * 轉橫屏全螢幕播放的 ```Activity```:
  
    ```java
    <activity
        android:name="com.core.adnsdk.FullScreenVideoActivity"
        android:configChanges="keyboard|keyboardHidden|orientation|screenLayout|uiMode|screenSize|smallestScreenSize"
        android:screenOrientation="landscape"
        android:hardwareAccelerated="true">
    </activity>
    ```
2. 創建 ```AdInterstitial``` 物件，需要傳入四個參數: Context,placement字串,apikey字串以及指定廣告類型為

    ```AdInterstitialType.INTERSTITIAL_VIDEO ```
    
    * ```activity```：Activity context
    * ```apikey``` : 一個用來請求廣告的獨特字串
    * ```placement```：依照placement命名規則產生的字串
    * ```adInterstitialType```：廣告類型,如AdInterstitialType.INTERSTITIAL_VIDEO
      
    ```java
    mAdInterstitial  = new AdInterstitial(this,
                "5630c874cef2370b13942b8f",
                "placement(interstitial_view)",
                AdInterstitialType.INTERSTITIAL_VIDEO);
    ```

3. 設定測試模式 - 在測試時請開啟測試模式，**測試完成上線前請務必設定成 false 關閉測試模式以免無法取得分潤.**

    ```mAdInterstitial.setTestMode(true); ```

4. 實作 ```AdListener()```，各個 ```callback``` 的定義如下：

    ```java
    public interface AdListener {
        void onAdLoaded(AdObject adObject); // 廣告完成載入
        void onError(ErrorMessage err); // SDK 出現錯誤
        void onAdClicked(); //廣告被點擊
        void onAdFinished(); //廣告點擊完成跳轉後
        void onAdReleased(); //廣告完成卸載並且釋放所有資源
        boolean onAdWatched(); //影片播放完畢，要自動載入下一檔廣告請回傳 true，否則回傳 false
        void onAdImpressed(); //廣告曝光
    }
    ```
    > 建議使用 AdListenerAdapter, 可以只實作一部份的 event callbacks

5. 載入廣告，載入完成後 SDK 會呼叫 ```onAdLoaded```

    ```mAdInterstitial.loadAd(); ```
    
6. 確定廣告已經載入完成後(可用 ```onAdLoaded``` 追蹤)後，展示廣告

    ```mAdInterstitial.showAd(); ```

7. 處理插頁廣告的 Life Cycle，釋放資源

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

#### 獎勵型廣告(Reward)
<p align="center">
<img src="https://github.com/applauseadn/android-sdk-3.0/blob/master/images/Video_Reward_Front.png?raw=true" alt="Video_Reward" width="216" height="384">
<img src="https://github.com/applauseadn/android-sdk-3.0/blob/master/images/Video_Reward_Back.png?raw=true" alt="Video_Reward" width="216" height="384">
</p>

###### 完整代碼演示
---
完整的程式碼請參考 [```MainActivity.java Reward fragment```](https://github.com/applauseadn/android-sdk-3.0/blob/master/VMFiveAdNetwork/app/src/main/java/com/core/vmfiveadnetwork/MainActivity.java#L522)

###### 載入並且展示廣告
---
1. 在開始撰寫程式碼之前,請先在 ```AndroidManifest.xml``` 中宣告獎勵廣告的 ```Actitivity ```
    * 橫屏的 ```Activity```：
  
    ```java
    <activity
        android:name="com.core.adnsdk.RewardActivity"
        android:configChanges="keyboard|keyboardHidden|orientation|screenLayout|uiMode|screenSize|smallestScreenSize"
        android:screenOrientation="landscape"
        android:hardwareAccelerated="true">
    </activity>
    ```

2. 創建 ```mAdReward``` 物件，需要傳入四個參數: Context, 一個apikey字串 ,一個placement字串以及指定廣告類型為

    **AdRewardType.REWARD_VIDEO**
    
    * ```activity```：Activity context
    * ```apikey``` : 一個用來請求廣告的獨特字串
    * ```placement```：依照placement命名規則產生的字串
    * ```adRewardType```：廣告類型,如AdRewardType.REWARD_VIDEO
    
    ```java
   mAdReward = new AdReward(
                    getActivity(),
                    "5630c874cef2370b13942b8f",
                    "placement(reward_video)",
                    AdRewardType.REWARD_VIDEO);
    ```

3. 設定測試模式 - 在測試時請開啟測試模式，**測試完成上線前請務必設定成 false 關閉測試模式以免無法取得分潤.**

    ```mAdReward.setTestMode(true); ```

4. 實作 ```AdListener()```，各個 ```callback``` 的定義如下：

    獎勵型廣告可透過 onAdRewarded, onAdReplayed, onAdClosed callbacks 知道獎勵結果, 被重新播放, 或是關閉
    ```java
    public interface AdRewardListener {
        void onAdLoaded(AdObject adObject); // 廣告完成載入
        void onError(ErrorMessage err); // SDK 出現錯誤
        void onAdClicked(); //廣告被點擊
        void onAdFinished(); //廣告點擊完成跳轉後
        void onAdReleased(); //廣告完成卸載並且釋放所有資源
        boolean onAdWatched(); //影片播放完畢，要自動載入下一檔廣告請回傳 true，否則回傳 false
        void onAdImpressed(); //廣告曝光
        String onAdRewarded(AdReward.RewardInfo rewardInfo); //獎勵廣告
        void onAdReplayed(); //重新播放
        void onAdClosed(); //廣告被關閉
    }
    ```
    > 建議使用 AdRewardListenerAdapter, 可以只實作一部份的 event callbacks

5. 載入廣告，載入完成後 SDK 會呼叫 ```onAdLoaded```

    ```mAdReward.loadAd(); ```
    
6. 確定廣告已經載入完成後(可用 ```onAdLoaded``` 追蹤)後，展示廣告

    ```mAdReward.showAd(); ```
  廣告播放結束後會利用
    ```String onAdRewarded(AdReward.RewardInfo rewardInfo)```
  告知 
  > 使用者可透過 onAdRewarded callback 回傳值, 回傳給其後端相關的訊息
   
7. 處理獎勵廣告的 Life Cycle，釋放資源

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

## 客製化 Renderer

  以客製化 Card 廣告格式為例, 使用者可以修改 [CustomCardAdRenderer.java](https://github.com/applauseadn/android-sdk-3.0/blob/master/VMFiveAdNetwork/app/src/main/java/com/core/vmfiveadnetwork/renderer/CustomCardAdRenderer.java) 並將此 renderer 傳入
  
  使用者可以客製化 renderer, 例如動態創建廣告格式, 或是顯示動畫等
  
  1. 關閉全屏播放選項
  ```java
  videoLayout.setExpandEnabled(false);
  ```

  2. 關閉倒數計時選項
  ```java
  videoLayout.setCountDownEnabled(false);
  ```

## 輪播(Mediation)

  蓋版影音廣告(Video Interstitial)可支援AdMob,DFP,和Mopub輪播.原生影音廣告(Native Video Ad)目前只支援Mopub輪播. 串接前請記得在 AndroidManifest.xml 宣告權限, 及所需的 activity

#### AdMob
----
  * [Banner](https://github.com/applauseadn/android-sdk-3.0/blob/master/VMFiveAdNetwork/app/src/main/java/com/core/vmfiveadnetwork/AdMobBanner.java)
  * [Interstitial](https://github.com/applauseadn/android-sdk-3.0/blob/master/VMFiveAdNetwork/app/src/main/java/com/core/vmfiveadnetwork/AdMobInterstitial.java)

#### DFP
----
  DFP 代碼撰寫方式與 AdMob 相同
  * [Banner](https://github.com/applauseadn/android-sdk-3.0/blob/master/VMFiveAdNetwork/app/src/main/java/com/core/vmfiveadnetwork/AdMobBanner.java)
  * [Interstitial](https://github.com/applauseadn/android-sdk-3.0/blob/master/VMFiveAdNetwork/app/src/main/java/com/core/vmfiveadnetwork/AdMobInterstitial.java)

#### MoPub
----
  * [Banner](https://github.com/applauseadn/android-sdk-3.0/blob/master/VMFiveMoPubAdapter/src/VM5Banner.java)
  * [Interstitial](https://github.com/applauseadn/android-sdk-3.0/blob/master/VMFiveMoPubAdapter/src/VM5Interstitial.java)
  * [Native Static](https://github.com/applauseadn/android-sdk-3.0/blob/master/VMFiveMoPubAdapter/src/VM5NativeStatic.java)
  * [Native Video](https://github.com/applauseadn/android-sdk-3.0/blob/master/VMFiveMoPubAdapter/src/VM5NativeVideo.java)

## Unity

#### 串接 Reward
----
<p align="center">
<img src="https://github.com/applauseadn/android-sdk-3.0/blob/master/images/Unity_Import_Package.png?raw=true" alt="Video_Reward" width="640" height="400">
</p>

  [VMFiveADN_Reward.unitypackage](https://github.com/applauseadn/android-sdk-3.0/tree/master/VMFiveADN_UnityPackages)
  請按照圖示載入 Unity package, 並參考 CallJavaCode.cs 加上載入 AdReward 的代碼, 最後執行 Build & Run
  
  ```java
  private AdReward mAdReward = null;

	void Start() {
		mAdReward = new AdReward ("5630c874cef2370b13942b8f", "placement(reward_video)");
		MyAdRewardListener adRewardListener = new MyAdRewardListener (mAdReward);
		mAdReward.setListener (adRewardListener);
		mAdReward.setTestMode (true);
	}

	void OnGUI() {
		if(GUI.Button(new Rect(20,40,200,200), "showReward")) {
			mAdReward.load ();
		}
	}

	void OnDestroy() {
		mAdReward.onDestroy ();
	}
  ```
  
#### 串接 Card
----
  串接 Card AdView 範例 [```UnityPlayerActivity```](https://github.com/applauseadn/android-sdk/blob/master/VMFiveUnity/app/src/main/java/com/vmfive/javaunitysample/UnityPlayerActivity.java)
  
  ``` java
  // 需要生成一個擺放 AdView 的 container, 最後透過 addContentView 加載到 UnityPlayer
  ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
  mUnityPlayer.currentActivity.getWindow().addContentView(mRelativeLayout, vlp);
  ```

## 問題排解
1. 影音廣告的影片, 有顯示CoverImage, 但播放影片時為黑屏

    ``` java
    /**
     * 請確認在 AndroidManifest.xml, 該版位所對應的 Activity/Application context 是否有啟用 hardwareAccelerated
    */
    android:hardwareAccelerated="true"
    ```
    
2. 廣告無法顯示素材的圖檔, 或影音廣告的影片, 無顯示CoverImage, 播放影片時也是黑屏

    ``` java
    /**
     * 請確認在 AndroidManifest.xml, 是否有增加 read/write storage permissions
    */
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    ```
    > 若開發者的 Application 為 Android M(23) 版本以上, 需要增加 request permission dialog, 可參考
    [VMFiveAdNetwork Demo App 的實現](https://github.com/applauseadn/android-sdk-3.0/blob/master/VMFiveAdNetwork/app/src/main/java/com/core/vmfiveadnetwork/MainActivity.java)
    ``` java
    // check permissions for M, if some permission denied, it would shut down activity
    checkRequiredPermissions();
    ```
    
    > 若問題仍然存在, 可能是使用者已關閉權限, 可到 Settings / Application Info / your package name / read or write permission 手動啟用

3. 如何在 release mode 印出 ADNSDK log

    ``` java
    adb logcat -c
    adb shell setprop log.tag.adnsdk VERBOSE
    adb logcat > adnsdk.log
    ```

    > 在 debug mode, 請使用 ADNSDK debug build, 預設就會印出 log
    
4. 啟用收集用戶手機安裝App資訊

    若開發者同意開啟收集用戶手機安裝App資訊，可以提昇廣告效益，SDK可能會優先投放廣告給開發者，進而提高開發者廣告營收，開發者需要配合SDK，顯示相關訊息讓用戶知道！如[參考代碼: MainActivity.java](https://github.com/applauseadn/android-sdk-3.0/blob/master/VMFiveAdNetwork/app/src/main/java/com/core/vmfiveadnetwork/MainActivity.java#L135)
    
    ```java
    // need to enable app scan in backend, and prompt this dialog to notice user
    ADN.showAppScanDialog(this, "是否同意讓我們收集App安裝資訊");
    ```
