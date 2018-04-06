package org.thoughtcrime.securesms;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.thoughtcrime.securesms.passcode.PasscodeDBHandlerTest;

import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@Ignore
@RunWith(PowerMockRunner.class)
@PrepareForTest({PasscodeDBHandlerTest.class})
public class PasscodeDBhandlerMocking extends BaseUnitTest {

  protected PasscodeDBhandler passcodeDBHMock1;
  protected PasscodeDBhandler passcodeDBHMock2;
  protected PasscodeDBhandler passcodeDBHMock3;
  private String passcode = "2018";
  private String recoveryAnswer = "abc";

  @Override
  public void setUp() {
    passcodeDBHMock1 = mock(PasscodeDBhandler.class);
    passcodeDBHMock2 = mock(PasscodeDBhandler.class);
    passcodeDBHMock3 = mock(PasscodeDBhandler.class);
  }

  protected void setUpPasscodeExistence() {
    when(passcodeDBHMock1.getPasscodeIfExists()).thenReturn(passcode);
    when(passcodeDBHMock1.isPasscodeExists()).thenReturn(true);
  }

  protected void setUpPasscodeAdd() {
    when(passcodeDBHMock2.add()).thenReturn("Success");
  }

  protected void setUpPasscodeUpdate() {
    when(passcodeDBHMock1.update()).thenReturn("Success");
  }

  protected void setUpPasscodeDeletion() {
    when(passcodeDBHMock2.delete()).thenReturn("Success");
    when(passcodeDBHMock2.isPasscodeExists()).thenReturn(false);
  }

  protected void setUpUpdateRecoveryAnswer() {
    when(passcodeDBHMock3.updateRecoveryAnswer()).thenReturn("Recovery answer has been successfully added.");
    when(passcodeDBHMock3.getRecoveryAnswerIfExists()).thenReturn(recoveryAnswer);
  }

}
