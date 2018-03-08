package org.thoughtcrime.securesms.database;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.annimon.stream.Stream;

import org.thoughtcrime.securesms.PermissionType;
import org.thoughtcrime.securesms.recipients.Recipient;
import org.thoughtcrime.securesms.util.BitmapUtil;
import org.thoughtcrime.securesms.util.GroupUtil;
import org.thoughtcrime.securesms.util.Util;
import org.whispersystems.libsignal.util.guava.Optional;
import org.whispersystems.signalservice.api.messages.SignalServiceAttachmentPointer;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class GroupDatabase extends Database {

  @SuppressWarnings("unused")
  private static final String TAG = GroupDatabase.class.getSimpleName();

          static final String TABLE_NAME          = "groups";
  private static final String ID                  = "_id";
          static final String GROUP_ID            = "group_id";
  private static final String TITLE               = "title"; // group name
  private static final String MODERATOR           = "moderator";
  private static final String MEMBERS             = "members";
  private static final String AVATAR              = "avatar";
  private static final String AVATAR_ID           = "avatar_id";
  private static final String AVATAR_KEY          = "avatar_key";
  private static final String AVATAR_CONTENT_TYPE = "avatar_content_type";
  private static final String AVATAR_RELAY        = "avatar_relay";
  private static final String AVATAR_DIGEST       = "avatar_digest";
  private static final String TIMESTAMP           = "timestamp";
  private static final String ACTIVE              = "active";
  private static final String MMS                 = "mms";


  public static final String CREATE_TABLE =
      "CREATE TABLE " + TABLE_NAME +
          " (" + ID + " INTEGER PRIMARY KEY, " +
          GROUP_ID + " TEXT, " +
          TITLE + " TEXT, " +
          MEMBERS + " TEXT, " +
          MODERATOR + " TEXT DEFAULT NULL, " +
          AVATAR + " BLOB, " +
          AVATAR_ID + " INTEGER, " +
          AVATAR_KEY + " BLOB, " +
          AVATAR_CONTENT_TYPE + " TEXT, " +
          AVATAR_RELAY + " TEXT, " +
          TIMESTAMP + " INTEGER, " +
          ACTIVE + " INTEGER DEFAULT 1, " +
          AVATAR_DIGEST + " BLOB, " +
          MMS + " INTEGER DEFAULT 0);";

  static final String[] CREATE_INDEXS = {
      "CREATE UNIQUE INDEX IF NOT EXISTS group_id_index ON " + TABLE_NAME + " (" + GROUP_ID + ");",
  };

  private static final String[] GROUP_PROJECTION = {
      GROUP_ID, TITLE, MEMBERS, MODERATOR, AVATAR, AVATAR_ID, AVATAR_KEY, AVATAR_CONTENT_TYPE, AVATAR_RELAY, AVATAR_DIGEST,
      TIMESTAMP, ACTIVE, MMS
  };

  static final List<String> TYPED_GROUP_PROJECTION = Stream.of(GROUP_PROJECTION).map(columnName -> TABLE_NAME + "." + columnName).toList();

  public GroupDatabase(Context context, SQLiteOpenHelper databaseHelper) {
    super(context, databaseHelper);
  }

  public Optional<GroupRecord> getGroup(String groupId) {
    try (Cursor cursor = databaseHelper.getReadableDatabase().query(TABLE_NAME, null, GROUP_ID + " = ?",
                                                                    new String[] {groupId},
                                                                    null, null, null))
    {
      if (cursor != null && cursor.moveToNext()) {
        return getGroup(cursor);
      }

      return Optional.absent();
    }
  }

  Optional<GroupRecord> getGroup(Cursor cursor) {
    Reader reader = new Reader(cursor);
    return Optional.fromNullable(reader.getCurrent());
  }

  public boolean isUnknownGroup(String groupId) {
    return !getGroup(groupId).isPresent();
  }

  public Reader getGroupsFilteredByTitle(String constraint) {
    @SuppressLint("Recycle")
    Cursor cursor = databaseHelper.getReadableDatabase().query(TABLE_NAME, null, TITLE + " LIKE ?",
                                                                                        new String[]{"%" + constraint + "%"},
                                                                                        null, null, null);

    return new Reader(cursor);
  }

  public String getOrCreateGroupForMembers(List<Address> members, boolean mms) {
    Collections.sort(members);

    Cursor cursor = databaseHelper.getReadableDatabase().query(TABLE_NAME, new String[] {GROUP_ID},
                                                               MEMBERS + " = ? AND " + MMS + " = ?",
                                                               new String[] {Address.toSerializedList(members, ','), mms ? "1" : "0"},
                                                               null, null, null);
    try {
      if (cursor != null && cursor.moveToNext()) {
        return cursor.getString(cursor.getColumnIndexOrThrow(GROUP_ID));
      } else {
        String groupId = GroupUtil.getEncodedId(allocateGroupId(), mms);
        create(groupId, null, members, null, null);
        return groupId;
      }
    } finally {
      if (cursor != null) cursor.close();
    }
  }

  public Reader getGroups() {
    @SuppressLint("Recycle")
    Cursor cursor = databaseHelper.getReadableDatabase().query(TABLE_NAME, null, null, null, null, null, null);
    return new Reader(cursor);
  }

  public @NonNull List<Recipient> getGroupMembers(String groupId, boolean includeSelf) {
    List<Address>   members     = getCurrentMembers(groupId);
    List<Recipient> recipients  = new LinkedList<>();

    for (Address member : members) {
      if (!includeSelf && Util.isOwnNumber(context, member))
        continue;

      recipients.add(Recipient.from(context, member, false));
    }

    return recipients;
  }

  public void create(@NonNull String groupId, @Nullable String title, @NonNull List<Address> members,
                     @Nullable SignalServiceAttachmentPointer avatar, @Nullable String relay)
  {
    Collections.sort(members);

    ContentValues contentValues = new ContentValues();
    contentValues.put(GROUP_ID, groupId);
    contentValues.put(TITLE, title);
    contentValues.put(MEMBERS, Address.toSerializedList(members, ','));
    if (avatar != null) {
      contentValues.put(AVATAR_ID, avatar.getId());
      contentValues.put(AVATAR_KEY, avatar.getKey());
      contentValues.put(AVATAR_CONTENT_TYPE, avatar.getContentType());
      contentValues.put(AVATAR_DIGEST, avatar.getDigest().orNull());
    }

    contentValues.put(AVATAR_RELAY, relay);
    contentValues.put(TIMESTAMP, System.currentTimeMillis());
    contentValues.put(ACTIVE, 1);
    contentValues.put(MMS, GroupUtil.isMmsGroup(groupId));

    databaseHelper.getWritableDatabase().insert(TABLE_NAME, null, contentValues);

    Recipient.applyCached(Address.fromSerialized(groupId), recipient -> {
      recipient.setName(title);
      recipient.setGroupAvatarId(avatar != null ? avatar.getId() : null);
      recipient.setParticipants(Stream.of(members).map(memberAddress -> Recipient.from(this.context, memberAddress, true)).toList());
    });

    notifyConversationListListeners();
  }

  /**
   * @param groupId for searching moderator
   * @return moderator of this group
   */
  private String getGroupModeratorByGroupId(String groupId) {
    SQLiteDatabase db = databaseHelper.getReadableDatabase();
    Cursor cursor = null;
    String sql = "SELECT " + MODERATOR +
            " FROM " + TABLE_NAME +
            " WHERE " + GROUP_ID + " = ?";

    try {
      cursor = db.rawQuery(sql, new String[]{groupId});
      if (cursor != null && cursor.moveToFirst()) {
        return cursor.getString(cursor.getColumnIndex(MODERATOR));
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
   * Add address of the person who creates
   * the group in the moderator column.
   * Version 2: using groupId, this is when
   * you don't have access to group id
   */
  public void updateModeratorColumnByGroupName(String moderator, String groupName) {
    ContentValues contentValues = new ContentValues();
    contentValues.put(MODERATOR, moderator);
    databaseHelper.getWritableDatabase().update(
            TABLE_NAME,
            contentValues,
            TITLE + " = ?",
            new String[]{groupName}
    );
  }

  /**
   * Add address of the person who creates
   * the group in the moderator column.
   */
  public void updateModeratorColumnByGroupId(String moderator, String groupId) {
    ContentValues contentValues = new ContentValues();
    contentValues.put(MODERATOR, moderator);
    databaseHelper.getWritableDatabase().update(
            TABLE_NAME,
            contentValues,
            GROUP_ID + " = ?",
            new String[]{groupId}
    );
  }

  /**
   * Check if local number is a moderator.
   * given a group id
   */
  public boolean isModerator(String moderator, String groupId) {
    if (getGroupModeratorByGroupId(groupId) != null) {
      String realModerator = getGroupModeratorByGroupId(groupId);
      assert realModerator != null;
      return realModerator.equals(moderator);
    }
    return false;
  }

  /**
   * @param recipients members to parse address from
   * @return a list of recipient address
   */
  public List<Address> getRecipientsAddress(List<Recipient> recipients) {
    List<Address> addressList = new LinkedList<>();
    for (Recipient recipient : recipients) {
      addressList.add(recipient.getAddress());
    }
    return addressList;
  }

  /**
   * @param groupId to search from
   * @return group members' addresses
   */
  public String getMembersByGroupId(String groupId) {
    SQLiteDatabase db = databaseHelper.getReadableDatabase();
    Cursor cursor = null;
    String sql = "SELECT " + MEMBERS +
            " FROM " + TABLE_NAME +
            " WHERE " + GROUP_ID + " = ?";

    try {
      cursor = db.rawQuery(sql, new String[]{groupId});
      if (cursor != null && cursor.moveToFirst()) {
        return cursor.getString(cursor.getColumnIndex(MEMBERS)).trim();
      } else {
        return null;
      }
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
  }

  public void update(String groupId, String title, SignalServiceAttachmentPointer avatar) {
    ContentValues contentValues = new ContentValues();
    if (title != null) contentValues.put(TITLE, title);

    if (avatar != null) {
      contentValues.put(AVATAR_ID, avatar.getId());
      contentValues.put(AVATAR_CONTENT_TYPE, avatar.getContentType());
      contentValues.put(AVATAR_KEY, avatar.getKey());
      contentValues.put(AVATAR_DIGEST, avatar.getDigest().orNull());
    }

    databaseHelper.getWritableDatabase().update(TABLE_NAME, contentValues,
                                                GROUP_ID + " = ?",
                                                new String[] {groupId});

    Recipient.applyCached(Address.fromSerialized(groupId), recipient -> {
      recipient.setName(title);
      recipient.setGroupAvatarId(avatar != null ? avatar.getId() : null);
    });

    notifyConversationListListeners();
  }

  public void updateTitle(String groupId, String title) {
    ContentValues contentValues = new ContentValues();
    contentValues.put(TITLE, title);
    databaseHelper.getWritableDatabase().update(TABLE_NAME, contentValues, GROUP_ID +  " = ?",
                                                new String[] {groupId});

    Recipient recipient = Recipient.from(context, Address.fromSerialized(groupId), false);
    recipient.setName(title);
  }

  public void updateAvatar(String groupId, Bitmap avatar) {
    updateAvatar(groupId, BitmapUtil.toByteArray(avatar));
  }

  public void updateAvatar(String groupId, byte[] avatar) {
    long avatarId;

    if (avatar != null) avatarId = Math.abs(new SecureRandom().nextLong());
    else                avatarId = 0;


    ContentValues contentValues = new ContentValues(2);
    contentValues.put(AVATAR, avatar);
    contentValues.put(AVATAR_ID, avatarId);

    databaseHelper.getWritableDatabase().update(TABLE_NAME, contentValues, GROUP_ID +  " = ?",
                                                new String[] {groupId});

    Recipient.applyCached(Address.fromSerialized(groupId), recipient -> recipient.setGroupAvatarId(avatarId == 0 ? null : avatarId));
  }

  public void updateMembers(String groupId, List<Address> members) {
    Collections.sort(members);

    ContentValues contents = new ContentValues();
    contents.put(MEMBERS, Address.toSerializedList(members, ','));
    contents.put(ACTIVE, 1);

    databaseHelper.getWritableDatabase().update(TABLE_NAME, contents, GROUP_ID + " = ?",
                                                new String[] {groupId});
  }

  public void remove(String groupId, Address source) {
    List<Address> currentMembers = getCurrentMembers(groupId);
    currentMembers.remove(source);

    ContentValues contents = new ContentValues();
    contents.put(MEMBERS, Address.toSerializedList(currentMembers, ','));

    databaseHelper.getWritableDatabase().update(TABLE_NAME, contents, GROUP_ID + " = ?",
                                                new String[] {groupId});
  }

  /**
   * Get current members of a group given a group id.
   * @param groupId
   * @return
   */
  private List<Address> getCurrentMembers(String groupId) {
    Cursor cursor = null;

    try {
      cursor = databaseHelper.getReadableDatabase().query(TABLE_NAME, new String[] {MEMBERS},
                                                          GROUP_ID + " = ?",
                                                          new String[] {groupId},
                                                          null, null, null);

      if (cursor != null && cursor.moveToFirst()) {
        String serializedMembers = cursor.getString(cursor.getColumnIndexOrThrow(MEMBERS));
        return Address.fromSerializedList(serializedMembers, ',');
      }

      return new LinkedList<>();
    } finally {
      if (cursor != null)
        cursor.close();
    }
  }

  public boolean isActive(String groupId) {
    Optional<GroupRecord> record = getGroup(groupId);
    return record.isPresent() && record.get().isActive();
  }

  public void setActive(String groupId, boolean active) {
    SQLiteDatabase database = databaseHelper.getWritableDatabase();
    ContentValues  values   = new ContentValues();
    values.put(ACTIVE, active ? 1 : 0);
    database.update(TABLE_NAME, values, GROUP_ID + " = ?", new String[] {groupId});
  }


  public byte[] allocateGroupId() {
    try {
      byte[] groupId = new byte[16];
      SecureRandom.getInstance("SHA1PRNG").nextBytes(groupId);
      return groupId;
    } catch (NoSuchAlgorithmException e) {
      throw new AssertionError(e);
    }
  }

  public static class Reader {

    private final Cursor cursor;

    public Reader(Cursor cursor) {
      this.cursor = cursor;
    }

    public @Nullable GroupRecord getNext() {
      if (cursor == null || !cursor.moveToNext()) {
        return null;
      }

      return getCurrent();
    }

    public @Nullable GroupRecord getCurrent() {
      if (cursor == null || cursor.getString(cursor.getColumnIndexOrThrow(GROUP_ID)) == null) {
        return null;
      }

      return new GroupRecord(cursor.getString(cursor.getColumnIndexOrThrow(GROUP_ID)),
                             cursor.getString(cursor.getColumnIndexOrThrow(TITLE)),
                             cursor.getString(cursor.getColumnIndexOrThrow(MEMBERS)),
                             cursor.getString(cursor.getColumnIndexOrThrow(MODERATOR)),
                             cursor.getBlob(cursor.getColumnIndexOrThrow(AVATAR)),
                             cursor.getLong(cursor.getColumnIndexOrThrow(AVATAR_ID)),
                             cursor.getBlob(cursor.getColumnIndexOrThrow(AVATAR_KEY)),
                             cursor.getString(cursor.getColumnIndexOrThrow(AVATAR_CONTENT_TYPE)),
                             cursor.getString(cursor.getColumnIndexOrThrow(AVATAR_RELAY)),
                             cursor.getInt(cursor.getColumnIndexOrThrow(ACTIVE)) == 1,
                             cursor.getBlob(cursor.getColumnIndexOrThrow(AVATAR_DIGEST)),
                             cursor.getInt(cursor.getColumnIndexOrThrow(MMS)) == 1);
    }

    public void close() {
      if (this.cursor != null)
        this.cursor.close();
    }
  }

  public static class GroupRecord {

    private final String        id;
    private final String        title;
    private final List<Address> members;
    private final String        moderator;
    private final byte[]        avatar;
    private final long          avatarId;
    private final byte[]        avatarKey;
    private final byte[]        avatarDigest;
    private final String        avatarContentType;
    private final String        relay;
    private final boolean       active;
    private final boolean       mms;

    public GroupRecord(String id, String title, String members, String moderator, byte[] avatar,
                       long avatarId, byte[] avatarKey, String avatarContentType,
                       String relay, boolean active, byte[] avatarDigest, boolean mms)
    {
      this.id                = id;
      this.title             = title;
      this.moderator         = moderator;
      this.avatar            = avatar;
      this.avatarId          = avatarId;
      this.avatarKey         = avatarKey;
      this.avatarDigest      = avatarDigest;
      this.avatarContentType = avatarContentType;
      this.relay             = relay;
      this.active            = active;
      this.mms               = mms;

      if (!TextUtils.isEmpty(members))
        this.members = Address.fromSerializedList(members, ',');
      else
        this.members = new LinkedList<>();
    }

    public byte[] getId() {
      try {
        return GroupUtil.getDecodedId(id);
      } catch (IOException ioe) {
        throw new AssertionError(ioe);
      }
    }

    public String getEncodedId() {
      return id;
    }

    public String getTitle() {
      return title;
    }

    public List<Address> getMembers() {
      return members;
    }

    public String getModerator() {
      return moderator;
    }

    public byte[] getAvatar() {
      return avatar;
    }

    public long getAvatarId() {
      return avatarId;
    }

    public byte[] getAvatarKey() {
      return avatarKey;
    }

    public byte[] getAvatarDigest() {
      return avatarDigest;
    }

    public String getAvatarContentType() {
      return avatarContentType;
    }

    public String getRelay() {
      return relay;
    }

    public boolean isActive() {
      return active;
    }

    public boolean isMms() {
      return mms;
    }
  }
}
