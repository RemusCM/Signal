package org.thoughtcrime.securesms;

import android.content.Context;

import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.GroupDatabase;
import org.thoughtcrime.securesms.recipients.Recipient;
import org.thoughtcrime.securesms.util.TextSecurePreferences;

import javax.annotation.Nullable;

public class RecipientPrivilege implements Privilege {

  private static final String TAG = RecipientPrivilege.class.getSimpleName();
  private Recipient recipient;
  private Context context;

  public RecipientPrivilege(Recipient recipient, Context context) {
    this.recipient = recipient;
    this.context = context;
  }

  @Override
  public boolean canEditGroup() {
    if (recipient.isGroupRecipient()) {
    /*
     * success scenario 1: you are the moderator
     * Using group database you can determine if you can edit
     * group name.
     *
     * Util.isOwnNumber(context, recipient.getAddress());
     */
      GroupDatabase groupDatabase = DatabaseFactory.getGroupDatabase(context);
      String localNumber = TextSecurePreferences.getLocalNumber(context);
      if (groupDatabase.isModerator(localNumber, recipient.getAddress().toGroupString())) {
        return true;
      }
      // success scenario 2: you have the permission
    }

    return false;
  }

  @Override
  public boolean canClearGroupConversation() {
    return false;
  }

}