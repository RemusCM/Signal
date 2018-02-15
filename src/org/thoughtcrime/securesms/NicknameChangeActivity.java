package org.thoughtcrime.securesms;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.widget.EditText;
import android.widget.Toast;

import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.RecipientDatabase;
import org.thoughtcrime.securesms.jobs.MultiDeviceProfileKeyUpdateJob;
import org.thoughtcrime.securesms.recipients.Recipient;
import org.thoughtcrime.securesms.util.Util;

public class NicknameChangeActivity implements Preference.OnPreferenceClickListener {
    private Context context;
    private Recipient recipient;
    private AlertDialog.Builder nicknameDialog;

    NicknameChangeActivity(Context context, Recipient recipient) {
        this.context = context;
        this.recipient = recipient;
        nicknameDialog = new AlertDialog.Builder(context);
    }

    // performs actions when you click on the nickname preference
    // in the conversation settings
    @Override
    public boolean onPreferenceClick(Preference preference) {
        // customization for dialog box
        nicknameDialog.setTitle("Change/Add");
        nicknameDialog.setCancelable(false);

        // add an edit text in the the dialog box for entering the nickname
        final EditText nicknameEditText = new EditText(context);
        nicknameDialog.setView(nicknameEditText);

        saveButton(nicknameEditText);
        cancelButton();
        nicknameDialog.show();
        return true;
    }

    /**
     * Performs the modification or addition of nickname
     * for a contact
     * @param nicknameEditText the desired nickname
     */
    private void saveButton(EditText nicknameEditText) {
        nicknameDialog.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // check the text being passed if it is null or empty,
                // otherwise perform modification action
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

    /**
     * Dismisses the dialog on pressed cancel.
     */
    private void cancelButton() {
        nicknameDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    /**
     * Nickname cannot be empty or null.
     * @param nicknameEditText
     * @return true if dialog nickname edit text is empty
     */
    private boolean isDialogNicknameEditTextEmpty(EditText nicknameEditText) {
        return Util.isEmpty(nicknameEditText) || nicknameEditText.getText().toString().isEmpty();
    }
}

