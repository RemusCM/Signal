package org.thoughtcrime.securesms;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ScheduleActivity extends Activity {
  private static final String TAG = ScheduleActivity.class.getSimpleName();

  private EditText smsEditText;
  private EditText receiverEditText;
  private TextView yearTextView;
  private TextView monthTextView;
  private TextView dayTextView;
  private TextView hourTextView;
  private TextView minuteTextView;

  private static final int REQUEST_CODE = 1;
  private static int eventID = 0;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.schedule_activity);

    yearTextView         = findViewById(R.id.year_tv);
    monthTextView        = findViewById(R.id.month_tv);
    dayTextView          = findViewById(R.id.day_tv);
    hourTextView         = findViewById(R.id.hour_tv);
    minuteTextView       = findViewById(R.id.minute_tv);

    receiverEditText     = findViewById(R.id.edit_text_phone);
    smsEditText          = findViewById(R.id.edit_text_message);
    Button timeSelectBtn = findViewById(R.id.time_select_button);
    Button setBtn        = findViewById(R.id.set_schedule_button);
    Button cancelBtn     = findViewById(R.id.cancel_schedule_button);
    Button contactBtn    = findViewById(R.id.contact);

    contactBtn.setOnClickListener(new ContactBtnClickListener());

    timeSelectBtn.setOnClickListener(v -> {
      showDatePickerDialog(v);
      showTimePickerDialog(v);
    });

    cancelBtn.setOnClickListener(v -> ScheduleActivity.super.onBackPressed());

    setBtn.setOnClickListener(new SetButtonListener(ScheduleActivity.this));

  }

  private class SetButtonListener implements View.OnClickListener {
    private Context context;
    SetButtonListener(Context context) {
      this.context = context;
    }

    @Override
    public void onClick(View v) {
      try {
        ScheduleMessage scheduleMessage = new ScheduleMessage();

        scheduleMessage.setPhoneNumber(receiverEditText.getText().toString());
        scheduleMessage.setSmsMessage(smsEditText.getText().toString());

        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourTextView.getText().toString()) );
        calendar.set(Calendar.MINUTE, Integer.parseInt(minuteTextView.getText().toString()));
        calendar.set(Calendar.YEAR, Integer.parseInt(yearTextView.getText().toString()));
        calendar.set(Calendar.MONTH, Integer.parseInt(monthTextView.getText().toString())-1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dayTextView.getText().toString()));
       

        String num = scheduleMessage.getPhoneNumber();
        String msg = scheduleMessage.getSmsMessage();

        Intent alarmIntent = new Intent(context, CustomAlarmReceiver.class);
        alarmIntent.putExtra(CustomAlarmReceiver.MESSAGE_EXTRA, msg);
        alarmIntent.putExtra(CustomAlarmReceiver.PHONE_EXTRA, num);

        PendingIntent pendingAlarm = PendingIntent.getBroadcast(context,
                eventID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        eventID += 1;

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingAlarm);

        Toast.makeText(context, "Message scheduled!", Toast.LENGTH_SHORT).show();
      }
      catch(Exception e){
        Toast.makeText(context, "Invalid, Try again.", Toast.LENGTH_SHORT).show();
      }
    }
  }

  public class ContactBtnClickListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
      Uri uri = Uri.parse("content://contacts");
      Intent intent = new Intent(Intent.ACTION_PICK, uri);
      intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
      startActivityForResult(intent, REQUEST_CODE);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent i) {
    super.onActivityResult(requestCode, resultCode, i);
    if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
      Uri uri = i.getData();
      String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

      Cursor cursor = getContentResolver().query(uri, projection,
              null, null, null);
      cursor.moveToFirst();

      int numberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
      String number = cursor.getString(numberColumnIndex);

      receiverEditText.setText(number);

    }
  }


  public void showTimePickerDialog(View v) {
    DialogFragment timePickerFragment = new TimePickerFragment();
    timePickerFragment.show(getFragmentManager(), "timePicker");
  }

  public void showDatePickerDialog(View v) {
    DialogFragment datePickerFragment = new DatePickerFragment();
    datePickerFragment.show(getFragmentManager(), "datePicker");
  }

  @SuppressLint("ValidFragment")
  public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      final Calendar c = Calendar.getInstance();
      int hour         = c.get(Calendar.HOUR_OF_DAY);
      int minute       = c.get(Calendar.MINUTE);

      return new TimePickerDialog(getActivity(), this, hour, minute,
              DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
      hourTextView.setText(String.valueOf(hourOfDay));
      minuteTextView.setText(String.valueOf(minute));
    }
  }

  @SuppressLint("ValidFragment")
  public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      final Calendar c = Calendar.getInstance();
      int year         = c.get(Calendar.YEAR);
      int month        = c.get(Calendar.MONTH);
      int day          = c.get(Calendar.DAY_OF_MONTH);

      DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
      datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis() - 1000);
      return datePickerDialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
      dayTextView.setText(String.valueOf(day));
      monthTextView.setText(String.valueOf(month + 1));
      yearTextView.setText(String.valueOf(year));
    }
  }

  private class ScheduleMessage {
    String phoneNumber;
    String smsMessage;

    public ScheduleMessage (){
      phoneNumber = null;
      smsMessage = null;
    }



    public String getPhoneNumber() {
      return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
      this.phoneNumber = phoneNumber;
    }

    public String getSmsMessage() {
      return smsMessage;
    }

    public void setSmsMessage(String smsMessage) {
      this.smsMessage = smsMessage;
    }

    private boolean areValidFields() {
      return !(getPhoneNumber().isEmpty() && getSmsMessage().isEmpty());
    }

    // validate phone number
    // validate message length
    private String deleteAll(String strValue, String charToRemove) {
      return strValue.replaceAll(charToRemove, "");
    }

  }

}
