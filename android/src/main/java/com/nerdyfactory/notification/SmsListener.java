/**
 *  SMS messages are fetched through NotificationListener. Following code is just an
 *  alternative. If you plan to use this code make sure to add following in manifest
 *  and also handle runtime permissions.
 *  <uses-permission android:name="android.permission.RECEIVE_SMS"/>
 *  <receiver android:name=".SmsListener" >
 *      <intent-filter>
 *          <action android:name="android.provider.Telephony.SMS_RECEIVED" />
 *      </intent-filter>
 *  </receiver>
 */

/*
package com.nerdyfactory.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import com.facebook.react.bridge.WritableNativeMap;

public class SmsListener extends BroadcastReceiver {
    private static final String TAG = "CallListener";

    private void receiveMessage(SmsMessage message) {
        Log.d(TAG, "Sms Received: "+message.getOriginatingAddress()+":"+message.getMessageBody());

        String text = message.getOriginatingAddress()+" "+message.getMessageBody();
        WritableNativeMap params = new WritableNativeMap();
        params.putString("app", "sms");
        params.putString("text", text);
        NotificationModule.sendEvent("notificationReceived", params);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            for (SmsMessage message : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                receiveMessage(message);
            }
            return;
        }

        try {
            final Bundle bundle = intent.getExtras();
            if (bundle == null || ! bundle.containsKey("pdus")) {
                return;
            }
            final Object[] pdus = (Object[]) bundle.get("pdus");
            for (Object pdu : pdus) {
                receiveMessage(SmsMessage.createFromPdu((byte[]) pdu));
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }
}
*/