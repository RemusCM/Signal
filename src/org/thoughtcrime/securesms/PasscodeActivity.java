package org.thoughtcrime.securesms;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.RecipientDatabase;
import org.thoughtcrime.securesms.database.ThreadDatabase;
import org.thoughtcrime.securesms.jobs.MultiDeviceProfileKeyUpdateJob;


public class PasscodeActivity extends Activity {

  private final Context context;
  private final Thread thread;

  static final String THREAD_ID = "threadId";
  static final String ADD       = "ADD";
  static final String UPDATE    = "UPDATE";
  static final String DELETE    = "DELETE";

  private AlertDialog.Builder addPasscodeDialog;

  PasscodeActivity(Context context, Thread thread) {
    this.context = context;
    this.thread = thread;
    this.addPasscodeDialog = new AlertDialog.Builder(context);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.passcode_list_actions);
    ListView passcodeListView = findViewById(R.id.passcode_action_list);

    Intent intent = this.getIntent();
    String threadId = null;
    String passcode = null;
    if (intent != null) {
      threadId = intent.getStringExtra(THREAD_ID);
      // TODO
      // using this threadId
      // checks if this thread has passcode using the passcode handler
      // if yes get the passcode
      // initiate passcode to this value
    }

    String[] data = new String[3];
    data[0] = ADD;
    data[1] = UPDATE;
    data[2] = DELETE;

    PasscodeAdapter adapter = new PasscodeAdapter(this, data, threadId);
    passcodeListView.setAdapter(adapter);

    // TODO
    // handleButtonVisibility(passcode) : handle action button visibility
    // passcodeListView.getChildAt([0-2]).setEnabled(false);
    // by default buttons are enabled however
    // disable ADD if passcode is already set
    // disable UPDATE if passcode is not yet set for this thread
    // disable DELETE if passcode is not yet set for this thread

    passcodeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String clickedItem = (String) passcodeListView.getItemAtPosition(position);
        TextView threadIdText = view.findViewById(R.id.thread_id_text);
        String threadIdStr = threadIdText.toString();
        long threadId = Long.parseLong(threadIdStr);
        switch (clickedItem) {
          case ADD:
            handleAdd(threadId);
            Toast.makeText(getBaseContext(), "Update was called", Toast.LENGTH_SHORT).show();
            break;
          case UPDATE:
            handleUpdate(threadId);
            Toast.makeText(getBaseContext(), "Update was called", Toast.LENGTH_SHORT).show();
            break;
          case DELETE:
            handleDelete(threadId);
            Toast.makeText(getBaseContext(), "Delete was called", Toast.LENGTH_SHORT).show();
            break;
          default:
            Toast.makeText(getBaseContext(), "Invalid action", Toast.LENGTH_SHORT).show();
            break;
        }
      }
    });

  }

  private void handleDelete(long threadId) {
    // TODO
    // create an alert dialog
    // insert passcode_add.xml to this dialog
    // call the passcode handler for handling addition
    //   transaction is done in alert dialog's ok button
  }

  private void handleUpdate(long threadId) {
    // TODO
    // create an alert dialog
    // insert passcode_update.xml to this dialog
    // call the passcode handler for handling passcode modification
    //   transaction is done in dialog's ok button
  }

  public void handleAdd(long threadId) {
    addPasscodeDialog.setTitle("Set Passcode");
    addPasscodeDialog.setCancelable(true);

    LayoutInflater layoutInf = getLayoutInflater();
    View v1 = layoutInf.inflate(R.layout.passcode_add,null);
    addPasscodeDialog.setView(v1);

    EditText et1 = findViewById(R.id.enter_passcode);
    String passcode = et1.getText().toString();

    addPasscodeDialog.setPositiveButton(R.string.passcode_dialog_save, new DialogInterface.OnClickListener() {
      @SuppressLint("StaticFieldLeak")
      @Override
      public void onClick(DialogInterface dialog, int which) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
              ThreadDatabase db = DatabaseFactory.getThreadDatabase(context);
              db.updatePasscode(passcode, threadId);
              ApplicationContext.getInstance(context)
                      .getJobManager()
                      .add(new MultiDeviceProfileKeyUpdateJob(context));
              return null;
            }
          }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
      });

    addPasscodeDialog.setNegativeButton(R.string.passcode_dialog_cancel, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });

    addPasscodeDialog.show();
  }

  public class PasscodeAdapter extends BaseAdapter {
    Context context;
    String[] data;
    String threadId;
    LayoutInflater inflater;

    PasscodeAdapter(Context context, String[] data, String threadId) {
      this.context = context;
      this.data = data;
      this.threadId = threadId;
      inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
      return data.length;
    }

    @Override
    public Object getItem(int position) {
      return data[position];
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      View view = convertView;
      if (view == null) {
        view = inflater.inflate(R.layout.passcode_row_action_item, null);
      }

      TextView actionText = view.findViewById(R.id.action_text);
      actionText.setText(data[position]);

      TextView threadIdText = view.findViewById(R.id.thread_id_text);
      threadIdText.setText(threadId);

      return view;
    }
  }

}
