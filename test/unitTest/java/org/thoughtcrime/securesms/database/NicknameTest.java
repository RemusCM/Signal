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
        System.out.println("Testing DisplayName; Expected: Dennis, Actual: " + recipientDb.getDisplayName());
        assertEquals("Dennis", recipientDb.getDisplayName());
    }

    @Test
    public void testGetCustomLabel() {
        System.out.println("Testing CustomLabel; Expected: CEO, Actual: " + recipientDb.getCustomLabel());
        assertEquals("CEO", recipientDb.getCustomLabel());
    }

}