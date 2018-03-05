package org.thoughtcrime.securesms;

import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.thoughtcrime.securesms.database.Address;
import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.PermissionDatabase;

import java.util.LinkedList;
import java.util.List;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DatabaseFactory.class})
public class PermissionMocking extends BaseUnitTest {

    protected Address addressMock;
    protected List<Address> addressList = new LinkedList<>();
    protected PermissionDatabase permissionDbMock;
    protected PermissionType canEdit = PermissionType.EDIT_GROUP;
    protected PermissionType canClear = PermissionType.CLEAR_GROUP_CONVERSATION;
    protected String[] privileges;
    protected String moderator = "222";

    //Address.ExternalAddressFormatter formatter = new Address.ExternalAddressFormatter("+12223334444");


    protected void setUpPrivileges(){
        privileges[0] = canEdit.getPermissionTypeCode(); // 64
        privileges[1] = canClear.getPermissionTypeCode(); // 32
    }

    protected void setUpAddressList(){
        addressList.add(addressMock);
    }

    protected void setUpPermission(){
//        permissionDbMock.create("111", moderator, addressList);
    }

}
