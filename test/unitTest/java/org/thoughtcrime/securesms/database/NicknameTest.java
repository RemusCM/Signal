package org.thoughtcrime.securesms.database;

import org.junit.runners.JUnit4;
import org.junit.runner.RunWith;
import org.junit.Test;

import org.thoughtcrime.securesms.NicknameMocking;
import org.thoughtcrime.securesms.recipients.Recipient;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(JUnit4.class)
public class NicknameTest extends NicknameMocking {

    @Override
    public void setUp() {
        dbFactory = mock(DatabaseFactory.class);
        recipient = mock(Recipient.class);
        recipientDb = mock(RecipientDatabase.class);

        super.setUpDisplayName();
        super.setUpCustomLabel();
    }

    @Test
    public void testGetDisplayName() {
        assertEquals(recipientDb.getDisplayName(), "Dennis");
    }

    @Test
    public void testGetCustomLabel() {
        assertEquals(recipientDb.getCustomLabel(), "CEO");
    }

}