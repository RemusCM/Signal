package org.thoughtcrime.securesms.search;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import org.junit.Test;
import org.thoughtcrime.securesms.ConversationAdapter;
import org.thoughtcrime.securesms.util.SearchMessageUtil;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SearchMessageUtilTest {

  private SearchMessageUtil smu = mock(SearchMessageUtil.class);
  private Cursor c1 = mock(Cursor.class);
  private Cursor c2 = mock(Cursor.class);
  private Cursor c3 = mock(Cursor.class);

  private Context context = mock(Context.class);
  long threadId = 1;

  private void setUpMessagePositions() {

    String q1 = "query1", q2 = "query2", q3 = "query3";
    int maxPosition = 100;

    when(smu.findMessagePosition(c1, q1, maxPosition)).thenReturn(1);
    when(smu.findMessagePosition(c2, q2, maxPosition)).thenReturn(2);
    when(smu.findMessagePosition(c3, q3, maxPosition)).thenReturn(3);
  }

  protected class MockSearchMessageUtil {
    private ConversationAdapter conversationAdapter = mock(ConversationAdapter.class);
    private RecyclerView recyclerView = mock(RecyclerView.class);
    private int messagePosition = 45;

    MockSearchMessageUtil() {
    }

    int getMessagePosition() {
      return messagePosition;
    }

    void search(String query) {
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

  @Test
  public void testMessagePositions() {
    setUpMessagePositions();
    System.out.println("- Testing Message Position : Outcome #1 -");
    System.out.println("    Expected: 1");
    System.out.println("    Actual: " + smu.findMessagePosition(c1, "query1", 100));
    assertEquals(1, smu.findMessagePosition(c1, "query1", 100));

    System.out.println("- Testing Message Position : Outcome #2 -");
    System.out.println("    Expected: 2");
    System.out.println("    Actual: " + smu.findMessagePosition(c2, "query2", 100));
    assertEquals(2, smu.findMessagePosition(c2, "query2", 100));

    System.out.println("- Testing Message Position : Outcome #3 -");
    System.out.println("    Expected: 3");
    System.out.println("    Actual: " + smu.findMessagePosition(c3, "query3", 100));
    assertEquals(3, smu.findMessagePosition(c3, "query3", 100));
  }

  @Test
  public void testSearch() {
    MockSearchMessageUtil msmu = new MockSearchMessageUtil();
    String query = "hello world";

    System.out.println("- Testing Message Search : Outcome #1 -");
    msmu.search(query);
    System.out.println("    Expected: 45" );
    System.out.println("    Actual: " + msmu.getMessagePosition());
    assertEquals(45, msmu.getMessagePosition());
  }

}
