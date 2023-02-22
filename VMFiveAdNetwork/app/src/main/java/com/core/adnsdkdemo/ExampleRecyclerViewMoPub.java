package com.core.adnsdkdemo;

import android.app.Activity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.common.logging.MoPubLog;
import com.mopub.nativeads.MoPubRecyclerAdapter;
import com.mopub.simpleadsdemo.VM5MediaViewBinder;
import com.mopub.simpleadsdemo.VM5MoPubVideoNativeAdRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExampleRecyclerViewMoPub extends Activity {
    private static final String TAG = "ExampleRecyclerView";

    private MoPubRecyclerAdapter mRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_recyclerview);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.native_recycler_view);

        // just for demo
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < 100; ++i) {
            data.add("Item " + i);
        }
        DemoAdapter originalAdapter = new DemoAdapter(data);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        VM5MediaViewBinder videoAdBinder = new VM5MediaViewBinder.Builder(R.layout.vm5_video_ad_list_item)
                .titleId(R.id.native_title)
                .textId(R.id.native_subtitle)
                .mediaLayoutId(R.id.native_media_layout)
                .iconImageId(R.id.native_icon_image)
                .callToActionId(R.id.native_cta)
                .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                .build();
        VM5MoPubVideoNativeAdRenderer vm5MoPubVideoNativeAdRenderer = new VM5MoPubVideoNativeAdRenderer(videoAdBinder);

        mRecyclerAdapter = new MoPubRecyclerAdapter(this, originalAdapter);
        mRecyclerAdapter.registerAdRenderer(vm5MoPubVideoNativeAdRenderer);
        recyclerView.setAdapter(mRecyclerAdapter);


        Map<String, String> mediatedNetworkConfiguration1 = new HashMap<>();
        mediatedNetworkConfiguration1.put("<custom-adapter-class-data-key>", "<custom-adapter-class-data-value>");
        Map<String, String> mediatedNetworkConfiguration2 = new HashMap<>();
        mediatedNetworkConfiguration2.put("<custom-adapter-class-data-key>", "<custom-adapter-class-data-value>");

        SdkConfiguration sdkConfiguration = new SdkConfiguration.Builder("AD_UNIT_ID")
                .withLogLevel(MoPubLog.LogLevel.DEBUG)
                .withLegitimateInterestAllowed(false)
                .build();
        MoPub.initializeSdk(this, sdkConfiguration, new SdkInitializationListener() {
            @Override
            public void onInitializationFinished() {
               /*
               MoPub SDK initialized.
               Check if you should show the consent dialog here, and make your ad requests.
               */
               mRecyclerAdapter.refreshAds("09a3bc6e42f84e389ef3cf9784082143");
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mRecyclerAdapter != null) {
//            mRecyclerAdapter.onDestroy();
            mRecyclerAdapter.destroy();
            mRecyclerAdapter = null;
        }
        super.onDestroy();
    }

    // original adapter of user
    class DemoAdapter extends RecyclerView.Adapter<DemoAdapter.ItemViewHolder> {
        private final List<String> data;

        public DemoAdapter(List<String> data) {
            this.data = data;
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            ItemViewHolder holder = new ItemViewHolder(v);
            holder.text = (TextView) v;
            return holder;
        }

        @Override
        public void onBindViewHolder(ItemViewHolder itemVH, int i) {
            switch (itemVH.getItemViewType()) {
                case 0:
                    itemVH.text.setText(data.get(i));
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {
            TextView text;

            public ItemViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

    public class ListViewItem {
        private String text;
        private int type;

        public ListViewItem(String text, int type) {
            this.text = text;
            this.type = type;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

}
