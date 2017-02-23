package com.nerdyfactory.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.facebook.react.bridge.WritableNativeMap;

public class CallListener extends BroadcastReceiver {
    private static final String TAG = "CallListener";

    @Override
    public void onReceive(final Context context, Intent intent) {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                Log.d(TAG, "Call received: "+incomingNumber);
                if (!incomingNumber.isEmpty()) {
                    WritableNativeMap params = new WritableNativeMap();
                    params.putString("app", "phone");
                    params.putString("text", incomingNumber);
                    NotificationModule.sendEvent("notificationReceived", params);
                }
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
    }
}
