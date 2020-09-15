package com.rnnativebockapps;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import android.content.pm.PackageManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
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

public class TaskService extends Service {

  private static final int NOTIF_ID = 1;
  private static final String NOTIF_CHANNEL_ID = "domus_locker";

  private final AppChecker appChecker;

  public TaskService() {
    this.appChecker = new AppChecker();
}


  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
      return null;
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId){
      // do your jobs here
      String currentPackage = "br.com.voeazul";
      //BlockAppsModule.blockApp(currentPackage);
      Context reactContext = this;

      try {
        //Log.d("ReactNativeBleManager", "one");
        final int counter = 0;

        Long totalUsageTime = 0L;
        Long packageDuration = 0L;
        final PowerManager powerManager;
        //Log.d("ReactNativeBleManager", "two");
        powerManager = (PowerManager) getSystemService(reactContext.POWER_SERVICE);
        int i = 0;

        // Log.d("ReactNativeBleManager", new AppChecker.Listener().toString());
        appChecker.stop();

        appChecker.whenAny(new AppChecker.Listener() {
            @Override
            public void onForeground(String packageName) {

                ActivityManager am = (ActivityManager)
                        getSystemService(reactContext.ACTIVITY_SERVICE);
                am.killBackgroundProcesses(currentPackage);
                
                PackageManager pm = getPackageManager();
                if (pm.getLaunchIntentForPackage(packageName) != null
                        && powerManager.isInteractive()
                        && !packageName.equals(reactContext.getPackageName())) {

                    if (packageName.equals(currentPackage)) {
                        // Log.d("ReactNativeBleManager", "packageNameDestroy");
                        //Intent launchIntent = pm.getLaunchIntentForPackage("com.rnnativebockapps");
                        // if (launchIntent != null) {
                        //   reactContext.startActivity(launchIntent);//null pointer check in case package name was not found
                        //}
                        Uri uri = Uri.parse("app://domus/shared/");
                        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
                        likeIng.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //likeIng.setPackage("com.rnnativebockapps");
                        
                        startActivity(likeIng);

                    }
                }
            }
        }).timeout(1000)
                .start(reactContext);


    } catch (Exception ex) {

    }

      //Uri uri = Uri.parse("app://domus/shared/");
      //Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
      //likeIng.setPackage("com.rnnativebockapps");
      //Activity activity = context.getCurrentActivity();
      //likeIng.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      //startActivity(likeIng);

      startForeground();

      return super.onStartCommand(intent, flags, startId);
  }



  private void startForeground() {
      Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);

      PendingIntent pendingIntent = null;

      if (notificationIntent != null){
          pendingIntent = PendingIntent.getActivity(this, 0,
                  notificationIntent, 0);
      }
      NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
      if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
          NotificationChannel notificationChannel = new NotificationChannel(NOTIF_CHANNEL_ID,
                  "DomusLockerRN", NotificationManager.IMPORTANCE_DEFAULT);
          notificationChannel.setDescription("DomusLockerRN");
          notificationChannel.enableLights(true);
          notificationChannel.setLightColor(Color.RED);
          notificationChannel.setVibrationPattern(new long[]{0,0,0,0});
          notificationChannel.enableVibration(true);

          notificationManager.createNotificationChannel(notificationChannel);
      }
      startForeground(NOTIF_ID, new NotificationCompat.Builder(getApplicationContext(),
              NOTIF_CHANNEL_ID) // don't forget create a notification channel first
              .setOngoing(true)
              .setContentTitle(getString(R.string.app_name))
              .setContentText("Service is running background")
              .setContentIntent(pendingIntent)
              .build());
  }

}