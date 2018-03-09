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

import java.util.ArrayList;
import java.util.LinkedList;

@SuppressLint({"Registered", "ValidFragment"})
public class ClearConversationActivity implements DialogInterface.OnClickListener {
    LinkedList<Recipient> members;
    long threadIdForMember;
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



          for(Recipient recipient : recipient.getParticipants()){
              members.add(recipient);
          }
          for(Recipient recipient : members){
              threadIdForMember = DatabaseFactory.getThreadDatabase(context).getThreadIdFor(recipient);
              //DatabaseFactory.getThreadDatabase(context).deleteConversation(threadIdForMember);
              //DatabaseFactory.getThreadDatabase(context).update(threadIdForMember, false);
              DatabaseFactory.getSmsDatabase(context).deleteMessagesInThreadBeforeDate(threadIdForMember,System.currentTimeMillis());
              DatabaseFactory.getMmsDatabase(context).deleteMessagesInThreadBeforeDate(threadIdForMember,System.currentTimeMillis());

          }
        //This part deletes the conversation locally.
//        long threadId = DatabaseFactory.getThreadDatabase(context)
//                .getThreadIdFor(recipient,
//                        ThreadDatabase.DistributionTypes.DEFAULT);
//        DatabaseFactory.getThreadDatabase(context).deleteConversation(threadId);
//        DatabaseFactory.getThreadDatabase(context).update(threadId, false);

        dialogInterface.dismiss();
        util.displayAllMessagesDeleted();
      }
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
