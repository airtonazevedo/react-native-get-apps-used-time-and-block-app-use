import { NativeModules } from 'react-native';

interface BlockApp {
  blockApp: (packageNames: string[]) => void
  blockOneApp: (packageNames: string) => void

}

export interface AppInfo {

  apkDir: string,
  appName: string,
  appUsageTime: number,
  firstInstallTime: number,
  icon: string,
  lastUpdateTime: number,
  packageName: string,
  size: number,
  versionCode: number,
  versionName: string
}

interface InstalledApps {
  getNonSystemApps: () => Promise<AppInfo[]>
}

const BlockApp = NativeModules.BlockApps as BlockApp;

const GetApps = NativeModules.GetApps as InstalledApps;
export { BlockApp, GetApps }


