package org.thoughtcrime.securesms;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.RecipientDatabase;
import org.thoughtcrime.securesms.jobs.MultiDeviceProfileKeyUpdateJob;
import org.thoughtcrime.securesms.recipients.Recipient;
import org.thoughtcrime.securesms.util.Util;

import java.util.LinkedList;
import java.util.List;

@SuppressLint("Registered")
public class NicknameChangeActivity implements Preference.OnPreferenceClickListener {
  private final Context context;
  private final Recipient recipient;

  private NicknameUtil nicknameUtil = new NicknameUtil();

  private AlertDialog.Builder soloNicknameDialogBuilder;
  private AlertDialog.Builder groupNicknamesDialogBuilder;
  private AlertDialog.Builder groupNicknameInnerDialogBuilder;

  private List<String> recipientsList = new LinkedList<>();
  private String[] names;
  private LinkedList<Recipient> members = new LinkedList<>();

  NicknameChangeActivity(Context context, Recipient recipient) {
    this.context = context;
    this.recipient = recipient;
    this.soloNicknameDialogBuilder = new AlertDialog.Builder(context);
    this.groupNicknamesDialogBuilder = new AlertDialog.Builder(context);
    this.groupNicknameInnerDialogBuilder = new AlertDialog.Builder(context);
  }

  @Override
  public boolean onPreferenceClick(Preference preference) {
    if(recipient.isGroupRecipient()) {
      showGroupMembersNicknameDialog();
    } else {
      showSoloNicknameDialog();
    }
    return true;
  }


  private void showSoloNicknameDialog() {
    soloNicknameDialogBuilder.setTitle(R.string.dialog_nickname_title);
    soloNicknameDialogBuilder.setCancelable(true);

    final EditText nicknameEditText = new EditText(context);
    soloNicknameDialogBuilder.setView(nicknameEditText);
    soloSaveButton(nicknameEditText);

    soloNicknameDialogBuilder.setNegativeButton(R.string.dialog_nickname_cancel, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });
    soloNicknameDialogBuilder.show();
  }


  private void showGroupMembersNicknameDialog() {
    groupNicknamesDialogBuilder = new AlertDialog.Builder(context);
    groupNicknamesDialogBuilder.setTitle(R.string.dialog_nickname_title);
    groupNicknamesDialogBuilder.setCancelable(true);

    if (recipientsList.size() == 0) {
      for (Recipient recipient : recipient.getParticipants()) {
        if (Util.isOwnNumber(context, recipient.getAddress())) {
          recipientsList.add("Me");
          members.add(recipient);
        } else {
          members.add(recipient);
          String name = recipient.toShortString();
          if (recipient.getName() == null && !TextUtils.isEmpty(recipient.getProfileName())) {
            name += " ~" + recipient.getProfileName();
          }
          recipientsList.add(name);
        }
      }
      names = recipientsList.toArray(new String[recipient.getParticipants().size()]);
    }
    // context given is the group conversation, not a specific group member
    groupNicknamesDialogBuilder.setItems(names, new NicknameChangeOnClickListener(context, members));

    groupNicknamesDialogBuilder.setNegativeButton(R.string.dialog_nickname_cancel, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });

    groupNicknamesDialogBuilder.show();

  }


  private void soloSaveButton(EditText nicknameEditText) {
    soloNicknameDialogBuilder.setPositiveButton(R.string.dialog_nickname_save, new DialogInterface.OnClickListener() {
      @SuppressLint("StaticFieldLeak")
      @Override
      public void onClick(DialogInterface dialog, int which) {
        if(nicknameUtil.isValidNicknameTest(nicknameEditText)) {
          dialog.dismiss();
          nicknameUtil.displayFailureModifyingMessage();
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
          nicknameUtil.displaySuccessModifyingMessage();
        }
      }
    });
  }


  private void showSpecificGroupMember(Context context, Recipient recipient) {
    groupNicknameInnerDialogBuilder.setTitle(R.string.dialog_nickname_title);
    groupNicknameInnerDialogBuilder.setCancelable(false);
    final EditText nicknameEditText = new EditText(context);

    groupNicknameInnerDialogBuilder.setPositiveButton(R.string.dialog_nickname_save, new DialogInterface.OnClickListener() {
      @SuppressLint("StaticFieldLeak")
      @Override
      public void onClick(DialogInterface dialog, int which) {
        if (nicknameUtil.isValidNicknameTest(nicknameEditText)) {
          dialog.dismiss();
          nicknameUtil.displayFailureModifyingMessage();
        } else {
          new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
              RecipientDatabase database = DatabaseFactory.getRecipientDatabase(context);
              database.setDisplayName(recipient, nicknameEditText.getText().toString());
              database.setCustomLabel(recipient, recipient.getAddress().serialize());
              ApplicationContext.getInstance(context)
                      .getJobManager()
                      .add(new MultiDeviceProfileKeyUpdateJob(context));
              groupNicknamesDialogBuilder = new AlertDialog.Builder(context);
              groupNicknameInnerDialogBuilder = new AlertDialog.Builder(context);
              recipientsList = new LinkedList<>();
              return null;
            }
          }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
          nicknameUtil.displaySuccessModifyingMessage();
        }
      }
    });

    groupNicknameInnerDialogBuilder.setView(nicknameEditText);

    groupNicknameInnerDialogBuilder.setNegativeButton(R.string.dialog_nickname_cancel, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        showGroupMembersNicknameDialog();
      }
    });

    groupNicknameInnerDialogBuilder.show();
  }


  private class NicknameChangeOnClickListener implements DialogInterface.OnClickListener {
    private final Context context;
    private final LinkedList<Recipient> members;

    NicknameChangeOnClickListener(Context context, LinkedList<Recipient> members) {
      this.context = context;
      this.members = members;
    }

    @Override
    public void onClick(DialogInterface dialog, int item) {
      Recipient recipient = members.get(item);
      dialog.dismiss();
      showSpecificGroupMember(context, recipient);
    }
  }


  private class NicknameUtil {
    NicknameUtil() {}
    private final int MAX_NICKNAME_TEXT_LENGTH = 10;
    private final int MIN_NICKNAME_TEXT_LENGTH = 1;

    // returns true if nickname is invalid
    private boolean isValidNicknameTest(EditText nicknameEditText) {
      boolean condition = false;
      String editTextStr = String.valueOf(nicknameEditText.getText());
      if(!Util.isEmpty(nicknameEditText) || editTextStr.length() >= MIN_NICKNAME_TEXT_LENGTH) {
        if(nicknameEditText.getText().toString().length() > MAX_NICKNAME_TEXT_LENGTH) {
          condition = true; // invalid nickname, nickname entered is too long
        }
      } else {
        condition = true; // invalid nickname, no nickname entered
      }
      return condition;
    }

    private void displaySuccessModifyingMessage() {
      Toast.makeText(context, R.string.success_modifying_nickname, Toast.LENGTH_SHORT).show();
    }

    private void displayFailureModifyingMessage() {
      Toast.makeText(context, R.string.invalid_nickname, Toast.LENGTH_SHORT).show();
    }
  }

}