import React, { Component } from 'react';
import { AppRegistry, StyleSheet, Text, View, Alert, PermissionsAndroid } from 'react-native';
import Notification from 'things-notification';

const PERMISSION = PermissionsAndroid.PERMISSIONS.READ_PHONE_STATE;

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

    this._checkPermissionPhoneState();
    this._checkPermissionNotification();

    this.subscription = Notification.on('notification', (data) => {
      console.log("notification received:", JSON.stringify(data));
      this.setState({
        sender: data.app,
        text: data.text
      });
    });
  }

  componentWillUnmount() {
    this.subscription.remove();
  }

  _checkPermissionPhoneState = () => {
    PermissionsAndroid.checkPermission(PERMISSION).then((result) => {
      if (!result) {
        PermissionsAndroid.requestPermission(PERMISSION).then((result) => {
          if (result) {
            console.log("User accepted read phone state permission");
          } else {
            console.log("User refused read phone state permission");
          }
        });
      }
    });
  }

  _checkPermissionNotification = () => {
    Notification.getPermissionStatus()
      .then(response => {
        if (response === 'denied') {
          this._requestPermissionNotification();
        }
      });
  }

  _requestPermissionNotification = () => {
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
