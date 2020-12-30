package com.core.adnsdkdemo;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.core.adnsdk.ADN;
import com.mopub.simpleadsdemo.VM5MoPubCentralManager;
//import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Shawn on 11/18/15.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        LeakCanary.install(this);

        VM5MoPubCentralManager.getInstance().setup(this);
    }
}
