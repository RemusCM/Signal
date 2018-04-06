package org.thoughtcrime.securesms.search;

import android.database.Cursor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.thoughtcrime.securesms.MessageMocking;
import org.thoughtcrime.securesms.R;
import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.util.SearchMessageUtil;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(JUnit4.class)
public class SearchMessageTest extends MessageMocking {

    @Override
    public void setUp() {
        smu = mock(SearchMessageUtil.class);
        c1 = mock(Cursor.class);
        c2 = mock(Cursor.class);
        c3 = mock(Cursor.class);

        setUpMessagePositions();
        setSearchingMsg();
    }

    @Test
    public void testMessagePositions(){
        System.out.println("- Testing Message Position : Outcome #1 -");
        System.out.println("    Expected: 1" );
        System.out.println("    Actual: " + smu.findMessagePosition(c1, "query1", 100));
        assertEquals(1, smu.findMessagePosition(c1, "query1", 100));

        System.out.println("- Testing Message Position : Outcome #2 -");
        System.out.println("    Expected: 2" );
        System.out.println("    Actual: " + smu.findMessagePosition(c2, "query2", 100));
        assertEquals(2, smu.findMessagePosition(c2, "query2", 100));

        System.out.println("- Testing Message Position : Outcome #3 -");
        System.out.println("    Expected: 3" );
        System.out.println("    Actual: " + smu.findMessagePosition(c3, "query3", 100));
        assertEquals(3, smu.findMessagePosition(c3, "query3", 100));
    }

    @Test
    public void testGetSearchingMsg(){
        System.out.println("- Testing getSearchingMsg : Outcome #1 -");
        System.out.println("    Expected: 0x7f0f0250" );
        System.out.println("    Actual: " + R.string.conversationFragment_searching);
        assertEquals(0x7f0f0250, R.string.conversationFragment_searching);
    }


}
