package org.thoughtcrime.securesms;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.IdentityDatabase;
import org.thoughtcrime.securesms.database.RecipientDatabase;
import org.thoughtcrime.securesms.database.identity.IdentityRecordList;
import org.thoughtcrime.securesms.jobs.MultiDeviceProfileKeyUpdateJob;
import org.thoughtcrime.securesms.recipients.Recipient;
import org.thoughtcrime.securesms.util.Util;

import static org.thoughtcrime.securesms.giph.util.InfiniteScrollListener.TAG;

public class NicknameChangeActivity extends ConversationActivity implements Preference.OnPreferenceClickListener {
  private Context context;
  private Recipient recipient;

  private AlertDialog.Builder soloNicknameDialog;
  private AlertDialog.Builder groupNicknamesDialog;

  NicknameChangeActivity(Context context, Recipient recipient) {
    this.context = context;
    this.recipient = recipient;
    groupNicknamesDialog = new AlertDialog.Builder(context);
    soloNicknameDialog = new AlertDialog.Builder(context);
  }

  public void groupNicknameDialog() {
    groupNicknamesDialog.setTitle("Change members' nickname");
    groupNicknamesDialog.setCancelable(false);

    IdentityDatabase identityDatabase   = DatabaseFactory.getIdentityDatabase(context);
    IdentityRecordList identityRecordList = new IdentityRecordList();
    for (Recipient recipient : recipient.getParticipants()) {
      Log.w(TAG, "Loading identity for: " + recipient.getAddress());
      identityRecordList.add(identityDatabase.getIdentity(recipient.getAddress()));
      // TODO
      // prints
//      02-15 15:36:35.255 11462-11462/org.thoughtcrime.securesms W/InfiniteScrollListener: Loading identity for: +14389797321
//      02-15 15:36:35.259 11462-11462/org.thoughtcrime.securesms W/InfiniteScrollListener: Loading identity for: +14389959811
//      02-15 15:36:35.260 11462-11462/org.thoughtcrime.securesms W/InfiniteScrollListener: Loading identity for: +15142387156
//      02-15 15:36:35.261 11462-11462/org.thoughtcrime.securesms W/InfiniteScrollListener: Loading identity for: +15146791670
//      02-15 15:36:35.262 11462-11462/org.thoughtcrime.securesms W/InfiniteScrollListener: Loading identity for: +15149163416
//      02-15 15:36:35.263 11462-11462/org.thoughtcrime.securesms W/InfiniteScrollListener: Loading identity for: +15149912693
    }


    groupNicknamesDialog.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        // TODO
      }
    });

    groupNicknamesDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });
    groupNicknamesDialog.show();
  }

  public void soloNicknameDialog() {
    soloNicknameDialog.setTitle("Change/Add");
    soloNicknameDialog.setCancelable(false);

    final EditText nicknameEditText = new EditText(context);
    soloNicknameDialog.setView(nicknameEditText);

    soloSaveButton(nicknameEditText);
    soloNicknameDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });
    soloNicknameDialog.show();
  }

  @Override
  public boolean onPreferenceClick(Preference preference) {
    if(recipient.isGroupRecipient()) {
      groupNicknameDialog();
    } else {
      soloNicknameDialog();
    }
    return true;
  }

  private void soloSaveButton(EditText nicknameEditText) {
    soloNicknameDialog.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
      @SuppressLint("StaticFieldLeak")
      @Override
      public void onClick(DialogInterface dialog, int which) {
        if(isDialogNicknameEditTextEmpty(nicknameEditText)) {
          dialog.dismiss();
          Toast.makeText(context, "Please enter a valid nickname.",
                  Toast.LENGTH_SHORT).show();
        } else {
          new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
              RecipientDatabase database   = DatabaseFactory.getRecipientDatabase(context);
              database.setDisplayName(recipient, nicknameEditText.getText().toString());
              database.setCustomLabel(recipient, recipient.getAddress().serialize());
              ApplicationContext.getInstance(context)
                      .getJobManager()
                      .add(new MultiDeviceProfileKeyUpdateJob(context));
              return null;
            }
          }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
      }
    });
  }

  private boolean isDialogNicknameEditTextEmpty(EditText nicknameEditText) {
    return Util.isEmpty(nicknameEditText) || nicknameEditText.getText().toString().isEmpty();
  }
}