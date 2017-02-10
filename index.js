import {NativeModules, DeviceEventEmitter} from 'react-native';

const eventsMap = {
    notification: 'notificationReceived'
};
const NotificationModule = NativeModules.NotificationModule;
const Notification = {};

//Notification.test = (callback) => {
//  NotificationModule.test(callback);
//}

Notification.getPermissionStatus = () => {
  return NotificationModule.getPermissionStatus();
}

Notification.requestPermission = () => {
  return NotificationModule.requestPermission();
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
