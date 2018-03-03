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
     */
      GroupDatabase groupDatabase = DatabaseFactory.getGroupDatabase(context);
      String localNumber = TextSecurePreferences.getLocalNumber(context);
      String groupId = recipient.getAddress().toGroupString();
      if (groupDatabase.isModerator(localNumber, groupId)) {
        return true;
      }

      /*
       * Success Scenario 2: you have the permission
       * has the permission 64 (edit group in column privilege)
       * in permission table
       */
      PermissionDatabase permissionDatabase = DatabaseFactory.getPermissionDatabase(context);
      if (permissionDatabase.hasEditGroupPermission(localNumber, groupId)) {
        return true;
      }

    }

    return false;
  }

  @Override
  public boolean canClearGroupConversation() {
    // TODO to be implemented by @ian-tab
    // see canEditGroup above
    /*
     * Success Scenario 1: you are the moderator
     * has the permission 32 (clear group chat)
     * in permission's privileges table
     */

    // Getting the group database
    GroupDatabase groupDatabase = DatabaseFactory.getGroupDatabase(context);
    // Getting the local number
    String localNumber = TextSecurePreferences.getLocalNumber(context);

    // Extracting the groupId from the local number
    String groupId = recipient.getAddress().toGroupString();

    // if the current user is a moderator then return true and clear the groupChat
    if (groupDatabase.isModerator(localNumber, groupId)) {
      return true;
    }

	  /*
     * Success Scenario 2: you have the permission
     * in permission's privileges table
	   * use PermissionDatabase hasClearGroupConversationPermission to verify
     */

	  PermissionDatabase permissionDatabase = DatabaseFactory.getPermissionDatabase(context);

    if (permissionDatabase.hasClearGroupConversationPermission(localNumber, groupId)) {
      return true;
    }

    return false;
  }
}