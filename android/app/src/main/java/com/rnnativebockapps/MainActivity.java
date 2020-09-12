package com.rnnativebockapps;

import com.facebook.react.ReactActivity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

public class MainActivity extends ReactActivity {

  /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */
  @Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (BlockAppsModule.getUsageStatsList(this).isEmpty()){
      Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
      startActivity(intent);
  }
}

  @Override
  protected String getMainComponentName() {
    return "RNNativeBockApps";
  }
}
