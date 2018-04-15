package org.thoughtcrime.securesms.scheduler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.thoughtcrime.securesms.SendMessageMocking;
import org.thoughtcrime.securesms.database.DatabaseFactory;

import static junit.framework.Assert.assertEquals;

@RunWith(JUnit4.class)
@PrepareForTest({DatabaseFactory.class})
public class SendMessageTest extends SendMessageMocking {

  @Override
  public void setUp() {
    super.setUp();

    setUpMessageToSend();
    setUpThreadId();
    setUpIsMessageSent();
    setUpSendSMSMessageFull();
  }

  @Test
  public void testGetMessageToSend() {
    // TODO add meaningful System out statements
    assertEquals("Hello!", sendSMSMessageMock.getMessage());
  }

  @Test
  public void testGetThreadId() {
    // TODO add meaningful System out statements
    assertEquals(1, sendSMSMessageMock.getThreadId());
  }

  @Test
  public void testIsMessageSent() {
    // TODO add meaningful System out statements
    assertEquals(true, sendSMSMessageMock.isMessageSent());
  }

  @Test
  public void testSendSMSMessageFull() {
    // TODO add meaningful System out statements
    assertEquals("Hello there!", sendSMSMessageMock2.getMessage());
    assertEquals(2, sendSMSMessageMock2.getThreadId());
    assertEquals(true, sendSMSMessageMock2.isMessageSent());
  }

}
