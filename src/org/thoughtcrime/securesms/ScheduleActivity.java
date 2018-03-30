package org.thoughtcrime.securesms;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import org.thoughtcrime.securesms.service.ScheduleService;

import java.util.Calendar;

public class ScheduleActivity extends Activity{
    public String sPhone,sSms;
    private EditText etPhone,etSms;

    private Button bStart,bCancel,bTimeSelect,bPhone;

    static final int TIME_DIALOG_ID=1;
    static final int DATE_DIALOG_ID=2;
    private static final int REQUEST_CODE = 1;

    Calendar c;
    public int yearPicked ,monthPicked ,day,hour,minute;
    private int mHour,mMinute, mYear, mMonth,mDay;


    private AlarmManager aManager;
    private PendingIntent pIntent;

    public ScheduleActivity(){
        // Assign current Date and Time Values to Variables
        c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_activity);

        etPhone = (EditText)findViewById(R.id.edit_text_phone);
        etSms = (EditText)findViewById(R.id.edit_text_message);

        bStart = (Button)findViewById(R.id.set_schedule_button);
        bCancel = (Button)findViewById(R.id.cancel_schedule_button);
        bTimeSelect = (Button)findViewById(R.id.time_select_button);
        bPhone = (Button)findViewById(R.id.contact);

        //contact
        bPhone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Uri uri = Uri.parse("content://contacts");
                Intent intent = new Intent(Intent.ACTION_PICK, uri);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        //start schedule
        bStart.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                Toast.makeText(ScheduleActivity.this, "Sms scheduled! " ,Toast.LENGTH_SHORT).show();
                sPhone = etPhone.getText().toString();
                sSms = etSms.getText().toString();
                etSms.getText().clear();

                Intent i = new Intent(ScheduleActivity.this,ScheduleService.class);
                i.putExtra("exPhone", sPhone);
                i.putExtra("exSmS", sSms);


                pIntent = PendingIntent.getService(getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

                aManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                c.setTimeInMillis(System.currentTimeMillis());
                c.set(Calendar.HOUR_OF_DAY, hour);
                c.set(Calendar.MINUTE, minute);
                c.set(Calendar.YEAR, yearPicked);
                c.set(Calendar.MONTH, monthPicked);
                c.set(Calendar.DAY_OF_MONTH, day);

                

                aManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pIntent);

                //ScheduleActivity.super.onBackPressed();

            }
        });

        //set time to send
        bTimeSelect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
                showDialog(DATE_DIALOG_ID);
            }

        });

        //Cancel schedule
        bCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               ScheduleActivity.super.onBackPressed();
            }
        });
    }

    //Choose phone in contact and set edit text
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent i) {
        super.onActivityResult(requestCode, resultCode, i);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = i.getData();
                String[] projection = { ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME };

                Cursor cursor = getContentResolver().query(uri, projection,
                        null, null, null);
                cursor.moveToFirst();

                int numberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(numberColumnIndex);

                etPhone.setText(number);
            }
        }
    }
    // Register  TimePickerDialog listener
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int min) {
                    hour = hourOfDay;
                    minute = min;
                    // Set the Selected Date in Select date Button
                    if(minute < 10){
                        bTimeSelect.setText(hour+ ":0" + minute + ", " + yearPicked + "/" + (monthPicked+1) + "/" + day);
                    }
                    else
                    bTimeSelect.setText(hour+":"+minute + ", " + yearPicked + "/" + (monthPicked+1) + "/" + day);
                }
            };

    //Register DatePickerDialog listerner
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    yearPicked = year;
                    monthPicked = month;
                    day = dayOfMonth;

                }
            };

    // Method automatically gets Called when you call showDialog()  method
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            // create a new TimePickerDialog with values you want to show
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, false);
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);

        }
        return null;
    }


}
