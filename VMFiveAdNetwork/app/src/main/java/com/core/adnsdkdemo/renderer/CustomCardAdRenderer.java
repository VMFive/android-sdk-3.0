package com.core.adnsdkdemo.renderer;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.core.adnsdk.AdBaseSpec;
import com.core.adnsdk.AdObject;
import com.core.adnsdk.AdRenderer;
import com.core.adnsdk.AdResourceSpec;
import com.core.adnsdk.CardViewBinder;
import com.core.adnsdk.CardViewHolder;
import com.core.adnsdk.CharacterInputFilter;
import com.core.adnsdk.GifView;
import com.core.adnsdk.VideoPlayer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ChanYiChih on 2016/5/9.
 */
public class CustomCardAdRenderer implements AdRenderer {
    private static final String TAG = "CustomCardAdRenderer";

    private ImageView.ScaleType mCoverImageScaleType = ImageView.ScaleType.CENTER_CROP;
    private final CardViewBinder mCardViewBinder;

    public CustomCardAdRenderer(CardViewBinder cardViewBinder) {
        mCardViewBinder = cardViewBinder;
    }

    @Override
    public void init(Context context) {

    }

    @Override
    public View createAdView(Context context, ViewGroup parent, AdBaseSpec baseSpec) {
        View view = LayoutInflater.from(context).inflate(mCardViewBinder.layoutId, parent, false);
        CardViewHolder holder = CardViewHolder.createFromBinder(view, mCardViewBinder);
        view.setTag(holder);
        if (holder.loadingImageView != null) {
            holder.loadingImageView.setVisibility(View.INVISIBLE);
        }
        if (holder.callToActionTextView != null) {
            holder.callToActionTextView.setVisibility(View.INVISIBLE);
        }
        if (holder.videoPlayer != null) {
            holder.videoPlayer.setCoverImageScaleType(mCoverImageScaleType);
        }
        return view;
    }

    @Override
    public void loadingAdView(View view, AdObject adObject) {
        CardViewHolder holder = (CardViewHolder)view.getTag();
        if (holder == null) return;

        if (holder.loadingImageView != null) {
            holder.loadingImageView.setVisibility(View.VISIBLE);
        }
        if (holder.callToActionTextView != null) {
            holder.callToActionTextView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void renderAdView(View view, AdObject adObject, ArrayList<View> interactView, HashMap<String, Bitmap> maps) {
        CardViewHolder holder = (CardViewHolder)view.getTag();
        if (holder == null) return;

        ImageView mainImage = holder.mainImageView;
        ImageView iconImage = holder.iconImageView;
        TextView title = holder.titleTextView;
        TextView subTitle = holder.subTitleTextView;
        TextView description = holder.descriptionTextView;
        TextView callToAction = holder.callToActionTextView;
        TextView countDown = holder.countDownTextView;
        ViewGroup expandLayout = holder.expandLayoutView;
        ImageView expand = holder.expandImageView;
        VideoPlayer videoLayout = holder.videoPlayer;

        interactView.add(holder.layoutView);
        if (mainImage != null) {
            if (mainImage instanceof GifView) {
                if (adObject.getAdImageResource() != null) {
                    GifView gifView = (GifView)mainImage;
                    boolean isSuccess = gifView.loadGifFile(adObject.getAdImageResource().getContentPath());
                    if (!isSuccess) {
                        gifView.setImageBitmap(maps.get(AdResourceSpec.ADRES_TAG_IMAGE));
                    }
                }
            } else {
                if (adObject.getAdImageResource() != null) {
                    mainImage.setImageBitmap(maps.get(AdResourceSpec.ADRES_TAG_IMAGE));
                } else {
                    mainImage.setImageDrawable(null);
                }
            }
            interactView.add(mainImage);
        }

        if (iconImage != null) {
            if (adObject.getAdIconResource() != null) {
                iconImage.setImageBitmap(maps.get(AdResourceSpec.ADRES_TAG_ICON));
                iconImage.setVisibility(View.VISIBLE);
            } else {
                iconImage.setImageDrawable(null);
                iconImage.setVisibility(View.GONE);
            }
            interactView.add(iconImage);
        }

        if (videoLayout != null && adObject.getAdVideoResource() != null) {
            videoLayout.setup(adObject, maps.get(AdResourceSpec.ADRES_TAG_VIDEO));
            videoLayout.setCountDownTextView(countDown);
            if (expandLayout != null) {
                videoLayout.setExpandImageView(expandLayout, expand);
            } else {
                videoLayout.setExpandImageView(expand);
            }
            interactView.add(videoLayout.getVideoView());
            interactView.add(videoLayout.getCoverImgView());
        }

        if (title != null) {
            if (adObject.getAdTitle() != null && !adObject.getAdTitle().equals("")) {
                title.setFilters(new InputFilter[]{ new CharacterInputFilter(40, true) });
                title.setText(adObject.getAdTitle());
                interactView.add(title);
            }
        }

        if (subTitle != null) {
            if (adObject.getAdSubTitle() != null && !adObject.getAdSubTitle().equals("")) {
                subTitle.setText(adObject.getAdSubTitle());
                interactView.add(subTitle);
                subTitle.setVisibility(View.VISIBLE);
            } else {
                subTitle.setVisibility(View.GONE);
            }
        }

        if (description != null) {
            if (adObject.getAdDescription() != null && !adObject.getAdDescription().equals("")) {
                description.setText(adObject.getAdDescription());
                interactView.add(description);
                description.setVisibility(View.VISIBLE);
            } else {
                description.setVisibility(View.GONE);
            }
        }

        if (callToAction != null && adObject.getAdCtaText() != null) {
            if (!adObject.getAdCtaText().equals("")) {
                callToAction.setFilters(new InputFilter[]{ new CharacterInputFilter(14) });
                callToAction.setText(adObject.getAdCtaText());
                callToAction.setVisibility(View.VISIBLE);
                interactView.add(callToAction);
            } else {
                callToAction.setVisibility(View.INVISIBLE);
            }
        }
        if (holder.loadingImageView != null) {
            holder.loadingImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public void cleanAdView(View view, AdObject adObject) {
        CardViewHolder holder = (CardViewHolder)view.getTag();
        if (holder == null) return;

        ImageView mainImage = holder.mainImageView;
        ImageView iconImage = holder.iconImageView;
        TextView title = holder.titleTextView;
        TextView subTitle = holder.subTitleTextView;
        TextView description = holder.descriptionTextView;
        TextView callToAction = holder.callToActionTextView;
        VideoPlayer videoLayout = holder.videoPlayer;

        if (mainImage != null) {
            if (mainImage instanceof GifView) {
                ((GifView)mainImage).clean();
            } else {
                mainImage.setImageDrawable(null);
            }
        }

        if (iconImage != null) {
            iconImage.setImageDrawable(null);
        }

        if (videoLayout != null) {
            videoLayout.clean();
        }

        if (title != null) {
            title.setText(null);
        }

        if (subTitle != null) {
            subTitle.setText(null);
        }

        if (description != null) {
            description.setText(null);
        }

        if (callToAction != null) {
            callToAction.setText(null);
            callToAction.setVisibility(View.INVISIBLE);
        }
        if (holder.loadingImageView != null) {
            holder.loadingImageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void release() {
    }
}
