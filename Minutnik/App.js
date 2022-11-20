 
import React from 'react';
import { SafeAreaView, StyleSheet, View } from 'react-native';
import { Timer } from 'react-native-progress-timer';
import Stopwatch from './android/lib/stopwatch';


export default function App() {
  return (
    <>
      <SafeAreaView>
          <View style={styles.body}>
              <View><Timer
                remainingTime={60}
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
              ></Timer></View>
            </View>
  </SafeAreaView>
  {/*<Stopwatch/>*/}
    </>
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
});