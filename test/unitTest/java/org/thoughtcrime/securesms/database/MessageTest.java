package org.thoughtcrime.securesms.database;

import org.junit.runners.JUnit4;
import org.junit.runner.RunWith;
import org.junit.Test;

import org.thoughtcrime.securesms.MessageMocking;
import org.thoughtcrime.securesms.recipients.Recipient;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(JUnit4.class)
public class MessageTest extends MessageMocking {

    @Override
    public void setUp() {
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
        System.out.println("Testing Message IDs; Expected: [111,222,333], Actual: " + smsDb.getMessageIdsByRecipientId(address).toString());
        assertEquals("[111, 222, 333]", smsDb.getMessageIdsByRecipientId(address).toString());
    }

    @Test
    public void testGetMessageCountByRecipientId(){
        System.out.println("Testing Message Count #1; Expected: 3, Actual: " + threadDb.getMessageCountByRecipientId(address));
        assertEquals(3, threadDb.getMessageCountByRecipientId(address));
    }

    @Test
    public void testDeleteAllMessagesByRecipientId(){
        System.out.println("Clearing messages and testing message count again...");
        smsDb.deleteAllMessagesByRecipientId(address);
        System.out.println("Testing Message Count #2; Expected: 0, Actual: " + threadDb.getMessageCountByRecipientId(address));
        assertEquals(0, threadDb.getMessageCountByRecipientId(address));
    }

}