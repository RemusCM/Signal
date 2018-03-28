package org.thoughtcrime.securesms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class PasscodeActivity extends Activity {

  private static final String TAG = PasscodeActivity.class.getSimpleName();

  static final String THREAD_ID = "threadId";
  static final String PASSCODE  = "passcode";;
  static final String ADD       = "ADD";
  static final String UPDATE    = "UPDATE";
  static final String DELETE    = "DELETE";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.passcode_list_actions);
    ListView passcodeListView = findViewById(R.id.passcode_action_list);

    Intent intent = this.getIntent();
    String threadIdStr = null;
    String passcode = null;
    if (intent != null) {
      threadIdStr = intent.getStringExtra(THREAD_ID);
      passcode = intent.getStringExtra(PASSCODE);
    }

    List<String> action = handleButtonVisibility(passcode);
    String[] data = new String[action.size()];

    PasscodeAdapter adapter = new PasscodeAdapter(this, action.toArray(data), threadIdStr);
    passcodeListView.setAdapter(adapter);

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

  private List<String> handleButtonVisibility(String passcode) {
    List<String> action = new ArrayList<>();
    if (passcode != null) {
      action.add(UPDATE);
      action.add(DELETE);
    } else {
      action.add(ADD);
    }
    return action;
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

          finish(); // reload activity so the changes is reflected
          startActivity(getIntent());
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

    AlertDialog.Builder updatePasscodeDialog = new AlertDialog.Builder(this);
    updatePasscodeDialog.setTitle("Update Passcode");
    updatePasscodeDialog.setCancelable(true);

    LayoutInflater layoutInf = getLayoutInflater();
    View updateDialogView = layoutInf.inflate(R.layout.passcode_update,null);
    updatePasscodeDialog.setView(updateDialogView);

    updatePasscodeDialog.setPositiveButton(R.string.passcode_dialog_save, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        EditText editText1 = updateDialogView.findViewById(R.id.old_passcode);
        EditText editText2 = updateDialogView.findViewById(R.id.new_passcode);

        String oldPasscodeInput = editText1.getText().toString();
        String newPasscodeInput = editText2.getText().toString();

        String[] inputs = {oldPasscodeInput, newPasscodeInput};
        PasscodeUtil items = new PasscodeUtil(Arrays.asList(inputs));

        if (items.isEmptyField()) {
          Toast.makeText(getApplicationContext(), "One of the field is empty. Please try again.", Toast.LENGTH_SHORT).show();
        } else if (!items.isValidPasscode()) {
          Toast.makeText(getApplicationContext(), "Please enter a valid passcode.", Toast.LENGTH_SHORT).show();
        } else {
          PasscodeDBhandler pdbh = new PasscodeDBhandler(getApplicationContext(), threadId);
          String passcodeFromDB = pdbh.getPasscodeIfExists();
          if(passcodeFromDB.equals(oldPasscodeInput)) {
            PasscodeDBhandler process = new PasscodeDBhandler(getApplicationContext(), threadId, newPasscodeInput);
            String result = process.update();
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

            finish(); // reload activity so the changes is reflected
            startActivity(getIntent());
          } else {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
          }
        }
      }
    });

    updatePasscodeDialog.setNegativeButton(R.string.passcode_dialog_cancel, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });

    updatePasscodeDialog.show();
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
          String result = process.add();
          Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

          finish(); // reload activity so the changes is reflected
          startActivity(getIntent());
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
        if (s.length() < MIN_LENGTH || !pattern.matcher(s).matches()) {
          cond = false;
          break;
        }
      }
      return cond;
    }
  }


}
