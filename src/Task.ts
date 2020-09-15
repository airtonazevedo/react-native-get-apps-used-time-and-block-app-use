import AsyncStorage from "@react-native-community/async-storage";
import BackgroundFetch, { HeadlessEvent } from "react-native-background-fetch";
import { BlockApp } from "./CustomNativeModules";

const CheckApps = async () => {
  const blockedAppsJSON = await AsyncStorage.getItem('@BlockedApps');
  if (blockedAppsJSON) {
    const blockedApps = JSON.parse(blockedAppsJSON);
    BlockApp.blockApp(blockedApps);
  }
}

const ScheduleTask = async (name: string) => {
  try {
    await BackgroundFetch.scheduleTask({
      taskId: name,
      stopOnTerminate: false,
      requiredNetworkType: BackgroundFetch.NETWORK_TYPE_NONE,
      requiresBatteryNotLow: false,
      requiresCharging: false,
      requiresDeviceIdle: false,
      requiresStorageNotLow: false,
      startOnBoot: true,
      enableHeadless: true,
      delay: 2000,               // milliseconds (5s)
      forceAlarmManager: true,   // more precise timing with AlarmManager vs default JobScheduler
      periodic: true            // Fire once only.
    });
  } catch (e) {
    console.warn('[BackgroundFetch] scheduleTask fail', e);
  }
}


const BackgroundFetchConfig = () => {
  BackgroundFetch.configure({
    minimumFetchInterval: 15,     // <-- minutes (15 is minimum allowed)
    // Android options

    forceAlarmManager: true,     // <-- Set true to bypass JobScheduler.
    stopOnTerminate: false,
    startOnBoot: true,
    requiredNetworkType: BackgroundFetch.NETWORK_TYPE_NONE, // Default
    requiresCharging: false,      // Default
    requiresDeviceIdle: false,    // Default
    requiresBatteryNotLow: false, // Default
    enableHeadless: true,
    requiresStorageNotLow: false  // Default

  }, async (taskId) => {
    console.log("[js] Received background-fetch event: ", taskId);

    if (taskId == 'react-native-background-fetch') {
      await ScheduleTask('com.rnnativemodule.checkapps')
    }

    if (taskId == 'com.rnnativemodule.checkapps') {
      console.log('scherudle task!!!');
      CheckApps()
    }

    BackgroundFetch.finish(taskId);
  }, (error) => {
    console.log("[js] RNBackgroundFetch failed to start");
  });

  BackgroundFetch.status((status) => {
    switch (status) {
      case BackgroundFetch.STATUS_RESTRICTED:
        console.log("BackgroundFetch restricted");
        break;
      case BackgroundFetch.STATUS_DENIED:
        console.log("BackgroundFetch denied");
        break;
      case BackgroundFetch.STATUS_AVAILABLE:
        console.log("BackgroundFetch is enabled");
        break;
    }
  });
}


let BackgroundTask = async (event: HeadlessEvent) => {
  console.log(event)
  if (event.taskId == 'com.rnnativemodule.checkapps') {
    CheckApps()
  }
  BackgroundFetch.finish(event.taskId);
}

export { BackgroundFetchConfig, ScheduleTask, CheckApps, BackgroundTask }