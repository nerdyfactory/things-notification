/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import { AppRegistry, StyleSheet, Text, View, Alert, Platform } from 'react-native';

import Notification from 'react-native-notification-listener';

export default class NotificationListener extends Component {

  constructor(props) {
    super(props);
    this.state = {
      sender: '',
      text: '',
    };
  }

  componentDidMount() {
    //Notification.test((msg) => {
    //  console.log("msg received", msg);
    //});

    if(Platform.OS === 'android' && Platform.Version >= 23) {
      this._checkPermission();
    }

    this.subscription = Notification.on('notification', (data) => {
      console.log("notification received:", JSON.stringify(data));
      this.setState({
        sender: data.packageName,
        text: data.tickerText
      });
    });
  }

  componentWillUnmount() {
    this.subscription.remove();
  }

  _checkPermission = () => {
    Notification.getPermissionStatus()
      .then(response => {
        if (response === 'denied') {
          this._requestPermission();
        }
      });
  }

  _requestPermission = () => {
    Alert.alert(
      'Can we access your notifications?',
      'We need access so that blah blah blah',
      [
        {text: 'No', onPress: () => console.log('permission denied')},
        {text: 'OK', onPress: Notification.requestPermission},
      ]
    )
  }
  

  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>
          Notification Listener!
        </Text>
        <Text style={styles.instructions}>
          Sender: {this.state.sender}
        </Text>
        <Text style={styles.instructions}>
          Text: {this.state.text}
        </Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});

AppRegistry.registerComponent('NotificationListener', () => NotificationListener);
