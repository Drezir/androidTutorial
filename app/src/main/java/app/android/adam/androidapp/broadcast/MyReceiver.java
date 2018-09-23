package app.android.adam.androidapp.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase("MyAction")) {
            Bundle bundle = intent.getExtras();
            String message = bundle.getString("message");
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } else if (intent.getAction().equalsIgnoreCase("android.provider.Telephony.SMS_RECEIVED")) {
            // sms received
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                SmsMessage[] messages = new SmsMessage[pdusObj.length];
                for (int i = 0; i < messages.length; ++i) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        String format = bundle.getString("format");
                        messages[i] = SmsMessage.createFromPdu((byte[])pdusObj[i], format);
                    } else {
                        messages[i] = SmsMessage.createFromPdu((byte[])pdusObj[i]);
                    }
                    String senderNum = messages[i].getOriginatingAddress();
                    String message = messages[i].getMessageBody();

                    Toast.makeText(context, senderNum + ":" + message, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
