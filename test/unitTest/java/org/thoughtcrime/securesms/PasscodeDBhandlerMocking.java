package org.thoughtcrime.securesms;

import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.PasscodeDatabase;

import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DatabaseFactory.class})
public class PasscodeDBhandlerMocking extends BaseUnitTest {

    protected DatabaseFactory databaseFactoryMock;
    protected PasscodeDatabase passcodeDatabaseMock;
    protected PasscodeDBhandler passcodeDBHMock;
    protected String passcode = "2018";

    @Override
    public void setUp() throws Exception{
        super.setUp();

        databaseFactoryMock = mock(DatabaseFactory.class);
        passcodeDatabaseMock = mock(PasscodeDatabase.class);
        passcodeDBHMock = mock(PasscodeDBhandler.class);
    }

    protected void setUpStaticPasscodeDatabase() {
        PowerMockito.mockStatic(DatabaseFactory.class);
        BDDMockito.given(DatabaseFactory.getInstance(context)).willReturn(databaseFactoryMock);
        BDDMockito.given(DatabaseFactory.getPasscodeDatabase(context)).willReturn(passcodeDatabaseMock);
    }

    protected void setUpPasscodeExistence() {
        when(passcodeDBHMock.getPasscodeIfExists()).thenReturn(passcode);
        when(passcodeDBHMock.isPasscodeExists()).thenReturn(true);
    }



}
