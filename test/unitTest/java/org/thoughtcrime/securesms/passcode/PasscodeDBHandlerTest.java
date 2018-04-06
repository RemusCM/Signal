package org.thoughtcrime.securesms.passcode;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.thoughtcrime.securesms.PasscodeDBhandlerMocking;
import org.thoughtcrime.securesms.database.DatabaseFactory;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

@RunWith(JUnit4.class)
@PrepareForTest({DatabaseFactory.class})
public class PasscodeDBHandlerTest extends PasscodeDBhandlerMocking {


  @Override
  public void setUp() {
    super.setUp();
    setUpPasscodeExistence();
    setUpPasscodeAdd();
    setUpPasscodeUpdate();
    setUpPasscodeDeletion();
    setUpUpdateRecoveryAnswer();
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
  public void testUpdateFailCase() {
    System.out.println("\n- Testing update FAIL case: Mock #1 -");
    System.out.println("    Expected: Success");
    System.out.println("    Actual: " + passcodeDBHMock1.update());
    assertThat(passcodeDBHMock1.update(), not(equalTo("Error")));
  }

  @Test
  public void testAdd() {
    System.out.println("\n- Testing add : Mock #2 -");
    System.out.println("    Expected: Success");
    System.out.println("    Actual: " + passcodeDBHMock2.add());
    assertEquals("Success", passcodeDBHMock2.add());
  }

  @Test
  public void testAddFailCase() {
    System.out.println("\n- Testing add FAIL case: Mock #2 -");
    System.out.println("    Expected: Success");
    System.out.println("    Actual: " + passcodeDBHMock2.add());
    // reverse assertion: asserts that the object under test must
    // return "Success" and not any other value i.e. Error
    // assertThat(passcodeDBHMock2.add(), not(equalTo("Success"))); // FAIL
    assertThat(passcodeDBHMock2.add(), not(equalTo("Error")));
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

  @Test
  public void testDeleteFailCase() {
    System.out.println("\n- Testing delete FAIL case : Mock #2 -");
    System.out.println("    Expected: Success");
    System.out.println("    Actual: " + passcodeDBHMock2.delete());
    assertThat(passcodeDBHMock2.delete(), not(equalTo("Error")));
  }

  @Test
  public void testUpdateRecoveryAnswer() {
    System.out.println("\n- Testing updateRecoveryAnswer : Mock #3 -");
    System.out.println("    Expected: Recovery answer has been successfully added.");
    System.out.println("    Actual: " + passcodeDBHMock3.updateRecoveryAnswer());
    assertEquals("Recovery answer has been successfully added.", passcodeDBHMock3.updateRecoveryAnswer());
  }

}
