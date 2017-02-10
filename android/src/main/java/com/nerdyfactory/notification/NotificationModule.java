package com.nerdyfactory.notification;

import android.app.Activity;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableNativeMap;

import java.util.Set;

public class NotificationModule extends ReactContextBaseJavaModule implements ActivityEventListener {
    private static final String TAG = "NotificationModule";
    private final ReactApplicationContext reactContext;
    private static Helper mHelper;
    public static String SmsApp;

    public NotificationModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mHelper = new Helper(reactContext);
        SmsApp = mHelper.getDefaultSmsPackage();
        this.reactContext = reactContext;
        //this.reactContext.addActivityEventListener(this);
        reactContext.addActivityEventListener(this);

    }

    @Override
    public String getName() {
        return "NotificationModule";
    }

    public static void sendEvent(WritableNativeMap params) {
        mHelper.sendEvent(params);
    }

    @ReactMethod
    public void getPermissionStatus(Promise promise) {
        String packageName = this.reactContext.getPackageName();
        Set<String> enabledPackages = NotificationManagerCompat.getEnabledListenerPackages(this.reactContext);
        if (enabledPackages.contains(packageName)) {
            promise.resolve("authorized");
        } else {
            promise.resolve("denied");
        }
    }

    @ReactMethod
    public void requestPermission() {
        final Intent i = new Intent();
        //i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.setAction(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
        //i.addCategory(Intent.CATEGORY_DEFAULT);
        //i.setData(Uri.parse("package:" + this.reactContext.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        this.reactContext.startActivity(i);
    }

    @ReactMethod
    public void test(Callback callback) {
        callback.invoke("test");
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {}

    @Override
    public void onNewIntent(Intent intent){}
}
