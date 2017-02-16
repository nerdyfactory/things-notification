package com.nerdyfactory.notification;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.facebook.react.bridge.WritableNativeMap;

import java.util.Arrays;

public class NotificationListener extends NotificationListenerService {
    private static final String TAG = "NotificationListener";

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.d(TAG, "Notification received: "+sbn.getPackageName()+":"+sbn.getNotification().tickerText);

        String app = sbn.getPackageName();
        final WritableNativeMap params = new WritableNativeMap();

        if (app.equals(NotificationModule.SmsApp)) {
            params.putString("app", "sms");
        } else {
            params.putString("app", app);
        }

        if (sbn.getNotification().tickerText == null) {
            params.putString("text", "");
        } else {
            params.putString("text", sbn.getNotification().tickerText.toString());
        }

        NotificationModule.sendEvent(params);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {}
}