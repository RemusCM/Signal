package org.thoughtcrime.securesms.database;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.thoughtcrime.securesms.PermissionMocking;
import org.thoughtcrime.securesms.PermissionType;
import org.thoughtcrime.securesms.Privilege;
import org.thoughtcrime.securesms.recipients.Recipient;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.thoughtcrime.securesms.PermissionType.EDIT_GROUP;

@RunWith(JUnit4.class)
public class RecipientPrivilegeTest {

    private class FakeRecipientPrivilege implements Privilege{
        private Recipient recipient;
        private GroupDatabase groupDatabase;
        private String currentUserPhoneNumber= "123-456-7890";
        private String groupId = "123";

        @Override
        public boolean canEditGroup() {

            boolean condition = false;
            if (recipient.isGroupRecipient()) {
                if (groupDatabase.isModerator(currentUserPhoneNumber, groupId)) {
                    condition = true;
                }
            }
            return condition;
        }

        @Override
        public boolean canClearGroupConversation() {

            boolean condition = false;
            if (recipient.isGroupRecipient()) {
                if (groupDatabase.isModerator(currentUserPhoneNumber, groupId)) {
                    condition = true;
                }
            }
            return condition;
        }
    }

    @Test
  public void testCanEditGroup() {

  }

  @Test
  public void testCanClearGroup() {

  }

  @Test
  public void testGetPermissionTypeCode() {
    String[] permissions = {"64", "32"};
    assertEquals(PermissionType.EDIT_GROUP.getPermissionTypeCode(), permissions[0]);
    assertEquals(PermissionType.CLEAR_GROUP_CONVERSATION.getPermissionTypeCode(), permissions[1]);
  }

  @Test
  public void testGetPermissionTypeCodeFail() {
    //This test is expected to fail
    String[] permissions = {"34", "47"};
    assertEquals(PermissionType.EDIT_GROUP.getPermissionTypeCode(), permissions[0]);
    assertEquals(PermissionType.CLEAR_GROUP_CONVERSATION.getPermissionTypeCode(), permissions[1]);
  }
}
