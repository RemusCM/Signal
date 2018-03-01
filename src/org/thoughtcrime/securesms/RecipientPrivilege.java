package org.thoughtcrime.securesms;

import android.content.Context;

import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.GroupDatabase;
import org.thoughtcrime.securesms.database.PermissionDatabase;
import org.thoughtcrime.securesms.recipients.Recipient;
import org.thoughtcrime.securesms.util.TextSecurePreferences;

import javax.annotation.Nullable;

public class RecipientPrivilege implements Privilege {

  private static final String TAG = RecipientPrivilege.class.getSimpleName();
  private Recipient recipient;
  private Context context;

  RecipientPrivilege(Recipient recipient, Context context) {
    this.recipient = recipient;
    this.context = context;
  }

  @Override
  public boolean canEditGroup() {
    if (recipient.isGroupRecipient()) {
    /*
     * Success Scenario 1: you are the moderator
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

      /*
       * Success Scenario 2: you have the permission
       * TODO check if the current user (localNumber)
       * has the permission 64 (edit group in column privilege)
       * in permission table
       */
      PermissionDatabase permissionDatabase = DatabaseFactory.getPermissionDatabase(context);
      // if () {}

    }

    return false;
  }

  @Override
  public boolean canClearGroupConversation() {
    // TODO use PermissionDatabase hasClearGroupConversationPermission to verify
    return false;
  }


}