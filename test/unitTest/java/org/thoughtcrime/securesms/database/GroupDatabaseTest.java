package org.thoughtcrime.securesms.database;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.thoughtcrime.securesms.BaseUnitTest;


import java.util.LinkedList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class GroupDatabaseTest extends BaseUnitTest {

  private PermissionDatabase mockPermissionDatabase;
  private GroupDatabase mockGroupDatabase;
  private PermissionDatabase.PermissionRecord mockPermissionRecord;

  private String moderator, nonModerator1, nonModerator2;
  private String groupId, groupName;

  private String[] givenPrivileges;
  private List<Address> members;

  @Override
  public void setUp() {

    mockPermissionDatabase = mock(PermissionDatabase.class);
    mockGroupDatabase = mock(GroupDatabase.class);
    mockPermissionRecord = mock(PermissionDatabase.PermissionRecord.class);

    setPermissionRecord();


  }

  @Test
  public void testUpdateModeratorColumnByGroupName() {
    mockGroupDatabase.updateModeratorColumnByGroupId("222", groupId);
    assertEquals("222", mockGroupDatabase.getGroupModerator("abc123"));
  }
  
  @Test
  public void testUpdateModeratorColumnByGroupId() {

  }

  @Test
  public void testIsModerator() {
    mockPermissionDatabase.create(
            groupId,
            moderator,
            givenPrivileges,
            members
    );
    assertEquals(true, mockGroupDatabase.isModerator(moderator, groupId));
  }

  private void setPermissionRecord() {
    moderator = "123";
    nonModerator1 = "456";
    nonModerator2 = "789";
    groupId = "abc123";
    groupName = "soen390a";

    givenPrivileges = new String[2];
    givenPrivileges[0] = "64";
    givenPrivileges[1] = "32";

    members = new LinkedList<>();
    members.add(Address.fromSerialized(moderator));
    members.add(Address.fromSerialized(nonModerator1));
    members.add(Address.fromSerialized(nonModerator2));

    when(mockGroupDatabase.isModerator(moderator, groupId)).thenReturn(true);
    when(mockGroupDatabase.getGroupModerator(groupId)).thenReturn("222");
  }
}
