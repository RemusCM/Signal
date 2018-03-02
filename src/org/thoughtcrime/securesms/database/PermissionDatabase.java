package org.thoughtcrime.securesms.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.annimon.stream.Stream;

import org.thoughtcrime.securesms.PermissionType;

import java.util.LinkedList;
import java.util.List;

public class PermissionDatabase extends Database {


  private static final String TAG = GroupDatabase.class.getSimpleName();

          static final String TABLE_NAME          = "permission";
  private static final String ID                  = "_id";
  private static final String GROUP_ID            = "group_id";
  private static final String ADDRESS             = "address";
  private static final String PRIVILEGES          = "privileges";

  public static final String CREATE_TABLE =
          "CREATE TABLE " + TABLE_NAME +
                  " (" + ID + " INTEGER PRIMARY KEY, " +
                  GROUP_ID + " TEXT, " +
                  ADDRESS + " TEXT, " +
                  PRIVILEGES + " TEXT);";

  static final String[] CREATE_INDEXS = {
          "CREATE INDEX IF NOT EXISTS permission_id_index ON " + TABLE_NAME + " (" + GROUP_ID + ");",
  };

  private static final String[] PERMISSION_PROJECTION = {GROUP_ID, ADDRESS, PRIVILEGES};

  static final List<String> TYPED_PERMISSION_PROJECTION = Stream.of(PERMISSION_PROJECTION).map(columnName -> TABLE_NAME + "." + columnName).toList();

  PermissionDatabase(Context context, SQLiteOpenHelper databaseHelper) {
    super(context, databaseHelper);
  }

  /**
   * Get privileges of user.
   * @return string of privileges in the form 64,32,16
   */
  private String getRecipientPrivilegesString(String localNumber, String groupId) {

    SQLiteDatabase db = databaseHelper.getReadableDatabase();
    Cursor cursor = null;
    String sql = "SELECT * FROM permission WHERE address = ? AND group_id = ?";
    String[] sqlArgs  = new String[] {localNumber, groupId};

    try{
      cursor = db.rawQuery(sql, sqlArgs);
      if (cursor != null && cursor.moveToFirst()){
        return cursor.getString(cursor.getColumnIndex(PRIVILEGES));
      } else {
        return null;
      }
    }finally{
      if(cursor != null){
        cursor.close();
      }
    }
  }

  /**
   * This is a helper method. Don't remove.
   * Parses String privileges (split comma separated string) into a list
   * @param privileges from getRecipientPrivilegesString
   * @return array of privileges
   */
  private List<String> splitPrivilegesIntoList(String privileges) {
    List<String> privilegeList = new LinkedList<>();
    String[] privilegeTokens = privileges.split("\\,");

    for (int i = 0; i < privilegeTokens.length; i++) {
      privilegeList.add(privilegeTokens[i]);
    }

    return privilegeList;
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

  /**
   * @param localNumber the current user phone number
   * @param groupId     Typical groupId: __textsecure_group__!a266a5868e682c63b2fd41e2484e007a
   * @return true if current user has clear group chat permission
   */
  public boolean hasClearGroupConversationPermission(String localNumber, String groupId) {
    String privileges = getRecipientPrivilegesString(localNumber, groupId);
    List<String> list = splitPrivilegesIntoList(privileges);
    String clearGroupConversationCode = String.valueOf(PermissionType.CLEAR_GROUP_CONVERSATION);
    return list != null && list.contains(clearGroupConversationCode);
  }

  /**
   * Insert records into the permission table.
   *
   * @param groupId this acts like a primary key
   * @param members group members
   */
  public void create(String groupId, String moderator, List<Address> members) {
    SQLiteDatabase db = databaseHelper.getWritableDatabase();

    PermissionType editGroup = PermissionType.EDIT_GROUP;
    PermissionType clearChat = PermissionType.CLEAR_GROUP_CONVERSATION;
    String[] str = {editGroup.getPermissionTypeCode(), clearChat.getPermissionTypeCode()};

    for (Address member : members) {
      ContentValues values = new ContentValues();
      values.put(GROUP_ID, groupId);
      values.put(ADDRESS, member.serialize());
      if (moderator.equals(member.serialize())) {
        values.put(PRIVILEGES, joinStringPrivileges(str));
      } else {
        values.put(PRIVILEGES, "");
      }

      db.insert(TABLE_NAME, null, values);
    }

  }

  /**
   * Concatenate privilege with comma
   * into one single string variable.
   *
   * @param str strings privileges
   * @return comma separated string
   */
  private String joinStringPrivileges(String[] str) {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < str.length; i++) {
      if (i != str.length - 1) {
        stringBuilder.append(str[i].concat(","));
      } else {
        stringBuilder.append(str[i]);
      }
    }
    return stringBuilder.toString();
  }
}
