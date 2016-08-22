package com.mopub.simpleadsdemo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mopub.common.VisibleForTesting;
import com.mopub.common.logging.MoPubLog;
import com.mopub.nativeads.MediaLayout;

/**
 * Created by ChanYiChih on 2016/5/5.
 */
public class VM5MediaViewHolder {
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

    @VisibleForTesting
    static final VM5MediaViewHolder EMPTY_MEDIA_VIEW_HOLDER = new VM5MediaViewHolder();

    // Use fromViewBinder instead of a constructor
    private VM5MediaViewHolder() {
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
}
