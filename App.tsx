/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * Generated with the TypeScript template
 * https://github.com/react-native-community/react-native-template-typescript
 *
 * @format
 */

import * as React from 'react';
import { NavigationContainer, NavigationContainerRef, useLinking } from '@react-navigation/native';
import moment from 'moment'
import { createStackNavigator } from '@react-navigation/stack';
import HomeScreen from './src/App'
import DetailsScreen from './src/AppBlocked';
import { useEffect } from 'react';

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



const Stack = createStackNavigator();

const App = () => {

  const ref = React.useRef<NavigationContainerRef>(null);
  const { getInitialState } = useLinking(ref, {
    prefixes: ['https://domus.com', 'domus://', 'app://domus'],
    config: {
      screens: {
        Home: "/",
        Details: 'shared/'
      }
    },
  });

  const [isReady, setIsReady] = React.useState(false);
  const [initialState, setInitialState] = React.useState<any>();

  useEffect(() => {
    getInitialState()
      .then(state => {
        if (state !== undefined) {
          console.log('initial state', JSON.stringify(state))
          setInitialState(state);

        }
        //console.log('state ' + state)
        setIsReady(true);
      });
  }, [getInitialState]);

  return (
    <NavigationContainer initialState={initialState} ref={ref}>
      <Stack.Navigator>
        <Stack.Screen name="Home" component={HomeScreen} />
        <Stack.Screen name="Details" component={DetailsScreen} />
      </Stack.Navigator>
    </NavigationContainer>
  );
};


export default App;
