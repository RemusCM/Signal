package org.thoughtcrime.securesms.database;

import android.content.Context;

import org.junit.runners.JUnit4;
import org.junit.runner.RunWith;
import org.junit.Test;

import org.mockito.ArgumentCaptor;
import org.thoughtcrime.securesms.MessageMocking;
import org.thoughtcrime.securesms.recipients.Recipient;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(JUnit4.class)
public class MessageTest extends MessageMocking {

  @Override
  public void setUp(){
    contextMock = mock(Context.class);
    dbFactoryMock = mock(DatabaseFactory.class);
    recipientMock = mock(Recipient.class);
    smsDbMock = mock(SmsDatabase.class);
    threadDbMock = mock(ThreadDatabase.class);
    addressMock = mock(Address.class);

    super.setUpMessageIds();
    super.setUpMessageCount();
  }

  @Test
  public void testGetMessageIdsByRecipientId(){
    System.out.println("- Testing Get Message By Recipient Id -");
    verify(smsDbMock).getMessageIdsByRecipientId(addressMock);
    System.out.println("    Expected: " + messageIds.toString());
    System.out.println("    Actual: " + smsDbMock.getMessageIdsByRecipientId(addressMock).toString());
    assertEquals(messageIds.toString(), smsDbMock.getMessageIdsByRecipientId(addressMock).toString());
  }

  @Test
  public void testGetMessageCountByRecipientId(){
    System.out.println("- Testing Message Count -");
    System.out.println("    Expected: " + messageIds.size());
    System.out.println("    Actual: " + threadDbMock.getMessageCountByRecipientId(addressMock));
    assertEquals(messageIds.size(), threadDbMock.getMessageCountByRecipientId(addressMock));
  }

  @Test
  public void testDeleteMessage(){
    System.out.println("- Testing Delete Message -");
    System.out.println("    Size before: " + threadDbMock.getMessageCountByRecipientId(addressMock));
    System.out.println("    Clearing messages and testing message count again...");
    smsDbMock.deleteAllMessagesByRecipientId(addressMock);
    System.out.println("    Size after: " + smsDbMock.getMessageCountForMessageId(addressMock));
    assertEquals(0, smsDbMock.getMessageCountForMessageId(addressMock));
  }

  @Test
  public void testGetRecipientForThreadId() {
    ArgumentCaptor<Long> argCaptor = ArgumentCaptor.forClass(Long.class);
    long threadId = 1;
    threadDbMock.getRecipientForThreadId(threadId);
    verify(threadDbMock).getRecipientForThreadId(argCaptor.capture());
    assertEquals(String.valueOf(threadId), String.valueOf(argCaptor.getValue()));
  }

  @Test
  public void testGetThreadDatabase() {
    ArgumentCaptor<Recipient> argCaptor = ArgumentCaptor.forClass(Recipient.class);
    threadDbMock.getThreadIdFor(recipientMock);
    verify(threadDbMock).getThreadIdFor(argCaptor.capture());
    System.out.println(argCaptor.getValue());
    assertEquals(recipientMock, argCaptor.getValue());
  }

}