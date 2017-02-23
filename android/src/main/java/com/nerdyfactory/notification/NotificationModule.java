package com.nerdyfactory.notification;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.provider.Telephony;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Set;

public class NotificationModule extends ReactContextBaseJavaModule implements ActivityEventListener {
    private static final String TAG = "NotificationModule";
    private static ReactApplicationContext reactContext;
    private String thisApp;
    public static String smsApp;

    public NotificationModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        //this.reactContext.addActivityEventListener(this);
        reactContext.addActivityEventListener(this);

        thisApp = reactContext.getPackageName();
        smsApp = getDefaultSmsPackage();
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

    public static void sendEvent(String event, WritableNativeMap params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(event, params);
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

    private static String saveIcon(String appPackage, Drawable icon){
        Bitmap bitmap = ((BitmapDrawable) icon).getBitmap();
        File file = new File(reactContext.getFilesDir(), appPackage);
        if(!file.exists()) {
            try {
                FileOutputStream fo = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fo);
                fo.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "file://"+file.getAbsolutePath();
    }

    @ReactMethod
    public void getInstalledApps(Promise promise) {
        WritableNativeArray params = new WritableNativeArray();
        PackageManager pm = reactContext.getPackageManager();
        List<ApplicationInfo> apps = pm.getInstalledApplications(0);
        for(ApplicationInfo app : apps) {
            if ((pm.getLaunchIntentForPackage(app.packageName) != null) &&
                    !thisApp.equals(app.packageName)) {
                WritableNativeMap param = new WritableNativeMap();
                String appName = app.loadLabel(pm).toString();
                String appPackage = app.packageName;
                String appIcon = saveIcon(appPackage, app.loadIcon(pm));
                //Log.d(TAG, "name: "+appName);
                //Log.d(TAG, "app: "+appPackage);
                //Log.d(TAG, "icon: "+appIcon);
                param.putString("name", appName);
                param.putString("app", appPackage);
                param.putString("icon", appIcon);
                //sendEvent("installedApps", param);
                params.pushMap(param);
            }
        }
        promise.resolve(params);
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {}

    @Override
    public void onNewIntent(Intent intent){}
}
