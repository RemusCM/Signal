package org.thoughtcrime.securesms;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.thoughtcrime.securesms.scheduler.SendMessageTest;

import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@Ignore
@RunWith(PowerMockRunner.class)
@PrepareForTest({SendMessageTest.class})
public class SendMessageMocking extends BaseUnitTest {


  protected SendSMSMessage sendSMSMessageMock;
  protected SendSMSMessage sendSMSMessageMock2;

  @Override
  public void setUp() {
    sendSMSMessageMock = mock(SendSMSMessage.class);
    sendSMSMessageMock2 = mock(SendSMSMessage.class);
  }

  protected void setUpMessageToSend() {
    sendSMSMessageMock.setMessage("Hello!");
    when(sendSMSMessageMock.getMessage()).thenReturn("Hello!");
  }

  protected void setUpThreadId() {
    sendSMSMessageMock.setThreadId(1);
    when(sendSMSMessageMock.getThreadId()).thenReturn((long) 1);
  }

  protected void setUpIsMessageSent() {
    sendSMSMessageMock.sendSMSMessage();
    when(sendSMSMessageMock.isMessageSent()).thenReturn(true);
  }

  protected void setUpSendSMSMessageFull() {
    String message = "Hello there!";
    long threadId = 2;
    when(sendSMSMessageMock2.getMessage()).thenReturn(message);
    when(sendSMSMessageMock2.getThreadId()).thenReturn(threadId);

    sendSMSMessageMock2.sendSMSMessage();
    when(sendSMSMessageMock2.isMessageSent()).thenReturn(true);
  }

}
