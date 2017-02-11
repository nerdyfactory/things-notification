package com.nerdyfactory.notification;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.provider.Settings;
import android.provider.Telephony;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.Set;

public class NotificationModule extends ReactContextBaseJavaModule implements ActivityEventListener {
    private static final String TAG = "NotificationModule";
    private static ReactApplicationContext reactContext;
    public static String SmsApp;

    public NotificationModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        //this.reactContext.addActivityEventListener(this);
        reactContext.addActivityEventListener(this);

        SmsApp = getDefaultSmsPackage();
        //Log.d(TAG, "sms app: "+SmsApp);
    }

    @Override
    public String getName() {
        return "NotificationModule";
    }

    private String getDefaultSmsPackage() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Telephony.Sms.getDefaultSmsPackage(reactContext);
        } else {
            String defApp = Settings.Secure.getString(reactContext.getContentResolver(), "sms_default_application");
            PackageManager pm = reactContext.getApplicationContext().getPackageManager();
            Intent iIntent = pm.getLaunchIntentForPackage(defApp);
            ResolveInfo mInfo = pm.resolveActivity(iIntent,0);
            return mInfo.activityInfo.packageName;
        }
    }

    public static void sendEvent(WritableNativeMap params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit("notificationReceived", params);
    }

    @ReactMethod
    public void getPermissionStatus(Promise promise) {
        String packageName = reactContext.getPackageName();
        Set<String> enabledPackages = NotificationManagerCompat.getEnabledListenerPackages(reactContext);
        if (enabledPackages.contains(packageName)) {
            promise.resolve("authorized");
        } else {
            promise.resolve("denied");
        }
    }

    @ReactMethod
    public void requestPermission() {
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        reactContext.startActivity(i);
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {}

    @Override
    public void onNewIntent(Intent intent){}
}
