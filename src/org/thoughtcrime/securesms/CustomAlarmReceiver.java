package org.thoughtcrime.securesms;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import org.thoughtcrime.securesms.crypto.MasterSecret;
import org.thoughtcrime.securesms.database.Address;
import org.thoughtcrime.securesms.recipients.Recipient;
import org.thoughtcrime.securesms.service.KeyCachingService;
import org.thoughtcrime.securesms.sms.MessageSender;
import org.thoughtcrime.securesms.sms.OutgoingTextMessage;

import static org.webrtc.ContextUtils.getApplicationContext;

/**
 * This class handles alarm service.
 * When an alarm is set, the method
 * onReceive is called and tries
 * to run whatever inside it's
 * method body.
 */
public class CustomAlarmReceiver extends BroadcastReceiver {

  private static final String TAG = CustomAlarmReceiver.class.getSimpleName();

  public static final String THREAD_ID_EXTRA = "thread_id";
  public static final String MESSAGE_EXTRA   = "message";
  public static final String NUMBER_EXTRA    = "number";

  // using the data we get from ScheduleMessageObj and the function
  // MessageSender.send -> we are able to send a message
  // when the alarm is reached
  @SuppressLint("StaticFieldLeak")
  @Override
  public void onReceive(Context context, Intent intent) {
    MasterSecret masterSecret    = KeyCachingService.getMasterSecret(context);
    String message               = intent.getStringExtra(MESSAGE_EXTRA);
    String number                = intent.getStringExtra(NUMBER_EXTRA);
    long threadId                = intent.getLongExtra(THREAD_ID_EXTRA, -1);
    Recipient recipient          = Recipient.from(context, Address.fromSerialized(number), true);
    int  subscriptionId          = recipient.getDefaultSubscriptionId().or(-1);
    long expiresIn               = recipient.getExpireMessages() * 1000;

    try {

      Toast.makeText(getApplicationContext(), "Sending sms message...", Toast.LENGTH_LONG).show();

      OutgoingTextMessage outgoingTextMessage = new OutgoingTextMessage(recipient, message, expiresIn, subscriptionId);

      new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... params) {
          MessageSender.send(context, masterSecret, outgoingTextMessage, threadId, false, null);

          return null;
        }

        @Override
        protected void onPostExecute(Void result) {
          Toast.makeText(getApplicationContext(), "SMS message has been sent successfully!", Toast.LENGTH_LONG).show();
        }

      }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    } catch (Exception e) {
      Toast.makeText(context, "There was an error sending your message. Please try again later.",
              Toast.LENGTH_SHORT).show();
      e.printStackTrace();
    }
  }

}
