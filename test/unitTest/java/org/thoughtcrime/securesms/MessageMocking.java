package org.thoughtcrime.securesms;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import org.junit.runner.RunWith;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.thoughtcrime.securesms.database.Address;
import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.SmsDatabase;
import org.thoughtcrime.securesms.database.ThreadDatabase;
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
  protected Cursor c1, c2, c3, crsr;

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

  protected void setUpMessagePositions(){
    String q1 = "query1", q2 = "query2", q3 = "query3";
    int maxPosition = 100;

    when(smu.findMessagePosition(c1, q1, maxPosition)).thenReturn(1);
    when(smu.findMessagePosition(c2, q2, maxPosition)).thenReturn(2);
    when(smu.findMessagePosition(c3, q3, maxPosition)).thenReturn(3);
  }

  protected class MockSearchMessageUtil{
    private ConversationAdapter conversationAdapter = mock(ConversationAdapter.class);
    private RecyclerView recyclerView = mock(RecyclerView.class);
    private int messagePosition = 45;

    public MockSearchMessageUtil(){}

    public int getMessagePosition(){
      return messagePosition;
    }

    public void search(String query){

      int maxPosition = 500;

      if (conversationAdapter != null) {
        new AsyncTask<String, Void, Integer>() {
          @Override
          protected Integer doInBackground(String... query) {
            return getMessagePosition();
          }

          @Override
          protected void onPostExecute(Integer matchingPosition) {
            if (matchingPosition >= 0) {
              recyclerView.scrollToPosition(matchingPosition);
            }

            if (matchingPosition >= 500) {
              recyclerView.scrollToPosition(maxPosition);
            }
          }
        }.execute(query);
      }
    }
  }

}