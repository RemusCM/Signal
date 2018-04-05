package org.thoughtcrime.securesms.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PasscodeDatabase extends ThreadDatabase {

  private static final String TAG = PasscodeDatabase.class.getSimpleName();

  PasscodeDatabase(Context context, SQLiteOpenHelper databaseHelper) {
    super(context, databaseHelper);
  }

  public String getPasscodeByThreadId(long threadId) {
    SQLiteDatabase db = databaseHelper.getReadableDatabase();
    Cursor cursor = null;
    String sql = "SELECT " + PASSCODE + " FROM " + TABLE_NAME + " WHERE " + ID + " = ?";
    String[] sqlArgs = new String[] {threadId+""};
    try {
      cursor = db.rawQuery(sql, sqlArgs);
      if (cursor != null && cursor.moveToFirst()) {
        return cursor.getString(0);
      }
    } catch (NullPointerException npe) {
      Log.e(TAG, npe.getMessage());
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
    return null;
  }

  public void updatePasscode(long threadId, String passcode) {
    ContentValues contentValues = new ContentValues(1);
    contentValues.put(PASSCODE, passcode);
    SQLiteDatabase db = databaseHelper.getWritableDatabase();
    db.update(TABLE_NAME, contentValues, ID + " = ? ", new String[] {threadId + ""});
  }

  public void removePasscode(long threadId) {
    ContentValues contentValues = new ContentValues(1);
    contentValues.putNull(PASSCODE);
    SQLiteDatabase db = databaseHelper.getWritableDatabase();
    db.update(TABLE_NAME, contentValues, ID + " = ? ", new String[] {threadId + ""});
  }

  public String getRecoveryAnswer(long threadId) {
    SQLiteDatabase db = databaseHelper.getReadableDatabase();
    Cursor cursor = null;
    String sql = "SELECT " + RECOVERY_ANSWER + " FROM " + TABLE_NAME + " WHERE " + ID + " = ?";
    try {
      cursor = db.rawQuery(sql, new String[] {threadId+""});
      if (cursor != null && cursor.moveToFirst()) {
        return cursor.getString(0);
      }
    } catch (NullPointerException npe) {
      Log.e(TAG, npe.getMessage());
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
    return null;
  }

  public void updateRecoveryAnswer(long threadId, String recoveryAnswer) {
    ContentValues contentValue = new ContentValues(1);
    contentValue.put(RECOVERY_ANSWER, recoveryAnswer);
    SQLiteDatabase db = databaseHelper.getWritableDatabase();
    db.update(TABLE_NAME, contentValue, ID + " = ? ", new String[] {threadId + ""});
  }

}