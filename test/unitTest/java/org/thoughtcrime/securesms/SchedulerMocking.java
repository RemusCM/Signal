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


}
