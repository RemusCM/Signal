package org.thoughtcrime.securesms;

import android.content.Context;

import org.junit.Ignore;
import org.junit.runner.RunWith;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.thoughtcrime.securesms.database.Address;
import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.MessageTest;
import org.thoughtcrime.securesms.database.SmsDatabase;
import org.thoughtcrime.securesms.database.ThreadDatabase;
import org.thoughtcrime.securesms.recipients.Recipient;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Ignore
@RunWith(PowerMockRunner.class)
@PrepareForTest({MessageTest.class})
public class MessageMocking extends BaseUnitTest {

  protected Context contextMock;
  protected DatabaseFactory dbFactoryMock;
  protected SmsDatabase smsDbMock;
  protected ThreadDatabase threadDbMock;
  protected Recipient recipientMock;
  protected Address addressMock;
  protected ArrayList<Integer> messageIds;

  @Override
  public void setUp() {
    contextMock = mock(Context.class);
    dbFactoryMock = mock(DatabaseFactory.class);
    recipientMock = mock(Recipient.class);
    smsDbMock = mock(SmsDatabase.class);
    threadDbMock = mock(ThreadDatabase.class);
    addressMock = mock(Address.class);
  }

  protected void setUpMessageIds(){
    messageIds = new ArrayList<>();
    messageIds.add(111);
    messageIds.add(222);
    messageIds.add(333);

    when(smsDbMock.getMessageIdsByRecipientId(addressMock)).thenReturn(messageIds);
  }

  protected void setUpMessageCount(){
    int c = smsDbMock.getMessageIdsByRecipientId(addressMock).size();
    when(threadDbMock.getMessageCountByRecipientId(addressMock)).thenReturn(c);
  }

}