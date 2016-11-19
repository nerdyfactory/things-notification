package com.nerdyfactory.notification;

import android.os.Handler;
import android.os.Looper;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class NLService extends NotificationListenerService {

    private static final String TAG = "NLService";

    private void handleNotification(ReactApplicationContext context, StatusBarNotification sbn) {
        WritableNativeMap params = new WritableNativeMap();
        params.putString("packageName", sbn.getPackageName());
        params.putString("tickerText", sbn.getNotification().tickerText.toString());

        context
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit("notificationReceived", params);
    }


    @Override
    public void onNotificationPosted(final StatusBarNotification sbn) {
        Log.d(TAG, "Notification received: "+sbn.getPackageName()+":"+sbn.getNotification().tickerText);

        //Following code is from https://github.com/zo0r/react-native-push-notification
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                ReactInstanceManager mReactInstanceManager = ((ReactApplication) getApplication()).getReactNativeHost().getReactInstanceManager();
                ReactContext context = mReactInstanceManager.getCurrentReactContext();
                if (context != null) {
                    handleNotification((ReactApplicationContext) context, sbn);
                } else {
                    mReactInstanceManager.addReactInstanceEventListener(new ReactInstanceManager.ReactInstanceEventListener() {
                        public void onReactContextInitialized(ReactContext context) {
                            handleNotification((ReactApplicationContext) context, sbn);
                        }
                    });
                    if (!mReactInstanceManager.hasStartedCreatingInitialContext()) {
                        mReactInstanceManager.createReactContextInBackground();
                    }
                }
            }
        });
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
    }
}