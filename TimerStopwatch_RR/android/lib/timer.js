import React, { useState, useEffect } from 'react';
import { StyleSheet, Text, View, StatusBar, TouchableOpacity, Dimensions } from 'react-native';

const screen = Dimensions.get('window');

const formatNumber = number => `0${number}`.slice(-2);

const getRemaining = (time) => {
    const mins = Math.floor(time / 60);
    const secs = time - mins * 60;
    return { mins: formatNumber(mins), secs: formatNumber(secs) };
}

function Timer() {
  
  const [remainingSecs, setRemainingSecs] = useState(0);
  const [isActive, setIsActive] = useState(false);
  const { mins, secs } = getRemaining(remainingSecs);

  toggle = () => {
    setIsActive(!isActive);
  }

  reset = () => {
    setRemainingSecs(0);
    setIsActive(false);
  }

  useEffect(() => {
    let interval = null;
    if (isActive) {
      interval = setInterval(() => {
        setRemainingSecs(remainingSecs => remainingSecs + 1);
      }, 1000);
    } else if (!isActive && remainingSecs !== 0) {
      clearInterval(interval);
    }
    return () => clearInterval(interval);
  }, [isActive, remainingSecs]);



    return (
      <View>
        <StatusBar barStyle="light-content" />
      <Text style={styles.timerText}>{`${mins}:${secs}`}</Text>
        <View style={styles.container}>
          <TouchableOpacity onPress={this.toggle} style={styles.button}>
              <Text style={styles.buttonText}>{isActive ? 'Pause' : 'Start'}</Text>
          </TouchableOpacity>
          <TouchableOpacity onPress={this.reset} style={[styles.button, styles.buttonReset]}>
              <Text style={[styles.buttonText, styles.buttonTextReset]}>Reset</Text>
          </TouchableOpacity>
          <TouchableOpacity onPress={() => {alert("hell_o")}} style={[styles.button, styles.buttonReset]}>
              <Text style={[styles.buttonText, styles.buttonTextReset]}>Timer</Text>
          </TouchableOpacity>
      </View>
      </View>
    );
  }


export default Timer;

const styles = StyleSheet.create({

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