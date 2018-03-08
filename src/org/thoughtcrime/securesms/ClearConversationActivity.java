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

  private ClearConversationUtil util = new ClearConversationUtil();

  ClearConversationActivity(Recipient recipient, Context context) {
    this.recipient = recipient;
    this.context = context;
  }

  @SuppressLint("StaticFieldLeak")
  @Override
  public void onClick(DialogInterface dialogInterface, int which) {
    Address recipientId = recipient.getAddress();
    ThreadDatabase threadDatabase = DatabaseFactory.getThreadDatabase(context);
    int messageCount = threadDatabase.getMessageCountByRecipientId(recipientId);

    if (messageCount < 1) {
      dialogInterface.dismiss();
      util.displayNothingToDeleteMessage();
    }

    //This part of the code completely clears group conversations, including the system messages.
    else if(recipient.isGroupRecipient()){

      long      threadId       = DatabaseFactory.getThreadDatabase(context).getThreadIdFor(recipient, ThreadDatabase.DistributionTypes.DEFAULT);
      DatabaseFactory.getSmsDatabase(context).deleteThread(threadId);
      DatabaseFactory.getMmsDatabase(context).deleteThread(threadId);
      dialogInterface.dismiss();
      util.displayAllMessagesDeleted();

    }
    else {
      new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... params) {
          ArrayList<Integer> messageIds;
          messageIds = DatabaseFactory.getSmsDatabase(context).getMessageIdsByRecipientId(recipientId);
          for (int i = 0; i < messageIds.size(); i++) {
            long messageId = messageIds.get(i);
            Log.w(TAG, "Deleting: " + String.valueOf(messageId));
            DatabaseFactory.getSmsDatabase(context).deleteMessage(messageId);
          }
          return null;
        }
      }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
      dialogInterface.dismiss();
      util.displayAllMessagesDeleted();
    }
  }

  private class ClearConversationUtil {
    private final String TAG = ClearConversationUtil.class.getSimpleName();
    ClearConversationUtil() {}

    private void displayNothingToDeleteMessage() {
      Log.w(TAG, "ClearConversationUtil::displayNothingToDeleteMessage()");
      Toast.makeText(
              context,
              R.string.RecipientPreferenceActivity_clear_conversation_nothing_to_delete,
              Toast.LENGTH_SHORT
      ).show();
    }

    private void displayAllMessagesDeleted() {
      Log.w(TAG, "ClearConversationUtil::displayAllMessagesDeleted()");
      Toast.makeText(
              context,
              R.string.RecipientPreferenceActivity_clear_conversation_successful_deletion,
              Toast.LENGTH_LONG
      ).show();
    }
  }

}
