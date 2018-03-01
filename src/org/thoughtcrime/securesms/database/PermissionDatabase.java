package org.thoughtcrime.securesms.database;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import org.thoughtcrime.securesms.PermissionType;

import java.util.List;

public class PermissionDatabase extends Database {


  PermissionDatabase(Context context, SQLiteOpenHelper databaseHelper) {
    super(context, databaseHelper);
  }

  // TODO create table

  /**
   * String localNumber = TextSecurePreferences.getLocalNumber(context);
   * recipient.getAddress().toGroupString()
   *
   * @return string of privileges in the form 64,32,16
   */
  private String getRecipientPrivilegesString(String localNumber, String groupId) {
    // TODO use select query here SELECT privileges FROM permission WHERE address = localNumber
    return null;
  }

  /**
   * This is a helper method. Don't remove.
   *
   * @param privileges from getRecipientPrivilegesString
   * @return array of privileges
   */
  private List<String> splitPrivilegesIntoList(String privileges) {
    // TODO parse string privileges (split comma separated string)
    return null;
  }

  /**
   * @param localNumber the current user phone number
   * @param groupId     Typical groupId: __textsecure_group__!a266a5868e682c63b2fd41e2484e007a
   * @return true if current user has edit group permission
   */
  public boolean hasEditGroupPermission(String localNumber, String groupId) {
    String privileges = getRecipientPrivilegesString(localNumber, groupId);
    List<String> list = splitPrivilegesIntoList(privileges);
    String editGroupCode = String.valueOf(PermissionType.EDIT_GROUP);
    return list != null && list.contains(editGroupCode);
  }

  public boolean hasClearGroupConversationPermission(String localNumber, String groupId) {
    // TODO see hasEditGroupPermission for reference
    return false;
  }


}
