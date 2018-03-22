package com.core.vmfiveadnetwork;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mopub.simpleadsdemo.VM5MediaViewBinder;
import com.mopub.simpleadsdemo.VM5MoPubVideoNativeAdRenderer;
import com.mopub.nativeads.MoPubRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

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

        mRecyclerAdapter.refreshAds("126d5f3ea7fd417894ace043132bfdc9");
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
