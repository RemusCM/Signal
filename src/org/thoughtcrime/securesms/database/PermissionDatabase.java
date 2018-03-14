package org.thoughtcrime.securesms.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.annimon.stream.Stream;

import org.thoughtcrime.securesms.PermissionType;
import org.thoughtcrime.securesms.util.Util;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nullable;

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
  public String getRecipientPrivilegesString(String localNumber, String groupId) {
    SQLiteDatabase db = databaseHelper.getReadableDatabase();
    Cursor cursor = null;
    String sql = "SELECT " + PRIVILEGES +
            " FROM " + TABLE_NAME +
            " WHERE " + ADDRESS + " = ?" +
            " AND " + GROUP_ID + " = ?";

    String[] sqlArgs  = new String[] {localNumber, groupId};

    try {
      cursor = db.rawQuery(sql, sqlArgs);
      if (cursor != null && cursor.moveToFirst()) {
        return cursor.getString(cursor.getColumnIndex(PRIVILEGES));
      } else {
        return null;
      }
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
  }

  /**
   * @param localNumber the current user phone number
   * @param groupId group id of the current group of the user
   * @return true if current user has edit group permission
   */
  public boolean hasEditGroupPermission(String localNumber, String groupId) {
    String privileges = getRecipientPrivilegesString(localNumber, groupId);
    List<String> list = Util.splitStringIntoList(privileges);
    String editGroupCode = PermissionType.EDIT_GROUP.getPermissionTypeCode();
    return list != null && list.contains(editGroupCode);
  }

  /**
   * @param localNumber the current user phone number
   * @param groupId group id of the current group of the user
   * @return true if current user has clear group chat permission
   */
  public boolean hasClearGroupChatPermission(String localNumber, String groupId) {
    String privileges = getRecipientPrivilegesString(localNumber, groupId);
    List<String> list = Util.splitStringIntoList(privileges);
    String clearGroupChatCode = PermissionType.CLEAR_GROUP_CONVERSATION.getPermissionTypeCode();
    return list != null && list.contains(clearGroupChatCode);
  }

  /**
   * Insert records into the permission table.
   * @param groupId this acts like a primary key
   * @param givenPrivileges privileges given to this moderator
   * @param members group members
   */
  public void create(String groupId, String moderator, String[] givenPrivileges, List<Address> members) {
    SQLiteDatabase db = databaseHelper.getWritableDatabase();

    for (Address member : members) {
      ContentValues values = new ContentValues();
      values.put(GROUP_ID, groupId);
      values.put(ADDRESS, member.serialize());
      if (moderator.equals(member.serialize())) {
        values.put(PRIVILEGES, Util.joinStringElements(givenPrivileges));
      } else {
        values.put(PRIVILEGES, "");
      }

      db.insert(TABLE_NAME, null, values);
    }

  }

  /**
   * @param groupId group to search for
   * @return true if this group exists
   */
  public boolean isGroupExistByGroupId(String groupId) {
    String sql = "SELECT COUNT(*) " +
            " FROM " + TABLE_NAME +
            " WHERE " + GROUP_ID + " = ?";
    SQLiteDatabase db = databaseHelper.getReadableDatabase();
    Cursor cursor = null;
    try {
      cursor = db.rawQuery(sql, new String[]{groupId});
      if (cursor != null && cursor.moveToFirst()) {
        return cursor.getInt(0) >= 1;
      } else {
        return false;
      }
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
  }

  /**
   * Permission model (helper class)
   * use for reading a record from
   * permission table.
   */
  public static class PermissionRecord {

    private final String groupId;
    private final String address;
    private final String privileges;

    public PermissionRecord(String groupId, String address, String privileges) {
      this.groupId = groupId;
      this.address = address;
      this.privileges = privileges;
    }

    public String getGroupId() {
      return groupId;
    }

    public String getAddress() {
      return address;
    }

    public String getPrivileges() {
      return privileges;
    }
  }

  /**
   * Reader of permission database (helper class)
   * You can use this reader to read a cursor.
   * See getCurrentMembers of GroupDatabase for
   * usage.
   */
  public static class Reader {

    private final Cursor cursor;

    public Reader(Cursor cursor) {
      this.cursor = cursor;
    }

    public @Nullable
    PermissionRecord getNext() {
      if (cursor == null || !cursor.moveToNext()) {
        return null;
      }
      return getCurrent();
    }

    public @Nullable
    PermissionRecord getCurrent() {
      if (cursor == null || cursor.getString(cursor.getColumnIndexOrThrow(GROUP_ID)) == null) {
        return null;
      }

      return new PermissionRecord(
              cursor.getString(cursor.getColumnIndexOrThrow(GROUP_ID)),
              cursor.getString(cursor.getColumnIndexOrThrow(ADDRESS)),
              cursor.getString(cursor.getColumnIndexOrThrow(PRIVILEGES))
      );
    }

    public void close() {
      if (this.cursor != null) {
        this.cursor.close();
      }
    }

  }

  
}
