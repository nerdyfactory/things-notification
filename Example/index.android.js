import React, { Component } from 'react';
import { AppRegistry, StyleSheet, Text, View, Alert, PermissionsAndroid, TouchableOpacity, AppState, Image } from 'react-native';
import Notification from 'things-notification';

const PERMISSION = PermissionsAndroid.PERMISSIONS.READ_PHONE_STATE;

export default class NotificationListener extends Component {

  constructor(props) {
    super(props);
    this.state = {
      sender: '',
      text: '',
      apps: [],
    };
  }

  componentDidMount() {
    this._checkPermissionPhoneState();
    this._checkPermissionNotification();

    Notification.on('notification', (data) => {
      //console.log("AppState", AppState.currentState);
      console.log("notification received", JSON.stringify(data));
      if (AppState.currentState === 'active') {
        this.setState({
          sender: data.app,
          text: data.text
        });
      }
    });
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
  
  getInstalledApps = () => {
    Notification.getInstalledApps()
      .then(response => {
        console.log("Installed Apps", JSON.stringify(response));
        this.setState({apps: response});
      });
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
        <TouchableOpacity onPress={this.getInstalledApps}>
          <Text style={styles.button}>Get Installed Apps</Text>
        </TouchableOpacity>
        <View style={styles.row}>
          {this.state.apps.map((app,i) => (
            <Image source={{uri: app.icon}} style={styles.image} key={i}/>
          ))}
        </View>
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
  button: {
    fontSize: 20,
    textDecorationLine: 'underline',
    paddingVertical: 20,
  },
  row: {
    flexDirection: 'row',
    flexWrap: 'wrap',
  },
  image: {
    width: 25,
    height: 25,
  },
});

AppRegistry.registerComponent('NotificationListener', () => NotificationListener);
