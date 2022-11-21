import React, { useState, useEffect } from 'react';
import {SafeAreaView, StyleSheet, Text, View, StatusBar, TouchableOpacity, Dimensions } from 'react-native';
import * as Progress from 'react-native-progress';
import * as moment from 'moment';
require('moment-duration-format')

const screen = Dimensions.get('window');

const formatNumber = number => `0${number}`.slice(-2);

const getRemaining = (time) => {
    const mins = Math.floor(time / 60);
    const secs = time - mins * 60;
    return { mins: formatNumber(mins), secs: formatNumber(secs) };
}

const  toggles = () => {
    setIsActive(!isActive);
  }

 const reset = () => {
    setRemainingSecs(0);
    setIsActive(false);
  }
  
function Stopwatch() {
  
  const [remainingSecs, setRemainingSecs] = useState(0);
  const [isActive, setIsActive] = useState(false);
  const { mins, secs } = getRemaining(remainingSecs);


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
        <View > 
        <Text style={styles.timeCount}>{`${mins}:${secs}`}</Text>
        {/*<Text style={styles.timerText}>{`${mins}:${secs}`}</Text>*/}
        <Progress.Circle
            style={styles.progress}
            size={300}
            showsText={false}
            formatText={(remainingSecs) => {return moment.duration(remainingSecs, 'seconds').format('mm:ss', { trim: false }); }}
            progress={(remainingSecs/60)%1}
            indeterminate={false}
            direction="clockwise"
          ></Progress.Circle>
            
      </View>    
      <View style={styles.view}> 
              <TouchableOpacity onPress={() => { setRemainingSecs(0); setIsActive(false); }} style={[styles.button, styles.buttonReset]}>
                <Text style={[styles.buttonText, styles.buttonTextReset]}>Reset</Text>
            </TouchableOpacity>
            <TouchableOpacity onPress={() => { setIsActive(!isActive);}} style={styles.button}>
                <Text style={styles.buttonText}>{isActive ? 'Pause' : 'Start'}</Text>
            </TouchableOpacity>
     
            </View>     
      </View>
    );
  }


export default Stopwatch;

const styles = StyleSheet.create({
  view: {
    flex: 1,
    flexDirection: 'row',
    flexWrap: "wrap",
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingTop: '40%'
  },
timeCount: {
  position: 'absolute',
  left: '14%',
  top: '27%',
  color: '#000000',
  fontSize: 90,
  marginBottom: 20
},
container: {
  flex: 1,
  flexDirection: 'row',
  flexWrap: "wrap",
  alignItems: 'center',
  marginBottom: 30,
  justifyContent: 'center',
},
button: {
    borderWidth: 1,
    borderColor: '#000000',
    width: screen.width / 5,
    height: screen.width / 5,
    borderRadius: screen.width / 5,

    alignItems: 'center',
    justifyContent: 'center'
},
buttonText: {
    fontSize: 20,
    color: '#000000'
},
timerText: {
    color: '#000000',
    fontSize: 90,
    marginBottom: 20
},
buttonReset: {
    marginTop: 20,
    },
buttonTextReset: {
  color: "#000000"
},
viewO: {
  flexDirection: 'row', 
  justifyContent: 'center', 
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
progress: {
  margin: 10,

  alignItems: 'center',
},
});