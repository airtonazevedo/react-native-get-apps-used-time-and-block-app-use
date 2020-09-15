package com.rnnativebockapps;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;

import android.content.pm.PackageManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.os.PowerManager;
import android.app.ActivityManager;
import android.net.Uri;
import android.app.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Objects;


import com.rvalerio.fgchecker.AppChecker;

public class BlockAppsModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private final AppChecker appChecker;

    public BlockAppsModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        this.appChecker = new AppChecker();
    }

    @Override
    public String getName() {
        return "BlockApps";
    }

    private static UsageStatsManager getUsageStatsManager(Context context) {
        UsageStatsManager usm = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        return usm;
    }

    public static List<UsageStats> getUsageStatsList(Context context) {
        UsageStatsManager usm = getUsageStatsManager(context);
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.YEAR, -1);
        long startTime = calendar.getTimeInMillis();


        List<UsageStats> usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);
        return usageStatsList;
    }
    

    @ReactMethod
    public void startBlockService() {
        reactContext.startService(new Intent(reactContext, TaskService.class));
    }
    @ReactMethod
    public void blockApp(ReadableArray currentPackage) {
        try {
            final PowerManager powerManager;
            powerManager = (PowerManager) getCurrentActivity().getSystemService(Context.POWER_SERVICE);
            int i = 0;

            appChecker.stop();

            appChecker.whenAny(new AppChecker.Listener() {
                @Override
                public void onForeground(String packageName) {
                    ActivityManager am = (ActivityManager)
                            getCurrentActivity().getSystemService(reactContext.ACTIVITY_SERVICE);
                    ArrayList<Object> arr = currentPackage.toArrayList();
                    List<String> strings = new ArrayList<>(arr.size());
                    for (Object object : arr) {
                        strings.add(Objects.toString(object, null));
                    }

                    for (String el : strings) {

                        am.killBackgroundProcesses(el);
                    }

                    PackageManager pm = getCurrentActivity().getPackageManager();
                    if (pm.getLaunchIntentForPackage(packageName) != null
                            && powerManager.isInteractive()
                            && !packageName.equals(getReactApplicationContext().getPackageName())) {
                        for (String el : strings) {
                            if (packageName.equals(el)) {
                                // Log.d("ReactNativeBleManager", "packageNameDestroy");

                                Uri uri = Uri.parse("app://domus/shared/");
                                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
                                likeIng.setPackage("com.rnnativebockapps");
                                Activity activity = getCurrentActivity();
                                activity.startActivity(likeIng);

                            }
                        }
                    }
                }
            }).timeout(1000)
                    .start(reactContext);


        } catch (Exception ex) {

        }
    }

    @ReactMethod
    public void blockOneApp(String currentPackage) {
        try {
            //Log.d("ReactNativeBleManager", "one");
            final int counter = 0;

            Long totalUsageTime = 0L;
            Long packageDuration = 0L;
            final PowerManager powerManager;
            //Log.d("ReactNativeBleManager", "two");
            powerManager = (PowerManager) getCurrentActivity().getSystemService(Context.POWER_SERVICE);
            int i = 0;

            // Log.d("ReactNativeBleManager", new AppChecker.Listener().toString());
            appChecker.stop();

            appChecker.whenAny(new AppChecker.Listener() {
                @Override
                public void onForeground(String packageName) {

                    ActivityManager am = (ActivityManager)
                            getCurrentActivity().getSystemService(reactContext.ACTIVITY_SERVICE);
                    am.killBackgroundProcesses(currentPackage);

                    PackageManager pm = getCurrentActivity().getPackageManager();
                    if (pm.getLaunchIntentForPackage(packageName) != null
                            && powerManager.isInteractive()
                            && !packageName.equals(getReactApplicationContext().getPackageName())) {

                        if (packageName.equals(currentPackage)) {
                            // Log.d("ReactNativeBleManager", "packageNameDestroy");
                            //Intent launchIntent = pm.getLaunchIntentForPackage("com.rnnativebockapps");
                            // if (launchIntent != null) {
                            //   reactContext.startActivity(launchIntent);//null pointer check in case package name was not found
                            //}
                            Uri uri = Uri.parse("app://domus/shared/");
                            Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
                            likeIng.setPackage("com.rnnativebockapps");
                            Activity activity = getCurrentActivity();
                            activity.startActivity(likeIng);

                        }
                    }
                }
            }).timeout(1000)
                    .start(reactContext);


        } catch (Exception ex) {

        }
    }
}