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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class PasscodeActivity extends Activity {

  private static final String TAG = PasscodeActivity.class.getSimpleName();

  static final String THREAD_ID        = "threadId";
  static final String PASSCODE         = "passcode";
  static final String ADD              = "ADD";
  static final String UPDATE           = "UPDATE";
  static final String DELETE           = "DELETE";
  static final String RECOVER_PASSCODE = "FORGOT PASSCODE";

  ListView passcodeListView;
  Spinner recoveryQuestionSpinner;
  EditText recoveryAnswerEditText;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.passcode_list_actions);
    passcodeListView = findViewById(R.id.passcode_action_list);

    // spinner for recovery questions
    recoveryQuestionSpinner = findViewById(R.id.recovery_questions);
    recoveryQuestionSpinner.setOnItemSelectedListener(new RecoveryQuestionItemSelectedListener(this));
    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
            android.R.layout.simple_spinner_item, getRecoveryQuestions());
    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    recoveryQuestionSpinner.setAdapter(dataAdapter);
    // for entering recovery answer
    recoveryAnswerEditText = findViewById(R.id.recovery_answer);
    recoveryAnswerEditText.clearFocus();

    // get the intent object passed by ConversationListFragment:handleLockWithPasscodeSelected
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
    passcodeListView.setOnItemClickListener(new PasscodeActionClickListener(this));
  }

  public void onClickBackBtn(View view) {
    PasscodeActivity.super.onBackPressed();
  }

  private class PasscodeActionClickListener implements AdapterView.OnItemClickListener {
    PasscodeActionClickListener(PasscodeActivity passcodeActivity) { }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      String clickedItem = (String) parent.getItemAtPosition(position);
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
        case RECOVER_PASSCODE:
          handleRecoverPasscode(threadId);
        default:
          break;
      }
    }
  }

  private List<String> getRecoveryQuestions() {
    // Spinner Drop down elements -> Recovery Questions
    List<String> recoveryQuestions = new ArrayList<>();
    recoveryQuestions.add("Select recovery question");
    recoveryQuestions.add("What is your mother's maiden name?");
    recoveryQuestions.add("What street did you grow up on?");
    recoveryQuestions.add("What is the name of your first school?");
    recoveryQuestions.add("What is your pet's name?");
    recoveryQuestions.add("What is your father's middle name?");
    recoveryQuestions.add("What was the model of your first car?");
    return recoveryQuestions;
  }

  private class RecoveryQuestionItemSelectedListener implements AdapterView.OnItemSelectedListener {
    private Context context;
    RecoveryQuestionItemSelectedListener(Context context) {
      this.context = context;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      String selected = (String) parent.getItemAtPosition(position);
      Log.i(TAG, selected + ": position: " + position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }
  }

  private List<String> handleButtonVisibility(String passcode) {
    List<String> action = new ArrayList<>();
    if (passcode != null) {
      action.add(UPDATE);
      action.add(DELETE);
      action.add(RECOVER_PASSCODE);
      recoveryQuestionSpinner.setVisibility(View.GONE);
      recoveryAnswerEditText.setVisibility(View.GONE);
    } else {
      action.add(ADD);
    }
    return action;
  }

  public int getSelectedRecoveryQuestionItem() {
    return recoveryQuestionSpinner.getSelectedItemPosition();
  }

  private String getRecoveryAnswer() {
    return recoveryAnswerEditText.getText().toString();
  }

  private void disableAddPasscodeRecoverPasscodeComponents() {
    recoveryQuestionSpinner.setEnabled(false);
    recoveryAnswerEditText.setEnabled(false);
    passcodeListView.setVisibility(View.GONE);
  }

  private boolean isValideRecoveryQuestionAndAnswer() {
    // validation before add -> make sure recovery question is selected and answer is provided
    // item at 0 -> "Select a recovery question" which is invalid
    return getSelectedRecoveryQuestionItem() != 0 && !getRecoveryAnswer().isEmpty();
  }

  private void handleRecoverPasscode(long threadId) {
    AlertDialog.Builder recoveryDialog = new AlertDialog.Builder(this);
    recoveryDialog.setTitle(R.string.passcode_recovery_title);
    recoveryDialog.setMessage("Passcode will be displayed after entering the correct recovery answer.");
    recoveryDialog.setCancelable(true);

    LayoutInflater inflater = this.getLayoutInflater();
    View recoverPasscodeView = inflater.inflate(R.layout.passcode_forgot_passcode, null);

    // get the recovery answer from the database
    PasscodeDBhandler passcodeDBhandler = new PasscodeDBhandler(getApplicationContext(), threadId);
    String answer = passcodeDBhandler.getRecoveryAnswerIfExists();
    int lastIndexOfComma = passcodeDBhandler.getRecoveryAnswerIfExists().lastIndexOf(",");
    // remember <recoveryAnswer>,<questionIndex>, this will return question index
    int selectedQuestionIndex = Integer.parseInt(answer.substring(lastIndexOfComma + 1));
    // provide the question chosen before in the TextView
    TextView recoveryQuestionTv = recoverPasscodeView.findViewById(R.id.recovery_question_tv);
    recoveryQuestionTv.setText(getRecoveryQuestions().get(selectedQuestionIndex));
    recoveryDialog.setView(recoverPasscodeView);

    recoveryDialog.setPositiveButton(R.string.ok, (dialog, which) -> {
      EditText editText = recoverPasscodeView.findViewById(R.id.entered_recovery_answer);
      String recoveryAnswer = editText.getText().toString();
      PasscodeUtil items = new PasscodeUtil(Arrays.asList(recoveryAnswer));
      if (items.isEmptyField()) {
        Toast.makeText(getApplicationContext(), R.string.passcode_invalid_message4, Toast.LENGTH_SHORT).show();
      } else {
        PasscodeDBhandler handler = new PasscodeDBhandler(getApplicationContext(), threadId);
        String recoveryAnswerFromDbFull = handler.getRecoveryAnswerIfExists();
        String answerFromDBWithoutIndex = recoveryAnswerFromDbFull.substring(0, recoveryAnswerFromDbFull.lastIndexOf(","));
        if (answerFromDBWithoutIndex.equals(recoveryAnswer)) {
          // provide the passcode if recovery answer is correct
          Toast.makeText(getApplicationContext(), "Your passcode was " +
                          handler.getPasscodeIfExists(), Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(getApplicationContext(), R.string.passcode_recovery_incorrect_answer, Toast.LENGTH_SHORT).show();
        }
      }
    });
    recoveryDialog.setNegativeButton(R.string.passcode_dialog_cancel, (dialog, which) -> dialog.dismiss());
    recoveryDialog.show();
  }

  private void handleDelete(long threadId) {
    AlertDialog.Builder deletePasscodeDialog = new AlertDialog.Builder(this);
    deletePasscodeDialog.setTitle(R.string.passcode_delete_title);
    deletePasscodeDialog.setCancelable(true);
    deletePasscodeDialog.setMessage(R.string.passcode_confirm_deletion);

    LayoutInflater inflater = this.getLayoutInflater();
    View deleteEditTextView = inflater.inflate(R.layout.passcode_delete, null);
    deletePasscodeDialog.setView(deleteEditTextView);

    deletePasscodeDialog.setPositiveButton(R.string.passcode_confirm_button, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        EditText editText = deleteEditTextView.findViewById(R.id.delete_passcode);
        String editTextStr = editText.getText().toString();
        PasscodeUtil items = new PasscodeUtil(Arrays.asList(editTextStr));
        if (items.isEmptyField()) {
          Toast.makeText(getApplicationContext(), R.string.passcode_invalid_message2, Toast.LENGTH_SHORT).show();
        } else if (!items.isValidPasscode()) {
          Toast.makeText(getApplicationContext(), R.string.passcode_invalid_message, Toast.LENGTH_SHORT).show();
        } else {
          PasscodeDBhandler process = new PasscodeDBhandler(getApplicationContext(), threadId, editTextStr);
          Toast.makeText(getApplicationContext(), process.delete(), Toast.LENGTH_SHORT).show();
          finish(); // reload activity so the changes is reflected
          startActivity(getIntent());
        }
      }
    });

    deletePasscodeDialog.setNegativeButton(R.string.passcode_dialog_cancel, new DialogInterface.OnClickListener() {
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
          Toast.makeText(getApplicationContext(), R.string.passcode_invalid_message, Toast.LENGTH_SHORT).show();
        } else {
          PasscodeDBhandler pdbh = new PasscodeDBhandler(getApplicationContext(), threadId);
          String passcodeFromDB = pdbh.getPasscodeIfExists();
          if(passcodeFromDB != null) {
            if (!passcodeFromDB.equals(oldPasscodeInput)) {
              Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            } else {
              PasscodeDBhandler process = new PasscodeDBhandler(getApplicationContext(), threadId, newPasscodeInput);
              Toast.makeText(getApplicationContext(), process.update(), Toast.LENGTH_SHORT).show();

              finish(); // reload activity so the changes is reflected
              startActivity(getIntent());
            }
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
    // prompt the user to enter recovery answer first
    if (isValideRecoveryQuestionAndAnswer()) {
      AlertDialog.Builder addPasscodeDialog = new AlertDialog.Builder(this);
      addPasscodeDialog.setTitle(R.string.passcode_set);
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
            Toast.makeText(getApplicationContext(), R.string.passcode_invalid_message2, Toast.LENGTH_SHORT).show();
          } else if (!items.isValidPasscode()) {
            Toast.makeText(getApplicationContext(), R.string.passcode_invalid_message, Toast.LENGTH_SHORT).show();
          } else {
            // for adding recovery answer
            // update recovery answer field with the one provided
            // index of question selected is included in the answer i.e. <recoveryAnswer>,<questionIndex>
            String recoveryAnswer = getRecoveryAnswer() + "," + String.valueOf(getSelectedRecoveryQuestionItem());
            PasscodeDBhandler passcodeDBhandler = new PasscodeDBhandler(
                    getApplicationContext(), threadId, passcode, recoveryAnswer);
            passcodeDBhandler.updateRecoveryAnswer();

            // for adding passcode
            PasscodeDBhandler process = new PasscodeDBhandler(getApplicationContext(), threadId, passcode);
            String result = process.add();
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            disableAddPasscodeRecoverPasscodeComponents();
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
    } else {
      Toast.makeText(this, "Please provide a recovery answer.", Toast.LENGTH_SHORT).show();
    }
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
