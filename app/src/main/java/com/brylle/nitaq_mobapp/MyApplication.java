package com.brylle.nitaq_mobapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.util.Log;

import com.hypelabs.hype.Error;
import com.hypelabs.hype.Hype;
import com.hypelabs.hype.Instance;
import com.hypelabs.hype.Message;
import com.hypelabs.hype.MessageInfo;
import com.hypelabs.hype.MessageObserver;
import com.hypelabs.hype.NetworkObserver;
import com.hypelabs.hype.StateObserver;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MyApplication extends Application {

    public static String announcement = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;
    //    private static final String TAG = MyApplication.class.getName();
    private static final String TAG = "DEBUG";
    private Map<String, Store> stores;
    private Activity activity;
    private boolean isConfigured = false;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public Map<String, Store> getStores() {

        if (stores == null) {
            stores = new HashMap<>();
        }

        return stores;
    }
}

