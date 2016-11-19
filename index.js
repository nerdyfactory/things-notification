import {NativeModules, DeviceEventEmitter} from 'react-native';

const eventsMap = {
    notification: 'notificationReceived'
};
const NotificationListener = NativeModules.NLModule;
const Notification = {};

//Notification.test = (callback) => {
//  NotificationListener.test(callback);
//}

Notification.getPermissionStatus = () => {
  return NotificationListener.getPermissionStatus();
}

Notification.requestPermission = () => {
  return NotificationListener.requestPermission();
}

Notification.on = (event, callback) => {
  const nativeEvent = eventsMap[event];
  if (!nativeEvent) {
    throw new Error('Invalid event');
  }
  return DeviceEventEmitter.addListener(nativeEvent, callback);
  //const listener = DeviceEventEmitter.addListener(nativeEvent, callback);
  //return listener;
  //return function remove() {
  //  listener.remove();
  //};
}

module.exports = Notification;
