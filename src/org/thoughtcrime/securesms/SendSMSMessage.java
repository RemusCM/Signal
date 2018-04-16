package org.thoughtcrime.securesms;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.thoughtcrime.securesms.crypto.MasterSecret;
import org.thoughtcrime.securesms.database.Address;
import org.thoughtcrime.securesms.recipients.Recipient;
import org.thoughtcrime.securesms.service.KeyCachingService;
import org.thoughtcrime.securesms.sms.MessageSender;
import org.thoughtcrime.securesms.sms.OutgoingTextMessage;

import static org.webrtc.ContextUtils.getApplicationContext;

/**
 * This object handles the actual routine
 * for sending a message.
 */
public class SendSMSMessage  {
  private static final String TAG = SendSMSMessage.class.getSimpleName();


  private Context context;
  private String message;
  private long threadId;

  private Recipient recipient;
  private int subscriptionId;
  private long expiresIn;
  private MasterSecret masterSecret;

  private boolean isMessageSent;

  SendSMSMessage(Context context, String message, String number, long threadId) {
    this.context = context;
    this.message = message;
    this.threadId = threadId;

    // these members are initialized internally with the help of this constructor parameters
    this.recipient = Recipient.from(context, Address.fromSerialized(number), true);
    this.subscriptionId = recipient.getDefaultSubscriptionId().or(-1);
    this.expiresIn = recipient.getExpireMessages() * 1000;
    this.masterSecret = KeyCachingService.getMasterSecret(context);
    this.isMessageSent = false;
  }

  // this is the actual routine that sends sms message
  void sendSMSMessage() {

    try {
      Toast.makeText(getApplicationContext(), "Sending sms message...", Toast.LENGTH_LONG).show();

      // using the OutgoingTextMessage to send sms
      OutgoingTextMessage outgoingTextMessage = new OutgoingTextMessage(
              recipient, message,
              expiresIn, subscriptionId
      );

      new AsyncTask<Void, Void, Void>() {

        @Override
        protected Void doInBackground(Void... params) {
          MessageSender.send(context, masterSecret, outgoingTextMessage, threadId, false, null);
          return null;
        }

        @Override
        protected void onPostExecute(Void result) {
          isMessageSent = true;
          String successSending = "SMS message has been sent successfully!";
          Toast.makeText(context, successSending, Toast.LENGTH_LONG).show();
        }

      }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    } catch (Exception e) {
      String failedSending = "There was an error sending your message. Please try again later.";
      Toast.makeText(context, failedSending, Toast.LENGTH_SHORT).show();

      Log.e(TAG, e.getMessage());
      e.printStackTrace();
    }

  }

  public void setMessage(String message) {
    this.message = message;
  }

  public void setThreadId(long threadId) {
    this.threadId = threadId;
  }

  public void setRecipient(Recipient recipient) {
    this.recipient = recipient;
  }

  public void setContext(Context context) {
    this.context = context;
  }

  public String getMessage() {
    return message;
  }

  public long getThreadId() {
    return threadId;
  }

  public boolean isMessageSent() {
    return isMessageSent;
  }

}
