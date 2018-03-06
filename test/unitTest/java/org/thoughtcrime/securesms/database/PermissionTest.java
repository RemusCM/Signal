package org.thoughtcrime.securesms.database;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.thoughtcrime.securesms.PermissionMocking;
import org.thoughtcrime.securesms.util.Util;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(JUnit4.class)
public class PermissionTest extends PermissionMocking {

    @Override
    public void setUp(){
        addressMock = mock(Address.class);
        permissionDbMock = mock(PermissionDatabase.class);
        privileges = new String[2];

        //setUpAddressList();
        //setUpPermission();
        setUpPrivileges();
    }

    @Test
    public void testJoinStringPrivileges(){

        String[] arr = {privileges[0], privileges[1]};
        System.out.println("- Testing joinStringPrivileges : Outcome #1 -");
        System.out.println("    Expected: 64,32" );
        System.out.println("    Actual: " + Util.joinStringPrivileges(arr));
        assertEquals("64,32", Util.joinStringPrivileges(arr));
    }
}
