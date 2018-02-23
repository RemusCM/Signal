package org.thoughtcrime.securesms;

import org.junit.runner.RunWith;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.thoughtcrime.securesms.database.Address;
import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.SmsDatabase;
import org.thoughtcrime.securesms.database.ThreadDatabase;
import org.thoughtcrime.securesms.recipients.Recipient;

import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DatabaseFactory.class})
public class MessageMocking extends BaseUnitTest{

    protected DatabaseFactory dbFactory;
    protected SmsDatabase smsDb;
    protected ThreadDatabase threadDb;
    protected Recipient recipient;
    protected Address address;

    protected void tryClearAllMessages(){
        smsDb.deleteAllMessagesByRecipientId(address);
    }

    protected void setUpMessageCount(){
        when(threadDb.getMessageCountByRecipientId(address)).thenReturn(10);
    }

}