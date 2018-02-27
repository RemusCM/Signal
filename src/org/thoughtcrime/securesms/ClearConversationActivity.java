package org.thoughtcrime.securesms;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.thoughtcrime.securesms.database.Address;
import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.ThreadDatabase;
import org.thoughtcrime.securesms.recipients.Recipient;

import java.util.ArrayList;

@SuppressLint({"Registered", "ValidFragment"})
public class ClearConversationActivity implements DialogInterface.OnClickListener {

  private static final String TAG = ClearConversationActivity.class.getSimpleName();

  private final Recipient recipient;
  private final Context context;


  ClearConversationActivity(Recipient recipient, Context context) {
    this.recipient = recipient;
    this.context = context;
  }

  @SuppressLint("StaticFieldLeak")
  @Override
  public void onClick(DialogInterface dialogInterface, int which) {

    new AsyncTask<Void, Void, Void>() {
      @Override
      protected Void doInBackground(Void... params) {
        Address recipientId = recipient.getAddress();

        ThreadDatabase threadDatabase = DatabaseFactory.getThreadDatabase(context);
        int oldMessageCount = threadDatabase.getMessageCountByRecipientId(recipientId);
        Log.w(TAG, "Message count = " + String.valueOf(oldMessageCount));

        if (oldMessageCount > 0) {
          ArrayList<Integer> messageIds;
          messageIds = DatabaseFactory.getSmsDatabase(context).getMessageIdsByRecipientId(recipientId);
          for (int i = 0; i < messageIds.size(); i++) {
            long messageId = messageIds.get(i);
            Log.w(TAG, "Deleting: " + String.valueOf(messageId));
            DatabaseFactory.getSmsDatabase(context).deleteMessage(messageId);
          }
          if (messageIds.isEmpty() || messageIds.size() == 0) {
            dialogInterface.dismiss();
            Toast.makeText(context,
                    R.string.RecipientPreferenceActivity_clear_conversation_successful_deletion,
                    Toast.LENGTH_LONG).show();
          }
        } else {
          dialogInterface.dismiss();
          Toast.makeText(context,
                  R.string.RecipientPreferenceActivity_clear_conversation_nothing_to_delete,
                  Toast.LENGTH_LONG).show();
        }

        return null;
      }

    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
  }
}