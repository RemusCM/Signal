package org.thoughtcrime.securesms.search;

import android.database.Cursor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.thoughtcrime.securesms.MessageMocking;
import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.util.SearchMessageUtil;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(JUnit4.class)
@PrepareForTest({DatabaseFactory.class})
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
        assertEquals(1, smu.findMessagePosition(c1, "query1", 100));
    }
}
