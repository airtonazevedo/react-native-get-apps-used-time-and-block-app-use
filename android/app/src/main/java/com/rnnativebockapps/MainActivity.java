package com.rnnativebockapps;

import com.facebook.react.ReactActivity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;
import androidx.annotation.Nullable;

public class MainActivity extends ReactActivity {
  public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE= 2323;
  /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */
  @Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if(!Settings.canDrawOverlays(this)){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      // Show alert dialog to the user saying a separate permission is needed
      // Launch the settings activity if the user prefers
      Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
              Uri.parse("package:" + this.getPackageName()));
      startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
  }
    }
    if (BlockAppsModule.getUsageStatsList(this).isEmpty()){
      Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
      startActivity(intent);
  }
}

@Override
public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(getApplicationContext())) {
                Toast.makeText(this, "PERMISSÃO NÃO ACEITA", Toast.LENGTH_SHORT).show();
            } else {
                // Permission Granted-System will work
            }

        }
    }
}


  @Override
  protected String getMainComponentName() {
    return "RNNativeBockApps";
  }
}
