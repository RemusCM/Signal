package org.thoughtcrime.securesms.passcode;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.thoughtcrime.securesms.PasscodeDBhandlerMocking;

import static junit.framework.Assert.assertEquals;

@RunWith(JUnit4.class)
public class PasscodeDBHandlerTest extends PasscodeDBhandlerMocking{


    @Override
    public void setUp() throws Exception{
        super.setUp();
        super.setUpStaticPasscodeDatabase();
        super.setUpPasscodeExistence();
    }


    @Test
    public void testIsPasscodeExists(){
        System.out.println("\n- Testing isPasscodeExists : Outcome #1 -");
        System.out.println("    Expected: True");
        System.out.println("    Actual: True");
        assertEquals(true, passcodeDBHMock.isPasscodeExists());
    }
/*

    @Test
    public void testUpdate(){

    }

    @Test
    public void testDelete(){

    }
*/
    @Test
    public void testGetPasscodeIfExists(){
        System.out.println("\n- Testing getPasscodeIfExists : Outcome #1 -");
        System.out.println("    Expected: 2018");
        System.out.println("    Actual: " + passcodeDBHMock.getPasscodeIfExists());
        assertEquals("2018", passcodeDBHMock.getPasscodeIfExists());
    }


}
