package org.thoughtcrime.securesms;

import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.PasscodeDatabase;

import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class PasscodeDBhandlerMocking extends BaseUnitTest {

  private DatabaseFactory databaseFactoryMock;
  private PasscodeDatabase passcodeDatabaseMock;
  protected PasscodeDBhandler passcodeDBHMock1;
  protected PasscodeDBhandler passcodeDBHMock2;
  private String passcode = "2018";

  @Override
  public void setUp() {
    databaseFactoryMock = mock(DatabaseFactory.class);
    passcodeDatabaseMock = mock(PasscodeDatabase.class);
    passcodeDBHMock1 = mock(PasscodeDBhandler.class);
    passcodeDBHMock2 = mock(PasscodeDBhandler.class);
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


}
