import React, { useState, useEffect } from 'react';
import { StyleSheet, Text, View, StatusBar, TouchableOpacity, Dimensions } from 'react-native';
import StopWatch from "./android/lib/stopwatch";
import Timer from './android/lib/timer';

const screen = Dimensions.get('window');

//for stopwatch
const currentDate = new Date();
const year =
      currentDate.getMonth() === 11 && currentDate.getDate() > 23
        ? currentDate.getFullYear() + 1
        : currentDate.getFullYear();



export default function App() {
 

  return (
    <View style={styles.main}>

        {/*<StopWatch date={`${year}-11-11T22:59:00`}/>*/}
        <Timer/>
      </View>
      
        
   
  );
}

const styles = StyleSheet.create({
    main: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#07121B',
  },
  container: {
    flex: 1,
    flexDirection: 'row',
    flexWrap: "wrap",
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#07121B',
  },
  button: {
      borderWidth: 5,
      borderColor: '#B9AAFF',
      width: screen.width / 5,
      height: screen.width / 5,
      borderRadius: screen.width / 5,

      alignItems: 'center',
      justifyContent: 'center'
  },
  buttonText: {
      fontSize: 23,
      color: '#B9AAFF'
  },
  timerText: {
      color: '#fff',
      fontSize: 90,
      marginBottom: 20
  },
  buttonReset: {
      marginTop: 20,
      borderColor: "#FF851B"
  },
  buttonTextReset: {
    color: "#FF851B"
  }
});