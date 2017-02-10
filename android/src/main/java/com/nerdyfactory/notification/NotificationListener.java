package com.nerdyfactory.notification;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.facebook.react.bridge.WritableNativeMap;

import java.util.Arrays;

public class NotificationListener extends NotificationListenerService {
    private static final String TAG = "NotificationListener";
    private final static String[] apps = {
            "com.google.android.talk",      // TODO remove after testing
            "com.kakao.talk",
            "jp.naver.line.android",
            "com.facebook.katana",
            "com.twitter.android",
            "com.google.android.gm",
            "com.nhn.android.search",
            "com.korail.korail",
            "com.starbucks.co",
            "com.tencent.mm"
    };

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.d(TAG, "Notification received: "+sbn.getPackageName()+":"+sbn.getNotification().tickerText);

        String app = sbn.getPackageName();

        if (!Arrays.asList(apps).contains(app) && !NotificationModule.SmsApp.equals(app)) {
            return;
        }

        final WritableNativeMap params = new WritableNativeMap();

        if (NotificationModule.SmsApp.equals(app)) {
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