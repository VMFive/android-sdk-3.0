<p align="center">
<img src="https://github.com/applauseadn/android-sdk-3.0/blob/master/images/Unity_Import_Package.png?raw=true" alt="Video_Reward" width="640" height="400">
</p>

  [VMFiveADN_Reward.unitypackage](https://github.com/applauseadn/android-sdk-3.0/tree/master/VMFiveADN_UnityPackages)
  請按照圖示載入 Unity package, 並參考 CallJavaCode.cs 加上載入 AdReward 的代碼, 最後執行 Build & Run
  
  1. 設定 android sdk, java jdk 路徑
  
      SDK 路徑: Unity / Preferences / External Tools / SDK
      
      JDK 路徑: Unity / Preferences / External Tools / JDK
  
  2. (Optional) trouble writing output: Too many method references: 70332; max is 65536.
      
      請參考 https://github.com/darkdukey/Google-Play-Service-Lite, 刪除不需要的 gms modules
      
  3. (Optional) gms 衝突
  
     因開發者的 unity project 已經加上 gms, 所以請移除 unity package 中 google-play-services.jar
     
  4. (Optional) No resource found that matches the given name (at 'theme' with value '@style/UnityThemeSelector').
  
      如果開發者使用 4.x Unity, 可能會遇到這個錯誤, 開發者需修改 AndroidManifest.xml 將 android:theme="@style/UnityThemeSelector" 拿掉
      
      AndroidManifest.xml 路徑: Assets / Plugins / Android / AndroidManifest.xml
     
  5. 修改 android bundle identifier
  
      Android Bundle Identifier 路徑: File / Build Settings / Android / Player Settings / 右邊 inspector 視窗裡的安卓小人偶 / Bundle identifier
      
  7. Unity package 已附上 3.0.0 版本的 sdk, 如想更新 VMFiveADNSDK 為最新版本, 例如 3.x.x, 請到 [下載最新版 SDK:jar](https://github.com/applauseadn/android-sdk-3.0/tree/master/VMFiveADNSDK)
  
      adnsdk-release.jar 路徑: Assets / Plugins / Android / adnsdk-release.jar
  
  8. 參考底下代碼, 完成串接, 主要注意的事項是, 在你想載入 reward 的地方呼叫 mAdReward.load(), 待 reward ad 準備完成後, 會透過 AdRewardListener.onAdLoaded() callback 回來, 並開發者可以使用 mAdReward.show() 展示廣告
  
  > 若該廣告為第一次載入, 需要一段載入時間, 開發者需要增加等待訊息或視窗提醒使用者, 可根據自己的需求增加
  
  你可以在 AdReward.AdRewardListener.onAdRewarded(), 或是 AdReward.AdRewardListener.onAdClosed() callback 中更新 Unity 控件, 但因為 android 與 Unity 執行在不同的 thread, 因此 Unity 的代碼需要執行在 Unity main thread 中, 我們已經幫開發者實現 task queue, 開發者只需要將想執行的 Unity 代碼放到 mAdReward.runOnMainThread(), 以及在 MonoBehaviour.Update() 加上 mAdReward.update(), 就可以讓開發者在 android thread 上將要執行的 Unity task 丟到 Unity main thread 執行
  
  ```java
  public class CallJavaCode : MonoBehaviour {
  	private AdReward mAdReward = null;

	void Start() {
		mAdReward = new AdReward ("5630c874cef2370b13942b8f", "placement(reward_video)");
