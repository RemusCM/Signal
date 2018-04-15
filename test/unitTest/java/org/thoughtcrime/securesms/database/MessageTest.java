package org.thoughtcrime.securesms.database;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.thoughtcrime.securesms.MessageMocking;
import org.thoughtcrime.securesms.recipients.Recipient;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;
import static org.mockito.Mockito.verify;

@RunWith(JUnit4.class)
public class MessageTest extends MessageMocking {

  @Override
  public void setUp(){
    super.setUp();
    setUpMessageIds();
    setUpMessageCount();
  }

  @Test
  public void testGetMessageIdsByRecipientId(){
    System.out.println("- Testing Get Message By Recipient Id : Outcome #1 -");
    verify(smsDbMock).getMessageIdsByRecipientId(addressMock);
    System.out.println("    Expected: " + messageIds.toString());
    System.out.println("    Actual: " + smsDbMock.getMessageIdsByRecipientId(addressMock).toString());
    assertEquals(messageIds.toString(), smsDbMock.getMessageIdsByRecipientId(addressMock).toString());
  }

  @Test
  public void testGetMessageCountByRecipientId(){
    System.out.println("- Testing Message Count : Outcome #1 -");
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

  /* Fail Case */
  @Test
  public void testGetMessageCountByRecipientIdFail1(){
    System.out.println("- FAIL case: testGetMessageCountByRecipientIdFail1 : Outcome #2 -");
    System.out.println("    Expected: 2");
    System.out.println("    Actual: " + threadDbMock.getMessageCountByRecipientId(addressMock));
    assertNotSame(2, threadDbMock.getMessageCountByRecipientId(addressMock));
  }

  /* Fail Case */
  @Test
  public void testGetMessageCountByRecipientIdFail2(){
    System.out.println("- FAIL case: testGetMessageCountByRecipientIdFail2: Outcome #3 -");
    System.out.println("    Expected: 5");
    System.out.println("    Actual: " + threadDbMock.getMessageCountByRecipientId(addressMock));
    assertNotSame(5, threadDbMock.getMessageCountByRecipientId(addressMock));
  }

  /* Fail Case */
  @Test
  public void testGetMessageCountByRecipientIdFail3(){
    System.out.println("- FAIL case: testGetMessageCountByRecipientIdFail3 : Outcome #4 -");
    System.out.println("    Expected: 10");
    System.out.println("    Actual: " + threadDbMock.getMessageCountByRecipientId(addressMock));
    assertNotSame(10, threadDbMock.getMessageCountByRecipientId(addressMock));
  }

  /* Fail Case */
  @Test
  public void testGetMessageIdsByRecipientIdFail1(){
    System.out.println("- FAIL case: testGetMessageIdsByRecipientIdFail1 : Outcome #2 -");
    verify(smsDbMock).getMessageIdsByRecipientId(addressMock);
    System.out.println("    Expected: [2018, 2019, 2020]" );
    System.out.println("    Actual: " + smsDbMock.getMessageIdsByRecipientId(addressMock).toString());
    assertNotSame("[2018, 2019, 2020]", smsDbMock.getMessageIdsByRecipientId(addressMock).toString());
  }

}