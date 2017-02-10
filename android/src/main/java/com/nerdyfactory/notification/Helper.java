package com.nerdyfactory.notification;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.provider.Settings;
import android.provider.Telephony;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class Helper {
    private ReactApplicationContext mContext;

    public Helper() {
        super();
    }

    public Helper(ReactApplicationContext context) {
        mContext = context;
    }

    public String getDefaultSmsPackage() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Telephony.Sms.getDefaultSmsPackage(mContext);
        } else {
            String defApp = Settings.Secure.getString(mContext.getContentResolver(), "sms_default_application");
            PackageManager pm = mContext.getApplicationContext().getPackageManager();
            Intent iIntent = pm.getLaunchIntentForPackage(defApp);
            ResolveInfo mInfo = pm.resolveActivity(iIntent,0);
            return mInfo.activityInfo.packageName;
        }
    }

    public void sendEvent(WritableNativeMap params) {
        mContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit("notificationReceived", params);
    }
}
