package org.thoughtcrime.securesms;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import org.thoughtcrime.securesms.database.Address;
import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.ThreadDatabase;
import org.thoughtcrime.securesms.recipients.Recipient;

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
    if (recipient != null) {
      Address recipientId = recipient.getAddress();

      ThreadDatabase threadDatabase = DatabaseFactory.getThreadDatabase(context);
      int messageCount = threadDatabase.getMessageCountByRecipientId(recipientId);

      if (messageCount < 1) {
        dialogInterface.dismiss();
        util.displayNothingToDeleteMessage();
      } else {

        long threadId = DatabaseFactory.getThreadDatabase(context)
                .getThreadIdFor(recipient,
                        ThreadDatabase.DistributionTypes.DEFAULT);
        DatabaseFactory.getSmsDatabase(context).deleteMessagesInThreadBeforeDate(threadId, System.currentTimeMillis());
        DatabaseFactory.getMmsDatabase(context).deleteMessagesInThreadBeforeDate(threadId, System.currentTimeMillis());
        DatabaseFactory.getThreadDatabase(context).update(threadId, false);

        dialogInterface.dismiss();
        util.displayAllMessagesDeleted();
      }
    }
  }

  private class ClearConversationUtil {
    private final String TAG = ClearConversationUtil.class.getSimpleName();
    ClearConversationUtil() {}

    private void displayNothingToDeleteMessage() {
      Toast.makeText(
              context,
              R.string.RecipientPreferenceActivity_clear_conversation_nothing_to_delete,
              Toast.LENGTH_LONG
      ).show();
    }

    private void displayAllMessagesDeleted() {
      Toast.makeText(
              context,
              R.string.RecipientPreferenceActivity_clear_conversation_successful_deletion,
              Toast.LENGTH_LONG
      ).show();
    }
  }

}
