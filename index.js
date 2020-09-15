/**
 * @format
 */

import { AppRegistry } from 'react-native';
import App from './App';
import { name as appName } from './app.json';
import 'react-native-gesture-handler';
import { BackgroundTask } from './src/Task';
import BackgroundFetch from 'react-native-background-fetch'

//BackgroundFetch.registerHeadlessTask(BackgroundTask);

AppRegistry.registerComponent(appName, () => App);
