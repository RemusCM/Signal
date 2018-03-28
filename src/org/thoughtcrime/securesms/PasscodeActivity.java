package org.thoughtcrime.securesms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class PasscodeActivity extends Activity {

  static final String THREAD_ID = "threadId";
  static final String ADD       = "ADD";
  static final String UPDATE    = "UPDATE";
  static final String DELETE    = "DELETE";


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
        String threadIdStr = threadIdText.getText().toString();
        long threadId = Long.parseLong(threadIdStr);
        switch (clickedItem) {
          case ADD:
            handleAdd(threadId);
            break;
          case UPDATE:
            handleUpdate(threadId);
            break;
          case DELETE:
            handleDelete(threadId);
            break;
          default:
            break;
        }
      }
    });

  }

  private void handleDelete(long threadId) {
    AlertDialog.Builder deletePasscodeDialog = new AlertDialog.Builder(this);
    deletePasscodeDialog.setTitle(R.string.delete_passcode_title);
    deletePasscodeDialog.setCancelable(true);
    deletePasscodeDialog.setMessage(R.string.confirm_deletion_with_passcode);

    LayoutInflater inflater = this.getLayoutInflater();
    View deleteEditTextView = inflater.inflate(R.layout.passcode_delete, null);
    deletePasscodeDialog.setView(deleteEditTextView);

    deletePasscodeDialog.setPositiveButton(R.string.confirm_button_passcode, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        EditText editText = deleteEditTextView.findViewById(R.id.delete_passcode);
        String editTextStr = editText.getText().toString();
        PasscodeUtil items = new PasscodeUtil(Arrays.asList(editTextStr));
        if (items.isEmptyField()) {
          Toast.makeText(getApplicationContext(), R.string.no_passcode_entered, Toast.LENGTH_SHORT).show();
        } else if (!items.isValidPasscode()) {
          Toast.makeText(getApplicationContext(), R.string.please_enter_a_valid_passcode, Toast.LENGTH_SHORT).show();
        } else {
          PasscodeDBhandler process = new PasscodeDBhandler(getApplicationContext(), threadId, editTextStr);
          String result = process.delete();
          Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
      }
    });

    deletePasscodeDialog.setNegativeButton(R.string.cancel_button_passcode, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });

    deletePasscodeDialog.show();
  }

  private void handleUpdate(long threadId) {
    // TODO
    // create an alert dialog
    // insert passcode_update.xml to this dialog
    // call the passcode handler for handling passcode modification
    //   transaction is done in dialog's ok button
  }

  public void handleAdd(long threadId) {
    AlertDialog.Builder addPasscodeDialog = new AlertDialog.Builder(this);
    addPasscodeDialog.setTitle(R.string.set_passcode);
    addPasscodeDialog.setCancelable(true);

    LayoutInflater layoutInf = getLayoutInflater();
    View addDialogView = layoutInf.inflate(R.layout.passcode_add,null);
    addPasscodeDialog.setView(addDialogView);

    addPasscodeDialog.setPositiveButton(R.string.passcode_dialog_save, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        EditText editText = addDialogView.findViewById(R.id.enter_passcode);
        String passcode = editText.getText().toString();
        PasscodeUtil items = new PasscodeUtil(Arrays.asList(passcode));
        if (items.isEmptyField()) {
          Toast.makeText(getApplicationContext(), R.string.no_passcode_entered, Toast.LENGTH_SHORT).show();
        } else if (!items.isValidPasscode()) {
          Toast.makeText(getApplicationContext(), R.string.please_enter_a_valid_passcode, Toast.LENGTH_SHORT).show();
        } else {
          PasscodeDBhandler process = new PasscodeDBhandler(getApplicationContext(), threadId, passcode);
          String result = process.update();
          Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
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

  private class PasscodeUtil {

    static final int MAX_LENGTH = 4;
    static final int MIN_LENGTH = 4;

    List<String> items = new LinkedList<>();

    PasscodeUtil(List<String> list) {
      this.items = list;
    }

    private boolean isEmptyField() {
      return items.contains(null) || items.contains("");
    }

    /**
     * Valid passcode must be a number and exactly four digits
     *
     * @return true if password is valid
     */
    private boolean isValidPasscode() {
      boolean cond = true;
      String regex = "\\d{4}";
      Pattern pattern = Pattern.compile(regex);
      for (String s : items) {
        if (s.length() > MAX_LENGTH || s.length() < MIN_LENGTH || !pattern.matcher(s).matches()) {
          cond = false;
          break;
        }
      }
      return cond;
    }
  }


}
