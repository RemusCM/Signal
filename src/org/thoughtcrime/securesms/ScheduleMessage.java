package org.thoughtcrime.securesms;

import org.thoughtcrime.securesms.database.Address;

/**
 * This class is used for passing scheduler data between activities.
 * So, instead of creating multiple variables you can
 * use this object to pass to a different activity.
 * See ScheduleActivity and CustomAlarmReceiver
 * Using object is cleaner approach.
 */
public class ScheduleMessage {

  private Address address;
  private long threadId;
  private String message;

  ScheduleMessage() {

  }

  public ScheduleMessage(Address address, long threadId, String message) {
    this.address = address;
    this.threadId = threadId;
    this.message = message;
  }


  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public long getThreadId() {
    return threadId;
  }

  public void setThreadId(long threadId) {
    this.threadId = threadId;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

}
