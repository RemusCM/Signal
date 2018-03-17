package org.thoughtcrime.securesms;

import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.thoughtcrime.securesms.database.Address;
import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.PermissionDatabase;

import java.util.LinkedList;
import java.util.List;

import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DatabaseFactory.class})
public class PermissionMocking extends BaseUnitTest {

    protected Address addressMock;
    protected List<Address> addressList = new LinkedList<>();
    protected PermissionDatabase permissionDbMock;
    protected PermissionType canEdit = PermissionType.EDIT_GROUP;
    protected PermissionType canClear = PermissionType.CLEAR_GROUP_CONVERSATION;
    protected String[] privileges;
    protected PermissionDatabase.PermissionRecord pr1;

    //Address.ExternalAddressFormatter formatter = new Address.ExternalAddressFormatter("+12223334444");

    protected void setUpPrivileges(){
        privileges[0] = canEdit.getPermissionTypeCode(); // 64
        privileges[1] = canClear.getPermissionTypeCode(); // 32
    }

    protected void setUpRecipientPrivilegeString(){
        when(permissionDbMock.getRecipientPrivilegesString("123","111")).thenReturn("64");
    }

    protected void setUpAddressList(){
        addressList.add(addressMock);
    }

    protected void setUpPermission(){
        when(permissionDbMock.hasClearGroupChatPermission("123","111")).thenReturn(true);
        when(permissionDbMock.hasEditGroupPermission("123","111")).thenReturn(true);
    }

    protected void setUpPermissionRecord(){
        pr1 = new PermissionDatabase.PermissionRecord("111", "123", "64");
    }

    protected void setUpCreateRecord() {
        when(permissionDbMock.create("ABC123", "111", privileges, addressList)).thenReturn(true);
        when(permissionDbMock.getRecipientPrivilegesString("111", "ABC123")).thenReturn(privileges.toString());
    }

}
