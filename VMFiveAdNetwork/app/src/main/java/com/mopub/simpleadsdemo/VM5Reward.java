package com.mopub.simpleadsdemo;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import com.core.adnsdk.AdObject;
import com.core.adnsdk.AdReward;
import com.core.adnsdk.AdRewardListener;
import com.core.adnsdk.AdRewardType;
import com.core.adnsdk.ErrorMessage;
import com.mopub.common.LifecycleListener;
import com.mopub.common.MoPubReward;
import com.mopub.mobileads.CustomEventRewardedVideo;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubRewardedVideoManager;

import java.util.Map;

/**
 * Created by ChanYiChih on 2016/4/22.
 */
public class VM5Reward extends CustomEventRewardedVideo {
    private static final String TAG = "VM5Interstitial";

    private static final String VM5_AD_NETWORK_CONSTANT = "vm5_id";

    private AdReward mAdReward;
    private RewardedVideoListener mRewardListener = new RewardedVideoListener();

    private final RewardLifeCycleListener mRewardLifeCycleListener = new RewardLifeCycleListener();

    public VM5Reward() {
    }

    @Nullable
    @Override
    protected CustomEventRewardedVideoListener getVideoListenerForSdk() {
        return mRewardListener;
    }

    @Nullable
    @Override
    protected LifecycleListener getLifecycleListener() {
        return mRewardLifeCycleListener;
    }

    @NonNull
    @Override
    protected String getAdNetworkId() {
        return VM5_AD_NETWORK_CONSTANT;
    }

    @Override
    protected void onInvalidate() {
        onDestroy();
    }

