package org.thoughtcrime.securesms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

import static org.webrtc.ContextUtils.getApplicationContext;

public class CustomAlarmReceiver extends BroadcastReceiver {

  private static final String TAG = CustomAlarmReceiver.class.getSimpleName();

  public static final String MESSAGE_EXTRA = "sms_message";
  public static final String PHONE_EXTRA   = "phone_number";


  @Override
  public void onReceive(Context context, Intent intent) {
    try {
      sendSMS(intent);
    } catch (Exception e) {
      Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
      e.printStackTrace();
    }
  }

  private void sendSMS(Intent intent) {
    if (intent.getExtras() != null) {
      String smsMessage = intent.getStringExtra(MESSAGE_EXTRA);
      String phoneNum   = intent.getStringExtra(PHONE_EXTRA);
      try {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNum, null, smsMessage, null, null);
        Toast.makeText(getApplicationContext(), "SMS Sent!", Toast.LENGTH_LONG).show();
      } catch (Exception e) {
        Toast.makeText(getApplicationContext(),
                "SMS failed, please try again later!",
                Toast.LENGTH_LONG).show();
        e.printStackTrace();
      }
    }
  }
}
