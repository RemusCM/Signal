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
  private String currentUserPhoneNumber;
  private String groupId;

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
    boolean condition = false;
    if (recipient.isGroupRecipient()) {
      if (groupDatabase.isModerator(currentUserPhoneNumber, groupId)) {
        condition = true;
      }
      if (permissionDatabase.isGroupExistByGroupId(groupId)) {
        if (permissionDatabase.hasEditGroupPermission(currentUserPhoneNumber, groupId)) {
          condition = true;
        }
      }
    }
    Log.i(TAG, "canEditGroup() : boolean -> " + condition);
    Log.i(TAG, "canEditGroup() : currentUserPhoneNumber -> " + currentUserPhoneNumber);
    Log.i(TAG, "canEditGroup() : groupId -> " + groupId);
    return condition;
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
    boolean condition = false;
    if (recipient.isGroupRecipient()) {
      if (groupDatabase.isModerator(currentUserPhoneNumber, groupId)) {
        condition = true;
      }
      if (permissionDatabase.isGroupExistByGroupId(groupId)) {
        if (permissionDatabase.hasClearGroupChatPermission(currentUserPhoneNumber, groupId)) {
          condition = true;
        }
      }
    }
    Log.i(TAG, "canClearGroupConversation() : boolean -> " + condition);
    Log.i(TAG, "canClearGroupConversation() : currentUserPhoneNumber -> " + currentUserPhoneNumber);
    Log.i(TAG, "canClearGroupConversation() : groupId -> " + groupId);
    return condition;
  }
}
