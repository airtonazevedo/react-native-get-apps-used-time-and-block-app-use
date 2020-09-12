/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * Generated with the TypeScript template
 * https://github.com/react-native-community/react-native-template-typescript
 *
 * @format
 */

import React, { useEffect, useState, useCallback } from 'react';
import {
  SafeAreaView,
  StyleSheet,
  ScrollView,
  View,
  Text,
  StatusBar,
  NativeModules,
  FlatList,
  Image,
  Button,
  ActivityIndicator,
  Alert
} from 'react-native';
import AsyncStorage from '@react-native-community/async-storage';
import moment from 'moment'
import { AppInfo, BlockApp, GetApps } from './CustomNativeModules';
import 'moment/locale/pt-br';
import BGLocation from './bgGeolocation'

moment.updateLocale('pt-br', {
  calendar: {
    lastDay: '[Ontem]',
    sameDay: 'LT',
    nextDay: '[Amanhã] LT',
    lastWeek: 'L',
    nextWeek: 'L',
    sameElse: 'L',
  }
});

declare const global: { HermesInternal: null | {} };




const App = () => {
  const [apps, setApps] = useState<AppInfo[]>([])
  const [loading, setLoading] = useState(false);
  const [blocked, setBlocked] = useState<string[]>([])

  const handleGetApps = useCallback(async () => {
    setLoading(true);

    const getApps = async () => {
      try {
        const myapps = (await GetApps.getNonSystemApps())
        setApps(myapps);
      } catch (err) {
        console.log(err)
      }

      setLoading(false)
    }
    setTimeout(async () => await getApps(), 1000);


  }, [])

  useEffect(() => {
    if (blocked.length)
      BlockApp.blockApp(blocked);

  }, [blocked])

  const handleBlockApp = (app: string) => {
    setBlocked([...blocked, app])
  }

  const handleUnBlockApp = (app: string) => {
    let blocks = [...blocked];
    const index = blocks.findIndex(b => b == app);
    if (index > -1)
      blocks.splice(index, 1);
    setBlocked(blocks);

  }

  return (
    <>
      <StatusBar barStyle="dark-content" />
      <BGLocation enable={true}></BGLocation>

      <SafeAreaView>
        <Button disabled={loading} onPress={handleGetApps} title={"Ver apps"} />

        {
          loading == true && <ActivityIndicator color={'#1212dd'} />
        }
        <FlatList
          data={apps}
          renderItem={({ item }) => <Item blockApp={handleBlockApp} unblockApp={handleUnBlockApp} app={item} blocked={blocked} />}
          keyExtractor={(item, index) => index.toString()}

        />
      </SafeAreaView>
    </>
  );
};

interface ItemProps {
  app: AppInfo,
  blocked: string[]
  unblockApp: (app: string) => void
  blockApp: (app: string) => void
}

const Item: React.FC<ItemProps> = ({ app, blocked, blockApp, unblockApp }) => {

  return (
    <View style={{ marginVertical: 5, borderWidth: StyleSheet.hairlineWidth, borderColor: '#444', padding: 20 }}>
      <Image style={{ width: 64, height: 64 }} source={{ uri: "data:image/png;base64," + app.icon }} />
      <View>
        <Text>{'dir: ' + app.apkDir}</Text>
        <Text>{'name: ' + app.appName}</Text>
        <Text>{'usege time: ' + moment.utc(app.appUsageTime).format('H:mm:ss')}</Text>
        <Text>{'install time: ' + moment(app.firstInstallTime).format('L')}</Text>
        <Text>{'last updated time: ' + moment(app.lastUpdateTime).format('L')}</Text>
        <Text>{'package name: ' + app.packageName}</Text>
        <Text>{'size (bytes): ' + app.size}</Text>
        <Text>{'version code: ' + app.versionCode}</Text>
        <Text>{'version name: ' + app.versionName}</Text>

      </View>
      { blocked.findIndex(b => b == app.packageName) == -1 ?
        <Button onPress={() => Alert.alert("Atenção", "Deseja bloquear esse app?",
          [
            {
              onPress: () => false,
              style: 'cancel',
              text: 'Não'
            },
            {
              onPress: () => blockApp(app.packageName),
              text: 'Sim'
            }
          ]
        )}
          title={"Bloquear app"}
        /> :
        <Button color={'red'} onPress={() => Alert.alert("Atenção", "Deseja desbloquear esse app?",
          [
            {
              onPress: () => false,
              style: 'cancel',
              text: 'Não'
            },
            {
              onPress: () => unblockApp(app.packageName),
              text: 'Sim'
            }
          ]
        )}
          title={"Desbloquear app"}
        />
      }
    </View>)
}

const styles = StyleSheet.create({

});

export default App;