    @Override
    protected boolean checkAndInitializeSdk(@NonNull Activity launcherActivity, @NonNull Map<String, Object> localExtras, @NonNull Map<String, String> serverExtras) throws Exception {
        String apiKey = "";
        if (serverExtras.containsKey("apiKey")) {
            apiKey = serverExtras.get("apiKey");
        } else {
            Log.e(TAG, "User should specify apiKey in serverExtras");
            MoPubRewardedVideoManager.onRewardedVideoLoadFailure(VM5Reward.class, VM5_AD_NETWORK_CONSTANT, MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return false;
        }

        String placementName = "";
        if (serverExtras.containsKey("placement")) {
            placementName = serverExtras.get("placement");
        } else {
            Log.e(TAG, "User should specify placement in serverExtras");
            MoPubRewardedVideoManager.onRewardedVideoLoadFailure(VM5Reward.class, VM5_AD_NETWORK_CONSTANT, MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return false;
        }

        boolean isTestMode = true;
        if (serverExtras.containsKey("test")) {
            isTestMode = serverExtras.get("test").equals("1");
        } else {
            Log.e(TAG, "User should specify test in serverExtras");
            MoPubRewardedVideoManager.onRewardedVideoLoadFailure(VM5Reward.class, VM5_AD_NETWORK_CONSTANT, MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return false;
        }
        return true;
    }

    @Override
    protected void loadWithSdkInitialized(@NonNull Activity activity, @NonNull Map<String, Object> localExtras, @NonNull Map<String, String> serverExtras) throws Exception {
        String apiKey = serverExtras.get("apiKey");
        String placementName = serverExtras.get("placement");
        boolean isTestMode = serverExtras.get("test").equals("1");

        mAdReward = new AdReward(activity, apiKey, placementName, AdRewardType.REWARD_VIDEO);
        mAdReward.setTestMode(isTestMode);
        mAdReward.setAdListener(mRewardListener);
        mAdReward.loadAd();
    }

    @Override
    protected boolean hasVideoAvailable() {
        return mAdReward.isLoaded();
    }

    @Override
    protected void showVideo() {
        if (mAdReward.isLoaded()) {
            mAdReward.showAd();
            MoPubRewardedVideoManager.onRewardedVideoStarted(VM5Reward.class, VM5_AD_NETWORK_CONSTANT);
        }
    }

    public void onResume() {
        if (mAdReward != null) {
            mAdReward.onResume();
        }
    }

    public void onPause() {
        if (mAdReward != null) {
            mAdReward.onPause();
        }
    }

    public void onDestroy() {
        if (mAdReward != null) {
            mAdReward.onDestroy();
            mAdReward = null;
        }
    }

    private class RewardedVideoListener implements AdRewardListener, CustomEventRewardedVideoListener {
        @Override
        public void onAdLoaded(AdObject adObject) {
            MoPubRewardedVideoManager.onRewardedVideoLoadSuccess(VM5Reward.class, VM5_AD_NETWORK_CONSTANT);
        }

        @Override
        public void onError(ErrorMessage errorMessage) {
            MoPubRewardedVideoManager.onRewardedVideoLoadFailure(VM5Reward.class, VM5_AD_NETWORK_CONSTANT, MoPubErrorCode.NETWORK_NO_FILL);
            switch (errorMessage) {
                case GENERIC:
                    MoPubRewardedVideoManager.onRewardedVideoLoadFailure(VM5Reward.class, VM5_AD_NETWORK_CONSTANT, MoPubErrorCode.UNSPECIFIED);
                    break;
                case NOADS:
                    MoPubRewardedVideoManager.onRewardedVideoLoadFailure(VM5Reward.class, VM5_AD_NETWORK_CONSTANT, MoPubErrorCode.VIDEO_NOT_AVAILABLE);
                    break;
                case RESOURCES_DOWNLOAD_FAIL:
                    MoPubRewardedVideoManager.onRewardedVideoLoadFailure(VM5Reward.class, VM5_AD_NETWORK_CONSTANT, MoPubErrorCode.NETWORK_INVALID_STATE);
                    break;
                case NOTVISIBLE:
                    MoPubRewardedVideoManager.onRewardedVideoLoadFailure(VM5Reward.class, VM5_AD_NETWORK_CONSTANT, MoPubErrorCode.NETWORK_NO_FILL);
                    break;
                default:
                    MoPubRewardedVideoManager.onRewardedVideoLoadFailure(VM5Reward.class, VM5_AD_NETWORK_CONSTANT, MoPubErrorCode.NETWORK_INVALID_STATE);
            }
        }

        @Override
        public void onAdClicked() {
            MoPubRewardedVideoManager.onRewardedVideoClicked(VM5Reward.class, VM5_AD_NETWORK_CONSTANT);
        }

        @Override
        public void onAdFinished() {

        }

        @Override
        public void onAdReleased() {
            MoPubRewardedVideoManager.onRewardedVideoClosed(VM5Reward.class, VM5_AD_NETWORK_CONSTANT);
            onDestroy();
        }

        @Override
        public boolean onAdWatched() {
            return false;
        }

        @Override
        public void onAdImpressed() {
        }

        @Override
        public String onAdRewarded(AdReward.RewardInfo rewardInfo) {
            MoPubReward reward = MoPubReward.success(rewardInfo.getCurrency(), (int) rewardInfo.getAmount());
            MoPubRewardedVideoManager.onRewardedVideoCompleted(VM5Reward.class, VM5_AD_NETWORK_CONSTANT, reward);
            return rewardInfo.toString();
        }

        @Override
        public void onAdReplayed() {

        }

        @Override
        public void onAdClosed() {

        }
    }

    private final class RewardLifeCycleListener implements LifecycleListener {

        @Override
        public void onCreate(@NonNull Activity activity) {
            Log.d(TAG, "onCreate");
        }

        @Override
        public void onStart(@NonNull Activity activity) {
            Log.d(TAG, "onStart");
        }

        @Override
        public void onPause(@NonNull Activity activity) {
            Log.d(TAG, "onPause");
        }

        @Override
        public void onResume(@NonNull Activity activity) {
            Log.d(TAG, "onResume");
        }

        @Override
        public void onRestart(@NonNull Activity activity) {
            Log.d(TAG, "onRestart");
        }

        @Override
        public void onStop(@NonNull Activity activity) {
            Log.d(TAG, "onStop");
        }

        @Override
        public void onDestroy(@NonNull Activity activity) {
            Log.d(TAG, "onDestroy");
        }

        @Override
        public void onBackPressed(@NonNull Activity activity) {
            Log.d(TAG, "onBackPressed");
        }
    }
}
