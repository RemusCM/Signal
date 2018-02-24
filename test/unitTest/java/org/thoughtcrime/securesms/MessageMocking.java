package org.thoughtcrime.securesms;

import org.junit.runner.RunWith;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.thoughtcrime.securesms.database.Address;
import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.SmsDatabase;
import org.thoughtcrime.securesms.database.ThreadDatabase;
import org.thoughtcrime.securesms.recipients.Recipient;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DatabaseFactory.class})
public class MessageMocking extends BaseUnitTest {

  protected DatabaseFactory dbFactory;
  protected SmsDatabase smsDb;
  protected ThreadDatabase threadDb;
  protected Recipient recipient;
  protected Address address;
  protected ArrayList<Integer> messageIds;
  protected int c;

  protected void setUpMessageIds(){
    messageIds = new ArrayList<>();
    messageIds.add(111);
    messageIds.add(222);
    messageIds.add(333);

    when(smsDb.getMessageIdsByRecipientId(address)).thenReturn(messageIds);
  }

  protected void setUpMessageCount(){
    c = smsDb.getMessageIdsByRecipientId(address).size();
    when(threadDb.getMessageCountByRecipientId(address)).thenReturn(c);
  }

}