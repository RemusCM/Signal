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
  private String localNumber;
  GroupDatabase groupDatabase;
  PermissionDatabase permissionDatabase;

  RecipientPrivilege(Recipient recipient, Context context) {
    this.recipient = recipient;
    this.context = context;

    // Getting the group database
    groupDatabase = DatabaseFactory.getGroupDatabase(context);

    // Getting the permission database
    permissionDatabase = DatabaseFactory.getPermissionDatabase(context);

    // Getting the local number (the phone number of the main user
    String localNumber = TextSecurePreferences.getLocalNumber(context);
    this.localNumber = localNumber;
  }

  @Override
  public boolean canEditGroup() {
    if (recipient.isGroupRecipient()) {
    /*
     * Success Scenario 1: you are the moderator
     * Using group database you can determine if you can edit
     * group name.
     */

      // Extracting the groupId from the local number
      String groupId = recipient.getAddress().toGroupString();

      if (groupDatabase.isModerator(localNumber, groupId)) {
        return true;
      }

      /*
       * Success Scenario 2: you have the permission
       * has the permission 64 (edit group in column privilege)
       * in permission table
       */

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

    if (permissionDatabase.hasClearGroupConversationPermission(localNumber, groupId)) {
      return true;
    }

    //Then the user must not have had permission and we return false
    return false;
  }
}