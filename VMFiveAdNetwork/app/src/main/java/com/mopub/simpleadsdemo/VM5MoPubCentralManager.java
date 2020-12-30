package com.mopub.simpleadsdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mopub.common.VisibleForTesting;
import com.mopub.common.logging.MoPubLog;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

/**
 * Created by ChanYiChih on 2020/5/13.
 */
public class VM5MoPubCentralManager implements Application.ActivityLifecycleCallbacks {

    private static VM5MoPubCentralManager instance;
    public synchronized static VM5MoPubCentralManager getInstance() {
        if (instance == null) {
            instance = new VM5MoPubCentralManager();
        }
        return instance;
    }

    @VisibleForTesting
    @NonNull
    private final WeakHashMap<Activity, WeakHashMap<VM5MoPubCentralManagerLifeCycleListener, Boolean>> lifecycleListenerMapByActivityMap;

    @Nullable
    private WeakReference<Activity> currentActivity;
    @Nullable
    private AlertDialog dialog;

    /**
     * Constructs a central manager for MoPub Ads.
     */
    private VM5MoPubCentralManager() {
        lifecycleListenerMapByActivityMap = new WeakHashMap<>();
    }

    public void setup(Application application) {
        application.registerActivityLifecycleCallbacks(this);
//        this.application = application;
    }

    public void registerLifecycleListener(VM5MoPubCentralManagerLifeCycleListener listener) {
        if (currentActivity == null) {
            return;
        }
        Activity activity = currentActivity.get();

        WeakHashMap<VM5MoPubCentralManagerLifeCycleListener, Boolean> lifecycleListenerMapByActivity = getLifecycleListenerMapByActivity(activity);
        if (lifecycleListenerMapByActivity != null) {
            lifecycleListenerMapByActivity.put(listener, true);
        }
    }

    public void unregisterLifecycleListener(VM5MoPubCentralManagerLifeCycleListener listener) {
        if (currentActivity == null) {
            return;
        }
        Activity activity = currentActivity.get();

        WeakHashMap<VM5MoPubCentralManagerLifeCycleListener, Boolean> lifecycleListenerMapByActivity = getLifecycleListenerMapByActivity(activity);
        if (lifecycleListenerMapByActivity != null) {
            lifecycleListenerMapByActivity.remove(listener);
        }
    }

    public void showLoading() {
        hideLoading();

        try {

            dialog = initLoadingDialog(currentActivity.get());

            if (dialog == null) {
                return;
            }
            dialog.show();

            Window window = dialog.getWindow();
            if (window != null) {
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(dialog.getWindow().getAttributes());
                layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
                layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(layoutParams);
            }

        } catch (Exception exception) {
            logW("Something error when showLoading", exception);
        }
    }
    public void hideLoading() {
        if (dialog == null) {
            return;
        }

        try {
            dialog.dismiss();
        } catch (Exception exception) {
            logW("Something error when hideLoading", exception);
        }
        dialog = null;
    }
    private AlertDialog initLoadingDialog(Activity context) {
        if (context == null) {
            return null;
        }

        int llPadding = 30;
        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setPadding(llPadding, llPadding, llPadding, llPadding);
        ll.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams llParam = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        ll.setLayoutParams(llParam);

        ProgressBar progressBar = new ProgressBar(context);
        progressBar.setIndeterminate(true);
        progressBar.setPadding(0, 0, llPadding, 0);
        progressBar.setLayoutParams(llParam);

        llParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        TextView tvText = new TextView(context);
        tvText.setText("Loading ...");
        tvText.setTextColor(Color.parseColor("#000000"));
        tvText.setTextSize(20);
        tvText.setLayoutParams(llParam);

        ll.addView(progressBar);
        ll.addView(tvText);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setView(ll);

        return builder.create();
    }

    public void onResume(Activity activity) {
        WeakHashMap<VM5MoPubCentralManagerLifeCycleListener, Boolean> lifecycleListenerMapByActivity = getLifecycleListenerMapByActivity(activity);
        if (lifecycleListenerMapByActivity == null) {
            return;
        }

        for (VM5MoPubCentralManagerLifeCycleListener listener: lifecycleListenerMapByActivity.keySet()) {
            if (listener == null) {
                continue;
            }

            try {
                listener.onResume();
            } catch (Exception exception) {
                logW("Something error when onResume", exception);
            }
        }
    }

    public void onPause(Activity activity) {
        WeakHashMap<VM5MoPubCentralManagerLifeCycleListener, Boolean> lifecycleListenerMapByActivity = getLifecycleListenerMapByActivity(activity);
        if (lifecycleListenerMapByActivity == null) {
            return;
        }

        for (VM5MoPubCentralManagerLifeCycleListener listener: lifecycleListenerMapByActivity.keySet()) {
            if (listener == null) {
                continue;
            }

            try {
                listener.onPause();
            } catch (Exception exception) {
                logW("Something error when onPause", exception);
            }
        }
    }

    public void onDestroy(Activity activity) {
        WeakHashMap<VM5MoPubCentralManagerLifeCycleListener, Boolean> lifecycleListenerMapByActivity = getLifecycleListenerMapByActivity(activity);
        if (lifecycleListenerMapByActivity == null) {
            return;
        }

        for (VM5MoPubCentralManagerLifeCycleListener listener: lifecycleListenerMapByActivity.keySet()) {
            if (listener == null) {
                continue;
            }

            try {
                listener.onDestroy();
            } catch (Exception exception) {
                logW("Something error when onDestroy", exception);
            }
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        if (activity == null) {
            return;
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (activity == null) {
            return;
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (activity == null) {
            return;
        }
        hideLoading();

        currentActivity = new WeakReference<>(activity);
        onResume(activity);

    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (activity == null) {
            return;
        }

        onPause(activity);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (activity == null) {
            return;
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        if (activity == null) {
            return;
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (activity == null) {
            return;
        }

        onDestroy(activity);
        lifecycleListenerMapByActivityMap.remove(activity);
    }

    private void logW(String message, Throwable throwable) {
        MoPubLog.w(message, throwable);
    }

    private synchronized WeakHashMap<VM5MoPubCentralManagerLifeCycleListener, Boolean> getLifecycleListenerMapByActivity(Activity activity) {
        if (activity == null) {
            return null;
        }

        WeakHashMap<VM5MoPubCentralManagerLifeCycleListener, Boolean> vm5MoPubCentralManagerLifeCycleListenerBooleanWeakHashMap = lifecycleListenerMapByActivityMap.get(activity);
        if (vm5MoPubCentralManagerLifeCycleListenerBooleanWeakHashMap == null) {
            vm5MoPubCentralManagerLifeCycleListenerBooleanWeakHashMap = new WeakHashMap<>();
            lifecycleListenerMapByActivityMap.put(activity, vm5MoPubCentralManagerLifeCycleListenerBooleanWeakHashMap);
        }

        return vm5MoPubCentralManagerLifeCycleListenerBooleanWeakHashMap;
    }
}

interface VM5MoPubCentralManagerLifeCycleListener {
    void onResume();
    void onPause();
    void onDestroy();
}


