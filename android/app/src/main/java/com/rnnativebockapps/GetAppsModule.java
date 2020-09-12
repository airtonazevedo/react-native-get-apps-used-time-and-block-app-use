package com.rnnativebockapps;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.Arguments;

import android.content.pm.PackageInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.Map;
import java.io.File;
import java.util.Calendar;


import com.helper.*;

public class GetAppsModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public GetAppsModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "GetApps";
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
    public void getNonSystemApps(Promise promise) {
        try {
            Log.d("ReactNativeBleManager", "Chega aqui meu papi");

            PackageManager pm = this.reactContext.getPackageManager();
            List<PackageInfo> pList = pm.getInstalledPackages(0);
            WritableArray list = Arguments.createArray();

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 0);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            long start = calendar.getTimeInMillis();
            long end = System.currentTimeMillis();


            for (int i = 0; i < pList.size(); i++) {
                PackageInfo packageInfo = pList.get(i);
                WritableMap appInfo = Arguments.createMap();

                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    appInfo.putString("packageName", packageInfo.packageName);
                    appInfo.putString("versionName", packageInfo.versionName);
                    appInfo.putDouble("versionCode", packageInfo.versionCode);
                    appInfo.putDouble("firstInstallTime", (packageInfo.firstInstallTime));
                    appInfo.putDouble("lastUpdateTime", (packageInfo.lastUpdateTime));
                    appInfo.putString("appName", ((String) packageInfo.applicationInfo.loadLabel(pm)).trim());

                    Drawable icon = pm.getApplicationIcon(packageInfo.applicationInfo);
                    appInfo.putString("icon", Utility.convert(icon));

                    String apkDir = packageInfo.applicationInfo.publicSourceDir;
                    appInfo.putString("apkDir", apkDir);

                    UsageStatsManager usageStatsManager = (UsageStatsManager) this.reactContext.getSystemService(Context.USAGE_STATS_SERVICE);

                    try {

                        Map<String, UsageStats> stats = usageStatsManager.queryAndAggregateUsageStats(start, end);

                        long totalTimeUsageInMillis = stats.get(packageInfo.packageName).
                                getTotalTimeInForeground();

                        double usageTime = (double) totalTimeUsageInMillis;
                        // Log.d("ReactNativeBleManager", String.valueOf(usageTime) );

                        appInfo.putDouble("appUsageTime", usageTime);
                    } catch (Exception ex) {
                        appInfo.putDouble("appUsageTime", 0);

                    }
                    File file = new File(apkDir);
                    double size = file.length();
                    appInfo.putDouble("size", size);

                    list.pushMap(appInfo);
                }
            }
            promise.resolve(list);
        } catch (Exception ex) {
            promise.reject(ex);
        }

    }

}
