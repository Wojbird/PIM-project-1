import React, {useState} from 'react';
import { SafeAreaView, StyleSheet, View, Button, TextInput } from 'react-native';
import { Timer } from 'react-native-progress-timer';
import { Stop } from 'react-native-svg';
import Stopwatch from './android/lib/stopwatch';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import TimePicker from 'react-native-simple-time-picker';

function HomeScreen({ navigation }) {
 
  const [number, onChangeNumber] = React.useState(null);

  return (
    <View >
              <SafeAreaView>
                        <View style={styles.body}>
                              <TextInput
                                style={styles.input}
                                onChangeText={onChangeNumber}
                                value={number}
                                placeholder="seconds"
                                keyboardType="numeric"
                              />
                            <Timer
                              remainingTime={number}
                              size={350}
                              showsText={true}
                              animated={true}
                              direction={'counter-clockwise'}
                              borderColor= {'#d9dcdd'}
                              borderWidth= {3}
                              thickness={5}
                              color={'#faac02'}
                              style={options.style}
                              textStyle={options.textStyle}
                            ></Timer>
 
                                  <Button
                                  title="Stopwatch"
                                  onPress={() => navigation.navigate('Stoper')}
      />
                          </View>
                      </SafeAreaView>
                      
    </View>
  );
}

function StopwatchScreen({navigation}) {
  return (
    <View>
       <View style={styles.body}>    
       {/*<Button style={styles.body}  title="Timer"
       onPress={() => navigation.navigate('Home')}
  />*/}
      <Stopwatch />



  
    </View>        

    </View>
  );
}

const Stack = createNativeStackNavigator();

export default function App() {

  return (
    <NavigationContainer>
      <Stack.Navigator initialRouteName="Home">
        <Stack.Screen name="Home" component={HomeScreen} />
        <Stack.Screen name="Stoper" component={StopwatchScreen} />
      </Stack.Navigator>
    </NavigationContainer>
  );
}


const options = {
  style: {
    margin: 'auto',
  },
  textStyle:{
    color: '#000000'
  },
  view: {
    flexDirection: 'row', 
    justifyContent: 'space-between', 
    margin: 10
  },
  highlight: {
    backgroundColor: '#ffffff'
  },
  play: {
    underlayColor: '#ffffff',
    borderColor: '#d9dcdd',
    textStyle:{
      color: '#000000'
    },
    style: {
      backgroundColor: '#ffffff'
    }
  },
  cancel: {
    underlayColor: '#ffffff',
    borderColor: '#d9dcdd',
    textStyle:{
      color: '#000000'
    },
    style: {
      backgroundColor: '#ffffff'
    }
  }
}
 
const styles = StyleSheet.create({
  scrollView: {
    backgroundColor: "white",
  },
  body: {
    backgroundColor: "white",
    alignItems: 'center',
    alignContent: 'center'
  },
  body2: {
    alignItems: 'center',
    alignContent: 'center',
    paddingTop: '100%',
  },
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
  input: {
    height: 40,
    margin: 12,
    borderWidth: 1,
    padding: 10,
  },
});