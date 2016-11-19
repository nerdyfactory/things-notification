package com.nerdyfactory.notification;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.Set;

public class NLModule extends ReactContextBaseJavaModule implements ActivityEventListener {
    private static final String TAG = "NLModule";
    private final ReactApplicationContext reactContext;

    public NLModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        this.reactContext.addActivityEventListener(this);
    }

    @Override
    public String getName() {
        return "NLModule";
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
        callback.invoke("Yo wassup");
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
    }

    @Override
    public void onNewIntent(Intent intent){
        //Log.d(TAG, "onNewIntent");
    }
}
