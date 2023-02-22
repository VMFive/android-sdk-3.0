package com.mopub.simpleadsdemo;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.core.adnsdk.ComscoreTrackingMediator;
import com.mopub.common.Preconditions;
import com.mopub.common.VisibleForTesting;
import com.mopub.nativeads.BaseNativeAd;
import com.mopub.nativeads.MediaLayout;
import com.mopub.nativeads.MoPubAdRenderer;
import com.mopub.nativeads.NativeImageHelper;
import com.mopub.nativeads.NativeRendererHelper;
import com.mopub.nativeads.VideoNativeAd;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

/**
 * Created by ChanYiChih on 2016/5/5.
 */
public class VM5MoPubVideoNativeAdRenderer implements MoPubAdRenderer<VideoNativeAd> {
    @NonNull
    private final VM5MediaViewBinder mMediaViewBinder;

    // This is used instead of View.setTag, which causes a memory leak in 2.3
// and earlier: https://code.google.com/p/android/issues/detail?id=18273
    @VisibleForTesting
    @NonNull
    final WeakHashMap<View, VM5MediaViewHolder> mMediaViewHolderMap;

    /**
     * Constructs a native ad renderer with a view binder.
     *
     * @param mediaViewBinder The view binder to use when inflating and rendering an ad.
     */
    public VM5MoPubVideoNativeAdRenderer(@NonNull final VM5MediaViewBinder mediaViewBinder) {
        mMediaViewBinder = mediaViewBinder;
        mMediaViewHolderMap = new WeakHashMap<View, VM5MediaViewHolder>();
    }

    @Override
    @NonNull
    public View createAdView(@NonNull final Context context, @Nullable final ViewGroup parent) {
        return LayoutInflater
                .from(context)
                .inflate(mMediaViewBinder.layoutId, parent, false);
    }

    @Override
    public void renderAdView(@NonNull final View view,
                             @NonNull final VideoNativeAd videoNativeAd) {
        //register View and key to VM5ComScoreTrackingMediator
        Object object = videoNativeAd.getExtra("uniqueID");
        if (object != null && object instanceof String) {
//            ComscoreTrackingMediator.getInstance().registerView((String)object, view);
        }
        VM5MediaViewHolder mediaViewHolder = mMediaViewHolderMap.get(view);
        if (mediaViewHolder == null) {
            mediaViewHolder = VM5MediaViewHolder.fromViewBinder(view, mMediaViewBinder);
            mMediaViewHolderMap.put(view, mediaViewHolder);
        }

        update(mediaViewHolder, videoNativeAd);
        NativeRendererHelper.updateExtras(mediaViewHolder.mainView, mMediaViewBinder.extras, videoNativeAd.getExtras());
        setViewVisibility(mediaViewHolder, View.VISIBLE);

        if (mediaViewHolder.iconImageView != null) {
            if (videoNativeAd.getIconImageUrl() == null || videoNativeAd.getIconImageUrl().equals("")) {
                mediaViewHolder.iconImageView.setVisibility(View.GONE);
            }
        }

        if (mediaViewHolder.callToActionView != null) {
            if (videoNativeAd.getCallToAction() == null || videoNativeAd.getCallToAction().equals("")) {
                mediaViewHolder.callToActionView.setVisibility(View.GONE);
            }
        }

        if (mediaViewHolder.titleView != null) {
            if (videoNativeAd.getTitle() == null || videoNativeAd.getTitle().equals("")) {
                mediaViewHolder.titleView.setVisibility(View.GONE);
            }
        }

        MediaLayout mediaLayout = (MediaLayout) view.findViewById(mMediaViewBinder.mediaLayoutId);
        videoNativeAd.render(mediaLayout);

        mediaViewHolder.setVideoNativeAdWeakReference(new WeakReference<>(videoNativeAd));
//        VM5MoPubCentralManager.getInstance().registerLifecycleListener(mediaViewHolder);
        mediaViewHolder.bindCtaEventsOnViews();
//        mediaViewHolder.onResume();
    }

    @Override
    public boolean supports(@NonNull final BaseNativeAd nativeAd) {
        Preconditions.checkNotNull(nativeAd);
        return nativeAd instanceof VideoNativeAd;
    }

    private void update(@NonNull final VM5MediaViewHolder mediaViewHolder,
                        @NonNull final VideoNativeAd videoNativeAd) {
        NativeRendererHelper.addTextView(mediaViewHolder.titleView,
                videoNativeAd.getTitle());
        NativeRendererHelper.addTextView(mediaViewHolder.textView, videoNativeAd.getText());
        NativeRendererHelper.addCtaButton(mediaViewHolder.callToActionView,
                mediaViewHolder.mainView, videoNativeAd.getCallToAction()
        );
        if (mediaViewHolder.mediaLayout != null) {
            NativeImageHelper.loadImageView(videoNativeAd.getMainImageUrl(),
                    mediaViewHolder.mediaLayout.getMainImageView());
        }
        NativeImageHelper.loadImageView(videoNativeAd.getIconImageUrl(),
                mediaViewHolder.iconImageView);
        NativeRendererHelper.addPrivacyInformationIcon(
                mediaViewHolder.privacyInformationIconImageView,
                videoNativeAd.getPrivacyInformationIconImageUrl(),
                videoNativeAd.getPrivacyInformationIconClickThroughUrl());
    }

    private void setViewVisibility(@NonNull final VM5MediaViewHolder mediaViewHolder,
                                   final int visibility) {
        if (mediaViewHolder.mainView != null) {
            mediaViewHolder.mainView.setVisibility(visibility);
        }
    }
}
