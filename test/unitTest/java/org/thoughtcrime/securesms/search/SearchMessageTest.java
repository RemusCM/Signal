package org.thoughtcrime.securesms.search;

import android.database.Cursor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.thoughtcrime.securesms.MessageMocking;
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
    public void testSearch(){
        MockSearchMessageUtil msmu = new MockSearchMessageUtil();
        crsr = mock(Cursor.class);
        String query = "hello world";

        System.out.println("- Testing Message Search : Outcome #1 -");
        msmu.search(query);
        System.out.println("    Expected: 45" );
        System.out.println("    Actual: " + msmu.getMessagePosition());
        assertEquals(45, msmu.getMessagePosition());
    }

}
