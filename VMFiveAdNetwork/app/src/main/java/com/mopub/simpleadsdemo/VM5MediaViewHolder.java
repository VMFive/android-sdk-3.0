package com.mopub.simpleadsdemo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mopub.common.VisibleForTesting;
import com.mopub.common.logging.MoPubLog;
import com.mopub.nativeads.MediaLayout;
import com.mopub.nativeads.VideoNativeAd;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

/**
 * Created by ChanYiChih on 2016/5/5.
 */
public class VM5MediaViewHolder implements VM5MoPubCentralManagerLifeCycleListener {
    @Nullable
    View mainView;
    @Nullable
    MediaLayout mediaLayout;
    @Nullable
    TextView titleView;
    @Nullable
    TextView textView;
    @Nullable
    ImageView iconImageView;
    @Nullable
    TextView callToActionView;
    @Nullable
    ImageView privacyInformationIconImageView;
    @Nullable
    private WeakReference<VideoNativeAd> videoNativeAdWeakReference;

    @VisibleForTesting
    static final VM5MediaViewHolder EMPTY_MEDIA_VIEW_HOLDER = new VM5MediaViewHolder();
    @VisibleForTesting
    private static final WeakHashMap<VideoNativeAd, WeakReference<VM5MediaViewHolder>> videoNativeAdWeakReferenceWeakHashMap = new WeakHashMap<>();
    @VisibleForTesting
    private final View.OnClickListener ctaOnMeOnClickListener;

    // Use fromViewBinder instead of a constructor
    private VM5MediaViewHolder() {
        final WeakReference<VM5MediaViewHolder> vm5MediaViewHolderWeakReference = new WeakReference<>(this);
        ctaOnMeOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                VM5MoPubCentralManager.getInstance().showLoading();

                VM5MediaViewHolder vm5MediaViewHolder = vm5MediaViewHolderWeakReference.get();
                if (vm5MediaViewHolder != null) {
                    // NOTE Do OnResume once only.(being registered only when needed)
                    VM5MoPubCentralManager.getInstance().registerLifecycleListener(vm5MediaViewHolder);

                    if (vm5MediaViewHolder.mainView != null) {
                        vm5MediaViewHolder.mainView.performClick();
                    }
                }
            }
        };
    }

    @NonNull
    static VM5MediaViewHolder fromViewBinder(@NonNull final View view,
                                             @NonNull final VM5MediaViewBinder mediaViewBinder) {
        final VM5MediaViewHolder mediaViewHolder = new VM5MediaViewHolder();
        mediaViewHolder.mainView = view;
        try {
            mediaViewHolder.titleView = (TextView) view.findViewById(mediaViewBinder.titleId);
            mediaViewHolder.textView = (TextView) view.findViewById(mediaViewBinder.textId);
            mediaViewHolder.callToActionView =
                    (TextView) view.findViewById(mediaViewBinder.callToActionId);
            mediaViewHolder.mediaLayout = (MediaLayout) view.findViewById(mediaViewBinder.mediaLayoutId);
            mediaViewHolder.iconImageView =
                    (ImageView) view.findViewById(mediaViewBinder.iconImageId);
            mediaViewHolder.privacyInformationIconImageView =
                    (ImageView) view.findViewById(mediaViewBinder.privacyInformationIconImageId);
            return mediaViewHolder;
        } catch (ClassCastException exception) {
            MoPubLog.w("Could not cast from id in MediaViewBinder to expected View type",
                    exception);
            return EMPTY_MEDIA_VIEW_HOLDER;
        }
    }

    public void setVideoNativeAdWeakReference(WeakReference<VideoNativeAd> videoNativeAdWeakReference) {
        this.videoNativeAdWeakReference = videoNativeAdWeakReference;
        if (videoNativeAdWeakReference != null) {
            VideoNativeAd videoNativeAd = videoNativeAdWeakReference.get();
            if (videoNativeAd != null) {
                videoNativeAdWeakReferenceWeakHashMap.put(videoNativeAd, new WeakReference<VM5MediaViewHolder>(this));
            }
        }
    }

    @Override
    public void onResume() {
        if (mediaLayout != null) {
//            mediaLayout.setMode(MediaLayout.Mode.PLAYING);
            if (videoNativeAdWeakReference != null) {
                VideoNativeAd videoNativeAd = videoNativeAdWeakReference.get();
                if (videoNativeAd != null) {
                    WeakReference<VM5MediaViewHolder> vm5MediaViewHolderWeakReference = videoNativeAdWeakReferenceWeakHashMap.get(videoNativeAd);
                    if (vm5MediaViewHolderWeakReference != null) {
                        final VM5MediaViewHolder vm5MediaViewHolder = vm5MediaViewHolderWeakReference.get();
//                        if (vm5MediaViewHolder == this) {
//                        }
                        if (vm5MediaViewHolder != null && vm5MediaViewHolder == this) {
                            MediaLayout targetMediaLayout = vm5MediaViewHolder.mediaLayout;
//                            targetMediaLayout.resetProgress();
//                            targetMediaLayout.setMode(MediaLayout.Mode.PLAYING);

                            videoNativeAd.clear(mainView);
//                            videoNativeAd.onStateChanged(true, NativeVideoController.STATE_READY);
                            videoNativeAd.prepare(mainView);
                            videoNativeAd.render(targetMediaLayout);
                            vm5MediaViewHolder.bindCtaEventsOnViews();

                            if (targetMediaLayout != null) {
                                try {
                                    int childCount = targetMediaLayout.getChildCount();
                                    for (int i = 0; i < childCount; i++) {
                                        View child = targetMediaLayout.getChildAt(i);
                                        if (child instanceof ImageView) {
                                            child.performClick();
                                        }
                                    }
                                } catch (Throwable e) {
                                    Log.e(VM5MediaViewHolder.class.getCanonicalName(), "Something wrong when onResume", e);
                                }
                            }
                        }
                    }
                }
            }
        }

        // NOTE Do OnResume once only.
        VM5MoPubCentralManager.getInstance().unregisterLifecycleListener(this);
    }

    void bindCtaEventsOnViews() {
        View[] views = {
                mediaLayout,
                titleView,
                textView,
                iconImageView,
                callToActionView,
//                privacyInformationIconImageView
        };
        for (View view : views) {
            if (view == null) {
                continue;
            }
            view.setOnClickListener(ctaOnMeOnClickListener);
        }
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }
}
