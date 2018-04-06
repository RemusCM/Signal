package org.thoughtcrime.securesms;

import android.content.Context;
import android.database.Cursor;

import org.junit.runner.RunWith;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.thoughtcrime.securesms.database.Address;
import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.SmsDatabase;
import org.thoughtcrime.securesms.database.ThreadDatabase;
import org.thoughtcrime.securesms.database.model.DisplayRecord;
import org.thoughtcrime.securesms.database.model.MessageRecord;
import org.thoughtcrime.securesms.recipients.Recipient;
import org.thoughtcrime.securesms.util.SearchMessageUtil;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DatabaseFactory.class})
public class MessageMocking extends BaseUnitTest {

  protected Context contextMock;
  protected DatabaseFactory dbFactoryMock;
  protected SmsDatabase smsDbMock;
  protected ThreadDatabase threadDbMock;
  protected Recipient recipientMock;
  protected Address addressMock;
  protected ArrayList<Integer> messageIds;

  protected SearchMessageUtil smu;
  protected MessageRecord messageRecord1;
  protected MessageRecord messageRecord2;
  protected MessageRecord messageRecord3;
  protected Cursor c1, c2, c3;

  protected void setUpMessageIds(){
    messageIds = new ArrayList<>();
    messageIds.add(111);
    messageIds.add(222);
    messageIds.add(333);

    when(smsDbMock.getMessageIdsByRecipientId(addressMock)).thenReturn(messageIds);
  }

  protected void setUpMessageCount(){
    int c = smsDbMock.getMessageIdsByRecipientId(addressMock).size();
    when(threadDbMock.getMessageCountByRecipientId(addressMock)).thenReturn(c);
  }

  protected void setSearchingMsg(){
    when(smu.getSearchingMsg()).thenReturn(0x7f0f0250);
  }

  protected void setUpMessagePositions(){

    String q1 = "query1", q2 = "query2", q3 = "query3";
    int maxPosition = 100;

    when(smu.findMessagePosition(c1, q1, maxPosition)).thenReturn(1);
    when(smu.findMessagePosition(c2, q2, maxPosition)).thenReturn(2);
    when(smu.findMessagePosition(c3, q3, maxPosition)).thenReturn(3);
  }

}