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

        super.setUpMessageCount();
    }

    @Test
    public void testGetMessageCountByRecipientId(){
        System.out.println("Testing Message Count #1; Expected: 10, Actual: " + threadDb.getMessageCountByRecipientId(address));
        assertEquals(10, threadDb.getMessageCountByRecipientId(address));
        System.out.println("All messages cleared.");
        super.tryClearAllMessages();
        System.out.println("Testing Message Count #2; Expected: 0, Actual: " + threadDb.getMessageCountByRecipientId(address));
        assertEquals(10, threadDb.getMessageCountByRecipientId(address));
    }

}