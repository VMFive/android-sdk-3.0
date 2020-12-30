package com.core.adnsdkdemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.core.adnsdk.*;

import java.util.List;

public class ExampleListView extends Activity {
    private static final String TAG = "ExampleListView";

    private NativeAdAdapter mNativeAdAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_listview);

        final ListView listView = (ListView) findViewById(R.id.native_list_view);

        // original adapter of user
        final ArrayAdapter<String> originalAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1);
        // just for demo
        for (int i = 0; i < 100; ++i) {
            originalAdapter.add("Item " + i);
        }

        /*
        final ArrayList<ListViewItem> items = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            if (i % 2 != 0) {
                items.add(new ListViewItem("ODD " + i, CustomAdapter.TYPE_ODD));
            } else {
                items.add(new ListViewItem("EVEN " + i, CustomAdapter.TYPE_EVEN));
            }
        }
        CustomAdapter originalAdapter = new CustomAdapter(this, R.id.text, items);
        */

        // native video layout builder
        CardViewBinder binder = new CardViewBinder.Builder(R.layout.card_ad_item)
                .loadingId(R.id.native_loading_image)
                .mainImageId(R.id.native_main_image)
                .titleId(R.id.native_title)
                .subTitleId(R.id.native_subtitle)
                .videoPlayerId(R.id.native_video_layout)
                .iconImageId(R.id.native_icon_image)
                .callToActionId(R.id.native_cta)
                .countDownId(R.id.native_count_down)
                .build();

        // set layout builder to adRenderer
        CardAdRenderer adRenderer = new CardAdRenderer(binder);

        final Context context = getApplication();
        final AdViewType adViewType = AdViewType.CARD_VIDEO;
        // create NativeAdAdapter, and given original adapter of user
        // given a placement tag for different advertisement section
        if (BuildConfig.ADN_MULTIKEY) {
            AuthList mAuthList = new AuthListBuilder()
                    .add("Test0", "placement(banner_admob)", "default", 1)
                    .add("Test1", "placement(banner_admob)", "default", 2)
                    .build();
            KeywordList mKeywordList = new KeywordListBuilder()
                    .add("3c")
                    .add("cosmetic")
                    .build();
            AdProfile mAdProfile = new AdProfile.AdProfileBuilder()
                    .setAuthList(mAuthList)
                    .setKeywordList(mKeywordList)
                    .setTestMode(true).build();
            mNativeAdAdapter = new NativeAdAdapter(
                    this
                    , listView
                    , originalAdapter
                    , mAdProfile
                    , adViewType);
        } else {
            mNativeAdAdapter = new NativeAdAdapter(
                    this
                    , listView
                    , originalAdapter
                    , "5630c874cef2370b13942b8f"
                    , "placement(list)"
                    , adViewType);
            mNativeAdAdapter.setTestMode(true); // for testing
        }
        // if user don't provide renderer, it would use default renderer.
        mNativeAdAdapter.setAdRenderer(adRenderer, adViewType); // for Video type
        mNativeAdAdapter.setFrequency(1, 3);
        /**
         * Users are also capable of using {@link com.core.adnsdk.AdPoolListenerAdapter}, default adapter design pattern of AdPoolListener, to receive notification.
         * Therefore, users can focus on specific events they care about.
         */
        mNativeAdAdapter.setAdListener(new AdPoolListener() {
            @Override
            public void onError(int index, ErrorMessage err) {
                Log.d(TAG, "onError : " + err);
                if (err != ErrorMessage.NOTREADY) {
                    Toast.makeText(context, "Error: " + err, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onAdLoaded(int index, AdObject obj) {
                Log.d(TAG, "onAdLoaded!");
            }

            @Override
            public void onAdClicked(int index) {
                Log.d(TAG, "onAdClicked!");
            }

            @Override
            public void onAdFinished(int index) {
                Log.d(TAG, "onAdFinished.");
            }

            @Override
            public void onAdReleased(int index) {
                Log.d(TAG, "onAdReleased.");
            }

            @Override
            public boolean onAdWatched(int index) {
                Log.d(TAG, "onAdWatched.");
                return false;
            }

            @Override
            public void onAdImpressed(int index) {
                Log.d(TAG, "onAdWatched.");
            }
        });
        // (Optional) sdk already set listener to ListView, if user want to set listener of ListView,
        // please set to NativeAdAdapter
        mNativeAdAdapter.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        // (Optional) sdk already set listener to ListView, if user want to set listener of ListView,
        // please set to NativeAdAdapter
        mNativeAdAdapter.setRecyclerListener(new AbsListView.RecyclerListener() {
            @Override
            public void onMovedToScrapHeap(View view) {

            }
        });
        // (Optional) sdk already set listener to ListView, if user want to set listener of ListView,
        // please set to NativeAdAdapter
        mNativeAdAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        listView.setAdapter(mNativeAdAdapter);
    }

    @Override
    protected void onPause() {
        if (mNativeAdAdapter != null) {
            mNativeAdAdapter.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mNativeAdAdapter != null) {
            mNativeAdAdapter.onResume();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mNativeAdAdapter != null) {
            mNativeAdAdapter.onDestroy();
            mNativeAdAdapter = null;
        }
        super.onDestroy();
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

    // view holder pattern
    public class ListItemViewHolder {
        TextView text;

        public ListItemViewHolder(TextView text) {
            this.text = text;
        }

        public TextView getText() {
            return text;
        }

        public void setText(TextView text) {
            this.text = text;
        }
    }

    public class CustomAdapter extends ArrayAdapter {
        public static final int TYPE_ODD = 0;
        public static final int TYPE_EVEN = 1;

        private List<ListViewItem> mItemData;

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return mItemData.get(position).getType();
        }

        public CustomAdapter(Context context, int resource, List<ListViewItem> objects) {
            super(context, resource, objects);
            // because it override getView, so resource id is a don't care field
            this.mItemData = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListItemViewHolder viewHolder = null;
            ListViewItem listViewItem = mItemData.get(position);
            int itemViewType = getItemViewType(position);

            // inflate view statically or create view dynamically
            if (convertView == null) {
                if (itemViewType == TYPE_ODD) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_odd, null);
                } else {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_even, null);
                }
                TextView textView = (TextView) convertView.findViewById(R.id.text);
                viewHolder = new ListItemViewHolder(textView);
                convertView.setTag(viewHolder);
            } else {
                // if convert view is not null, we could reuse it
                viewHolder = (ListItemViewHolder) convertView.getTag();
            }
            // update item view content
            viewHolder.getText().setText(listViewItem.getText());
            return convertView;
        }
    }
}
