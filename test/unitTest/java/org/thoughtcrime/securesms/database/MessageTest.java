package org.thoughtcrime.securesms.database;

import org.junit.runners.JUnit4;
import org.junit.runner.RunWith;
import org.junit.Test;

import org.thoughtcrime.securesms.MessageMocking;
import org.thoughtcrime.securesms.recipients.Recipient;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(JUnit4.class)
public class MessageTest extends MessageMocking {

  @Override
  public void setUp(){
    dbFactory = mock(DatabaseFactory.class);
    recipient = mock(Recipient.class);
    smsDb = mock(SmsDatabase.class);
    threadDb = mock(ThreadDatabase.class);
    address = mock(Address.class);

    super.setUpMessageIds();
    super.setUpMessageCount();
  }

  @Test
  public void testGetMessageIdsByRecipientId(){
    System.out.println("- Testing Get Message By Recipient Id -");
    verify(smsDb).getMessageIdsByRecipientId(address);
    System.out.println("    Expected: " + messageIds.toString());
    System.out.println("    Actual: " + smsDb.getMessageIdsByRecipientId(address).toString());
    assertEquals(messageIds.toString(), smsDb.getMessageIdsByRecipientId(address).toString());
  }

  @Test
  public void testGetMessageCountByRecipientId(){
    System.out.println("- Testing Message Count -");
    System.out.println("    Expected: " + messageIds.size());
    System.out.println("    Actual: " + threadDb.getMessageCountByRecipientId(address));
    assertEquals(messageIds.size(), threadDb.getMessageCountByRecipientId(address));
  }

  @Test
  public void testDeleteMessage(){
    System.out.println("- Testing Delete Message -");
    System.out.println("    Size before: " + threadDb.getMessageCountByRecipientId(address));
    System.out.println("    Clearing messages and testing message count again...");
    smsDb.deleteAllMessagesByRecipientId(address);
    System.out.println("    Size after: " + smsDb.getMessageCountForMessageId(address));
    assertEquals(0, smsDb.getMessageCountForMessageId(address));
  }

}