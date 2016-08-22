/*
 * Copyright (C) 2015 VMFive Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.core.vmfiveadnetwork.renderer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.core.adnsdk.AdBaseSpec;
import com.core.adnsdk.AdObject;
import com.core.adnsdk.AdResourceSpec;
import com.core.adnsdk.GifView;
import com.core.adnsdk.RecyclerAdRenderer;
import com.core.adnsdk.RecyclerBannerViewBinder;
import com.core.adnsdk.RecyclerBannerViewHolder;
import com.core.adnsdk.VideoPlayer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ChanYiChih on 2016/5/9.
 */
public class CustomRecyclerBannerAdRenderer implements RecyclerAdRenderer {
    private static final String TAG = "CustomRecyclerBannerAdRenderer";

    private ImageView.ScaleType mCoverImageScaleType = ImageView.ScaleType.CENTER_CROP;
    private final RecyclerBannerViewBinder mBannerViewBinder;
    private int mScreenWidth;
    private int mScreenHeight;

    public CustomRecyclerBannerAdRenderer(RecyclerBannerViewBinder nativeBannerViewBinder) {
        mBannerViewBinder = nativeBannerViewBinder;
    }

    @Override
    public void init(Context context) {
        WindowManager window = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = window.getDefaultDisplay();
        Point dimension = new Point();
        display.getSize(dimension);
        mScreenWidth = dimension.x;
        mScreenHeight = dimension.y;
    }

    @Override
    public View createAdView(Context context, ViewGroup parent, AdBaseSpec baseSpec) {
        View view = LayoutInflater.from(context).inflate(mBannerViewBinder.layoutId, parent, false);
        RecyclerBannerViewHolder holder = RecyclerBannerViewHolder.createFromBinder(view, mBannerViewBinder);
        view.setTag(holder);
        if (holder.loadingImageView != null) {
            holder.loadingImageView.setVisibility(View.INVISIBLE);
        }
        if (holder.videoPlayer != null) {
            holder.videoPlayer.setCoverImageScaleType(mCoverImageScaleType);
        }
        return view;
    }

    @Override
    public void loadingAdView(View view, AdObject adObject) {
        RecyclerBannerViewHolder holder = (RecyclerBannerViewHolder)view.getTag();
        if (holder == null) return;

        if (holder.loadingImageView != null) {
            holder.loadingImageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void renderAdView(View view, AdObject adObject, ArrayList<View> interactView, HashMap<String, Bitmap> maps) {
        RecyclerBannerViewHolder holder = (RecyclerBannerViewHolder)view.getTag();
        if (holder == null) return;

        ImageView backgroundImage = holder.mainImageView;
        VideoPlayer videoLayout = holder.videoPlayer;

        interactView.add(holder.layoutView);
        if (backgroundImage != null) {
            if (backgroundImage instanceof GifView) {
                if (adObject.getAdImageResource() != null) {
                    GifView gifView = (GifView)backgroundImage;
                    boolean isSuccess = gifView.loadGifFile(adObject.getAdImageResource().getContentPath());
                    if (!isSuccess) {
                        gifView.setImageBitmap(maps.get(AdResourceSpec.ADRES_TAG_IMAGE));
                    }
                }
            } else {
                if (adObject.getAdImageResource() != null) {
                    backgroundImage.setImageBitmap(maps.get(AdResourceSpec.ADRES_TAG_IMAGE));
                } else {
                    backgroundImage.setImageDrawable(null);
                }
            }
            interactView.add(backgroundImage);
        }

        if (videoLayout != null && adObject.getAdVideoResource() != null) {
            // VideoPlayer measure dimension by width, we need to reset its width based on video ratio
            ViewGroup.LayoutParams lp = videoLayout.getLayoutParams();
            if (lp.height != ViewGroup.LayoutParams.MATCH_PARENT && lp.height != ViewGroup.LayoutParams.WRAP_CONTENT) {
                float videoRatio = adObject.getVideoRatio();
                lp.width = (int) (lp.height * videoRatio + 0.5f);
                videoLayout.setLayoutParams(lp);
            }
            videoLayout.setMuteEnabled(false);
            videoLayout.setCountDownEnabled(false);
            videoLayout.setup(adObject, maps.get(AdResourceSpec.ADRES_TAG_VIDEO));
        }
        if (holder.loadingImageView != null) {
            holder.loadingImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public void cleanAdView(View view, AdObject adObject) {
        RecyclerBannerViewHolder holder = (RecyclerBannerViewHolder)view.getTag();
        if (holder == null) return;

        ImageView backgroundImage = holder.mainImageView;
        VideoPlayer videoLayout = holder.videoPlayer;

        if (backgroundImage != null) {
            if (backgroundImage instanceof GifView) {
                ((GifView)backgroundImage).clean();
            } else {
                backgroundImage.setImageDrawable(null);
            }
        }

        if (videoLayout != null) {
            videoLayout.clean();
        }
        if (holder.loadingImageView != null) {
            holder.loadingImageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void release() {
    }
}
