package org.thoughtcrime.securesms;

import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by daanish on 4/15/2018.
 */

public class SchedulerMocking extends BaseUnitTest {
  public SendSMSMessage mockSendSms;
  public SendSMSMessage mockSendSms2;

  @Override
  public void setUp() throws Exception{
    super.setUp();
  }

  public void setUpThreadId(){
    long threadId = 1;
    mockSendSms.setThreadId(threadId);
    when(mockSendSms.getThreadId()).thenReturn(threadId);
  }

  public void setUpMessage(){
    String message = "Hello, this is your message";
    mockSendSms.setMessage(message);
    when(mockSendSms.getMessage()).thenReturn(message);
  }

  public void setUpIsMessageSent(){
    boolean isSent = false;
    mockSendSms.sendSMSMessage();
    isSent = true;
    when(mockSendSms.isMessageSent()).thenReturn(isSent);
  }

  public void setUpSendSMSMessage(){
    String message = "Hello, this is your message";
    long threadId = 123;
    boolean isSent = false;

    mockSendSms2.setMessage(message);
    mockSendSms2.setThreadId(threadId);
    mockSendSms2.sendSMSMessage();
    isSent = true;

    when(mockSendSms2.getMessage()).thenReturn(message);
    when(mockSendSms2.getThreadId()).thenReturn(threadId);
    when(mockSendSms2.isMessageSent()).thenReturn(isSent);

  }
}
