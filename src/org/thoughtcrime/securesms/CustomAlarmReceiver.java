package org.thoughtcrime.securesms;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.thoughtcrime.securesms.database.Address;
import org.thoughtcrime.securesms.recipients.Recipient;

/**
 * This class handles alarm service.
 * When the alarm is off, the method
 * onReceive runs whatever inside it's
 * method body.
 */
public class CustomAlarmReceiver extends BroadcastReceiver {

  private static final String TAG = CustomAlarmReceiver.class.getSimpleName();

  public static final String THREAD_ID_EXTRA = "thread_id";
  public static final String MESSAGE_EXTRA   = "message";
  public static final String NUMBER_EXTRA    = "number";

  @Override
  public void onReceive(Context context, Intent intent) {
    String message               = intent.getStringExtra(MESSAGE_EXTRA);
    String number                = intent.getStringExtra(NUMBER_EXTRA);
    long threadId                = intent.getLongExtra(THREAD_ID_EXTRA, -1);

    SendSMSMessage sendSMSMessage = new SendSMSMessage(context, message, number, threadId);

    try {
      sendSMSMessage.sendSMSMessage();
    } catch (NullPointerException npe) {
      Log.e(TAG, npe.getMessage());
      npe.printStackTrace();
    }

  }

}
