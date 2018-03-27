package org.thoughtcrime.securesms.database;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

public class PasscodeDatabase extends ThreadDatabase {

  PasscodeDatabase(Context context, SQLiteOpenHelper databaseHelper) {
    super(context, databaseHelper);
  }

  public boolean isPasscodeExist(long threadId) {
    return false;
  }

  public String getPasscodeByThreadId(long threadId) {
    return "";
  }

  public void updatePasscode(long threadId, String passcode) {

  }

  public void removePasscode(long threadId, String passcode) {

  }
}