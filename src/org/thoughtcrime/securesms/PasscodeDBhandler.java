package org.thoughtcrime.securesms;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.thoughtcrime.securesms.database.DatabaseFactory;

public class PasscodeDBhandler {
  private static final String TAG = PasscodeDBhandler.class.getSimpleName();

  private static final String PASSCODE = "passcode";

  private Context context;
  private long threadId;
  private String passcode;

  public String getPasscode() {
    return passcode;
  }

  public long getThreadId() {
    return threadId;
  }

  PasscodeDBhandler(Context context, long threadId, String passcode) {
    this.context = context;
    this.threadId = threadId;
    this.passcode = passcode;
  }

  PasscodeDBhandler(Context context, long threadId) {
    this.context = context;
    this.threadId = threadId;
  }

  /**
   * Given a threadId and a passcode,
   * get the passcode for this thread
   * if it exists
   * @return the passcode currently
   * stored in the database
   */
  public String getPasscodeIfExists() {
    return DatabaseFactory.getPasscodeDatabase(context).getPasscodeByThreadId(threadId);
  }

  /**
   * Given a threadId and passcode,
   * check if passcode field is set
   * in the thread database
   * @return true if passcode exists for this thread
   */
  private boolean isPasscodeExists() {
    return getPasscodeIfExists() != null;
  }

  /**
   * Given a threadId and a passcode,
   * update the passcode field
   * in the thread database
   * @return updates the passcode field
   */
  public String update() {
    String passcodeInDb = getPasscodeIfExists();
    try {
      if (!passcodeInDb.isEmpty()) {
        // confirmation : checks if the one entered is the same as the one in database
        if (passcodeInDb.equals(passcode)) {
          DatabaseFactory.getPasscodeDatabase(context).updatePasscode(threadId, passcode);
          if (DatabaseFactory.getPasscodeDatabase(context).getPasscodeByThreadId(threadId).equals(passcode)) {
            return "Success";
          }
        }
      }
    } catch (NullPointerException e) {
      Toast.makeText(context, "There is nothing to update.", Toast.LENGTH_SHORT).show();
    }
    return "Error";
  }

  public String add() {
    String passcodeInDb = getPasscodeIfExists();
    // by default passcode field is null
    if (passcodeInDb == null) {
      DatabaseFactory.getPasscodeDatabase(context).updatePasscode(threadId, passcode);
      if (DatabaseFactory.getPasscodeDatabase(context).getPasscodeByThreadId(threadId).equals(passcode)) {
        return "Success";
      }
    }
    return "Error";
  }

  /**
   * Given a threadId and a passcode,
   * remove the passcode stored in
   * the passcode field in the
   * thread database
   * @return removes the passcode stored
   * in the field
   */
  public String delete() {
    String passcodeInDb = getPasscodeIfExists();
    try {
      if (!passcodeInDb.isEmpty()) {
        // confirmation : checks if the one entered is the same as the one in database
        if (passcodeInDb.equals(passcode)) {
          DatabaseFactory.getPasscodeDatabase(context).removePasscode(threadId);
          if (DatabaseFactory.getPasscodeDatabase(context).getPasscodeByThreadId(threadId) == null) {
            return "Success";
          }
        }
      }
    } catch (NullPointerException e) {
      Toast.makeText(context, "There is nothing to delete.", Toast.LENGTH_SHORT).show();
    }
    return "Error";
  }
}
