import {NativeModules, DeviceEventEmitter} from 'react-native';

const eventsMap = {
    notification: 'notificationReceived',
    //installedApps: 'installedApps',
};
const NotificationModule = NativeModules.NotificationModule;
const Notification = {};

Notification.getPermissionStatus = () => {
  return NotificationModule.getPermissionStatus();
}

Notification.requestPermission = () => {
  return NotificationModule.requestPermission();
}

Notification.getInstalledApps = () => {
  return NotificationModule.getInstalledApps();
}

Notification.on = (event, callback) => {
  const nativeEvent = eventsMap[event];
  if (!nativeEvent) {
    throw new Error('Invalid event');
  }
  DeviceEventEmitter.removeAllListeners(nativeEvent);
  return DeviceEventEmitter.addListener(nativeEvent, callback);
}

module.exports = Notification;
