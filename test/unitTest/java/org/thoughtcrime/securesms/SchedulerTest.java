package org.thoughtcrime.securesms;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Created by daanish on 4/15/2018.
 */

public class SchedulerTest extends SchedulerMocking {

  @Override
  public void setUp(){
    mockSendSms = mock(SendSMSMessage.class);
    mockSendSms2 = mock(SendSMSMessage.class);
    setUpThreadId();
  }

  @Test
  public void testGetThreadId(){

    long actualThreadId = mockSendSms.getThreadId();
    System.out.println("testGetThreadId()");
    System.out.println("Expected: " + 1);
    System.out.println("Actual: " + actualThreadId);
    assertEquals(1, actualThreadId);
  }

}
