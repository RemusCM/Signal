package org.thoughtcrime.securesms.passcode;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.thoughtcrime.securesms.PasscodeDBhandlerMocking;

import static junit.framework.Assert.assertEquals;

@RunWith(JUnit4.class)
public class PasscodeDBHandlerTest extends PasscodeDBhandlerMocking {


  @Override
  public void setUp() {
    super.setUp();
    setUpPasscodeExistence();
    setUpPasscodeAdd();
    setUpPasscodeUpdate();
    setUpPasscodeDeletion();
  }

  @Test
  public void testIsPasscodeExists() {
    System.out.println("\n- Testing isPasscodeExists : Mock #1 -");
    System.out.println("    Expected: True");
    System.out.println("    Actual: " + passcodeDBHMock1.isPasscodeExists());
    assertEquals(true, passcodeDBHMock1.isPasscodeExists());
  }

  @Test
  public void testGetPasscodeIfExists() {
    System.out.println("\n- Testing getPasscodeIfExists : Mock #1 -");
    System.out.println("    Expected: 2018");
    System.out.println("    Actual: " + passcodeDBHMock1.getPasscodeIfExists());
    assertEquals("2018", passcodeDBHMock1.getPasscodeIfExists());
  }

  @Test
  public void testUpdate() {
    System.out.println("\n- Testing update : Mock #1 -");
    System.out.println("    Expected: Success");
    System.out.println("    Actual: " + passcodeDBHMock1.update());
    assertEquals("Success", passcodeDBHMock1.update());
  }

  @Test
  public void testAdd() {
    System.out.println("\n- Testing add : Mock #2 -");
    System.out.println("    Expected: Success");
    System.out.println("    Actual: " + passcodeDBHMock2.add());
    assertEquals("Success", passcodeDBHMock2.add());
  }

  @Test
  public void testDelete() {
    System.out.println("\n- Testing delete : Mock #2 -");
    System.out.println("    Expected: Success");
    System.out.println("    Actual: " + passcodeDBHMock2.delete());
    assertEquals("Success", passcodeDBHMock2.delete());
    System.out.println("- Checking if Passcode still exists -");
    System.out.println("    Expected: false");
    System.out.println("    Actual: " + passcodeDBHMock2.isPasscodeExists());
    assertEquals(false, passcodeDBHMock2.isPasscodeExists());
  }

}
