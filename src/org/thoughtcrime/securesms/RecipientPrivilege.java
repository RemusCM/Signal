package org.thoughtcrime.securesms;

import android.content.Context;
import android.util.Log;

import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.GroupDatabase;
import org.thoughtcrime.securesms.database.PermissionDatabase;
import org.thoughtcrime.securesms.recipients.Recipient;
import org.thoughtcrime.securesms.util.TextSecurePreferences;

public class RecipientPrivilege implements Privilege {

  private static final String TAG = RecipientPrivilege.class.getSimpleName();

  private Recipient recipient;

  /**
   * databases needed for determining if one is permitted
   *   groupDatabase: use to determine who's moderator in a group
   *   permissionDatabase: use to determine user privileges
   */
  private GroupDatabase groupDatabase;
  private PermissionDatabase permissionDatabase;

  /**
   * parameters required for determining if one is permitted
   *   currentUserPhoneNumber: current user's phone number
   *   groupId: unique group id
   */
  private final String currentUserPhoneNumber;
  private final String groupId;

  RecipientPrivilege(Recipient recipient, Context context) {
    this.recipient = recipient;

    this.groupDatabase = DatabaseFactory.getGroupDatabase(context);
    this.permissionDatabase = DatabaseFactory.getPermissionDatabase(context);

    this.currentUserPhoneNumber = TextSecurePreferences.getLocalNumber(context);
    this.groupId = recipient.getAddress().toGroupString();
  }


  /*
   * Success Scenario 1: if you are the moderator
   * and you have the permission to edit a group.
   * With this permission you can: edit group name
   * and add group member(s)
   *
   * Success Scenario 2 (for non-moderator): if you're granted the permission
   * to edit a group
   */
  @Override
  public boolean canEditGroup() {
    if (recipient.isGroupRecipient()) {
      Log.i(TAG, "canEditGroup[currentUserPhoneNumber]: " + currentUserPhoneNumber);
      Log.i(TAG, "canEditGroup[groupId]: " + groupId);
      if (groupDatabase.isModerator(currentUserPhoneNumber, groupId)) {
        return true;
      }
      if (permissionDatabase.hasEditGroupPermission(currentUserPhoneNumber, groupId)) {
        return true;
      }
    }
    return false;
  }

  /*
   * Success Scenario 1: if you are the moderator
   * and you have the permission to clear a group chat
   *
   * Success Scenario 2 (for non-moderator): you are granted the
   * permission to clear a conversation in a group chat
   */
  @Override
  public boolean canClearGroupConversation() {
    if (recipient.isGroupRecipient()) {
      if (groupDatabase.isModerator(currentUserPhoneNumber, groupId)) {
        return true;
      }
      if (permissionDatabase.hasClearGroupConversationPermission(currentUserPhoneNumber, groupId)) {
        return true;
      }
    }
    return false;
  }
}
