package org.thoughtcrime.securesms;

import org.junit.Ignore;
import org.junit.runner.RunWith;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.RecipientDatabase;
import org.thoughtcrime.securesms.recipients.Recipient;

import static org.mockito.Mockito.when;

@Ignore
@RunWith(PowerMockRunner.class)
@PrepareForTest({DatabaseFactory.class})
public class NicknameMocking extends BaseUnitTest{

    protected DatabaseFactory dbFactory;
    protected Recipient recipient;
    protected RecipientDatabase recipientDb;

    protected void setUpDisplayName() {
        when(recipientDb.getDisplayName()).thenReturn("Dennis");
    }

    protected void setUpCustomLabel() {
        when(recipientDb.getCustomLabel()).thenReturn("CEO");
    }

}