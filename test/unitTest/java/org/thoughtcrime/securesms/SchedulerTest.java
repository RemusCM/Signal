package org.thoughtcrime.securesms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.widget.Toast;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.ALARM_SERVICE;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by daanish on 4/15/2018.
 */

public class SchedulerTest extends SchedulerMocking {

  @Override
  public void setUp(){
    mockSendSms = mock(SendSMSMessage.class);
    mockSendSms2 = mock(SendSMSMessage.class);
    setUpThreadId();
    setUpMessage();
    setUpIsMessageSent();
    setUpSendSMSMessage();

  }

  @Test
  public void testGetThreadId(){

    long actualThreadId = mockSendSms.getThreadId();
    System.out.println("testGetThreadId()");
    System.out.println("Expected: " + 1);
    System.out.println("Actual: " + actualThreadId);
    assertEquals(1, actualThreadId);
  }

  @Test
  public void testGetMessage(){
    String actualMessage = mockSendSms.getMessage();
    System.out.println("testGetMessage()");
    System.out.println("Expected: " + "Hello, this is your message");
    System.out.println("Actual: " + actualMessage);
    assertEquals("Hello, this is your message", actualMessage);
  }

  @Test
  public void testIsSentMessage(){
    boolean isSent = mockSendSms.isMessageSent();
    System.out.println("testIsSentMessage()");
    System.out.println("Expected: " + true);
    System.out.println("Actual: " + isSent);
    assertTrue(isSent);
  }

  @Test
  public void testSendSMSMessage(){
    long actualThreadId= mockSendSms2.getThreadId();
    String actualMessage = mockSendSms2.getMessage();
    boolean isSent = mockSendSms2.isMessageSent();

    System.out.println("testSendSMSMessage()");
    System.out.println("Expected: " + 1);
    System.out.println("Actual: " + actualThreadId);
    System.out.println("Expected: " + "Hello, this is your message");
    System.out.println("Actual: " + actualMessage);
    System.out.println("Expected: " + true);
    System.out.println("Actual: " + isSent);

    assertEquals("Hello, this is your message", actualMessage);
    assertEquals(123, actualThreadId);
    assertTrue(isSent);
  }

  @Test
  public void testScheduler(){

    FakeAlarmManager fam= new FakeAlarmManager();
    fam.setScheduledTime();
    CustomAlarmReceiver car= mock(CustomAlarmReceiver.class);
    car.setAlarm(context);


    LocalDateTime localNow = fam.localTime;
    int year = localNow.getYear();
    int month = localNow.getMonthValue();
    int day = localNow.getDayOfMonth();
    int hour = localNow.getHour();
    int minute = localNow.getMinute();
    int second = localNow.getSecond();

    System.out.printf("%d-%02d-%02d %02d:%02d:%02d", year, month, day, hour, minute, second);
    String expected = String.format("%d-%02d-%02d %02d:%02d:%02d",year, month, day, hour, minute, second);

    Calendar now = fam.calendar;
    int cYear = now.get(Calendar.YEAR);
    int cMonth = now.get(Calendar.MONTH); // Note: zero based!
    int cDay = now.get(Calendar.DAY_OF_MONTH);
    int cHour = now.get(Calendar.HOUR_OF_DAY);
    int cMinute = now.get(Calendar.MINUTE) -1;
    int cSecond = now.get(Calendar.SECOND);
    System.out.println();
    System.out.printf("%d-%02d-%02d %02d:%02d:%02d", cYear, cMonth, cDay, cHour, cMinute, cSecond);
    String scheduled = String.format("%d-%02d-%02d %02d:%02d:%02d", cYear, cMonth, cDay, cHour, cMinute, cSecond);
    System.out.println();


    System.out.println(" - Testing Scheduler - ");
    System.out.println("Expected: true" );
    System.out.println("Actual: " + fam.isAlarmSet());
    System.out.println("Current time: " + fam.localTime);
    System.out.println("Scheduled time: " + fam.calendar.getTime());
    System.out.println("Message sent: " + fam.msgSent);
    assertTrue(fam.isAlarmSet());

    assertEquals(expected, scheduled);
  }


  public class FakeAlarmManager{
    private boolean alarm = false;
    private String msgSent = "Hello World.";
    private Calendar calendar = Calendar.getInstance();
    private LocalDateTime localTime = LocalDateTime.now();

    public FakeAlarmManager(){}

    private void setScheduledTime(){

      int eventID=0;
      calendar.setTimeInMillis(System.currentTimeMillis());
      calendar.set(Calendar.HOUR_OF_DAY, localTime.getHour());
      calendar.set(Calendar.MINUTE, localTime.getMinute() + 1);
      calendar.set(Calendar.YEAR, localTime.getYear());
      calendar.set(Calendar.MONTH, localTime.getMonthValue());
      calendar.set(Calendar.DAY_OF_MONTH, localTime.getDayOfMonth());

      // pass the thread id, the recipient, and the message to the receiver class
      Intent alarmIntent = new Intent(context, CustomAlarmReceiver.class);
      alarmIntent.putExtra(CustomAlarmReceiver.THREAD_ID_EXTRA, 1);
      alarmIntent.putExtra(CustomAlarmReceiver.NUMBER_EXTRA, 1112223333);
      alarmIntent.putExtra(CustomAlarmReceiver.MESSAGE_EXTRA, msgSent);

      PendingIntent pendingAlarm = PendingIntent.getBroadcast(context,
              eventID, alarmIntent, 0);

      AlarmManager alarmManager = mock(AlarmManager.class);
      assert alarmManager != null;
      alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingAlarm);
      alarm=true;
    }

    private boolean isAlarmSet() {
      return alarm;
    }
  }
}
