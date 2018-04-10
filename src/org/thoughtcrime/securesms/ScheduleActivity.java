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
import android.os.Bundle;
import android.os.Parcelable;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.thoughtcrime.securesms.database.Address;
import org.thoughtcrime.securesms.recipients.Recipient;

import java.util.Calendar;

/**
 * Handles scheduling a message which include
 * inputting the message and selecting the time
 * and date.
 */
public class ScheduleActivity extends Activity {
  private static final String TAG = ScheduleActivity.class.getSimpleName();

  public static final String THREAD_ID_EXTRA      = "thread_id";
  public static final String NUMBER_EXTRA         = "number";

  private EditText smsEditText;
  private TextView yearTextView;
  private TextView monthTextView;
  private TextView dayTextView;
  private TextView hourTextView;
  private TextView minuteTextView;

  private static int eventID = 0;

  private String number;
  private long threadId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.schedule_activity);

    if (getIntent() != null) {
      this.threadId      = getIntent().getLongExtra(THREAD_ID_EXTRA, -1);
      this.number        = getIntent().getStringExtra(NUMBER_EXTRA);
    }

    yearTextView         = findViewById(R.id.year_tv);
    monthTextView        = findViewById(R.id.month_tv);
    dayTextView          = findViewById(R.id.day_tv);
    hourTextView         = findViewById(R.id.hour_tv);
    minuteTextView       = findViewById(R.id.minute_tv);

    smsEditText          = findViewById(R.id.edit_text_message);
    Button timeSelectBtn = findViewById(R.id.time_select_button);
    Button setBtn        = findViewById(R.id.set_schedule_button);
    Button cancelBtn     = findViewById(R.id.cancel_schedule_button);

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

        String message        = smsEditText.getText().toString();
        String hourTv         = hourTextView.getText().toString();
        String minuteTv       = minuteTextView.getText().toString();
        String yearTv         = yearTextView.getText().toString();
        String monthTv        = monthTextView.getText().toString();
        String dayTv          = dayTextView.getText().toString();
        String[] stringValues = {message, hourTv, minuteTv, yearTv, monthTv, dayTv};

        // cannot schedule a message if one of these are empty
        for (String aTimeDateOrMessage : stringValues) {
          if (aTimeDateOrMessage.isEmpty()) {
            Toast.makeText(context, "One of the required fields is empty. Please try again.",
                    Toast.LENGTH_SHORT).show();
            return;
          }
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourTv));
        calendar.set(Calendar.MINUTE, Integer.parseInt(minuteTv));
        calendar.set(Calendar.YEAR, Integer.parseInt(yearTv));
        calendar.set(Calendar.MONTH, Integer.parseInt(monthTv) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dayTv));

        // pass the thread id, the recipient, and the message to the receiver class
        Intent alarmIntent = new Intent(ScheduleActivity.this, CustomAlarmReceiver.class);
        alarmIntent.putExtra(CustomAlarmReceiver.THREAD_ID_EXTRA, threadId);
        alarmIntent.putExtra(CustomAlarmReceiver.NUMBER_EXTRA, number);
        alarmIntent.putExtra(CustomAlarmReceiver.MESSAGE_EXTRA, message);

        PendingIntent pendingAlarm = PendingIntent.getBroadcast(ScheduleActivity.this,
                eventID, alarmIntent, 0);
        eventID += 1;

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingAlarm);

        Toast.makeText(context, "Message scheduled!", Toast.LENGTH_SHORT).show();

      } catch(Exception e){
        Toast.makeText(context, "Invalid, Try again.", Toast.LENGTH_SHORT).show();
      }
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

}
