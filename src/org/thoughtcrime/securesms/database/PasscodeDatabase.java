package org.thoughtcrime.securesms.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PasscodeDatabase extends ThreadDatabase {

  PasscodeDatabase(Context context, SQLiteOpenHelper databaseHelper) {
    super(context, databaseHelper);
  }

  public boolean hasPasscodeByThreadId(long threadId) {
    SQLiteDatabase db = databaseHelper.getReadableDatabase();
    Cursor cursor = null;
    String sql = "SELECT " + PASSCODE + " FROM " + TABLE_NAME + " WHERE " + ID + " = ?";
    String[] sqlArgs = new String[] {threadId+""};
    try {
      cursor = db.rawQuery(sql, sqlArgs);
      if (cursor != null && cursor.moveToFirst()) {
        return true;
      } else {
        return false;
      }
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
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
      } else {
        return null;
      }
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }

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
}