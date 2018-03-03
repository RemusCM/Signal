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

      // Getting the group database
      GroupDatabase groupDatabase = DatabaseFactory.getGroupDatabase(context);
      // Getting the local number (the phone number of the main user
      String localNumber = TextSecurePreferences.getLocalNumber(context);

      // Extracting the groupId from the local number
      String groupId = recipient.getAddress().toGroupString();
      // if the current user is a moderator then return true and clear the groupChat
      if (groupDatabase.isModerator(localNumber, groupId)) {
        return true;
      }

      /*
       * Success Scenario 2: you have the permission
       * has the permission 64 (edit group in column privilege)
       * in permission table
       */
      PermissionDatabase permissionDatabase = DatabaseFactory.getPermissionDatabase(context);

      // check if the user has edit privileges in that group
      if (permissionDatabase.hasEditGroupPermission(localNumber, groupId)) {
        return true;
      }

    }

    // Then user must not have had editing privileges
    return false;
  }

  @Override
  public boolean canClearGroupConversation() {
    /*
     * Success Scenario 1: you are the moderator
     * has the permission 32 (clear group chat)
     * in permission's privileges table
     */

    // Getting the group database
    GroupDatabase groupDatabase = DatabaseFactory.getGroupDatabase(context);
    // Getting the local number (the phone number of the main user
    String localNumber = TextSecurePreferences.getLocalNumber(context);

    // Extracting the groupId from the local number
    String groupId = recipient.getAddress().toGroupString();

    // check if the current user is a moderator
    if (groupDatabase.isModerator(localNumber, groupId)) {
      return true;
    }

	  /*
     * Success Scenario 2: you have the permission
     * in permission's privileges table
	   * use PermissionDatabase hasClearGroupConversationPermission to verify
     */

	  PermissionDatabase permissionDatabase = DatabaseFactory.getPermissionDatabase(context);

	  // check if the current user has the privileges to clear conversation in that group
    if (permissionDatabase.hasClearGroupConversationPermission(localNumber, groupId)) {
      return true;
    }

    //Then the user must not have had permission and we return false
    return false;
  }
}