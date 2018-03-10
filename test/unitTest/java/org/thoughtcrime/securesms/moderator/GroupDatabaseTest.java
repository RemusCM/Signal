package org.thoughtcrime.securesms.moderator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.thoughtcrime.securesms.BaseUnitTest;
import org.thoughtcrime.securesms.database.Address;
import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.GroupDatabase;
import org.thoughtcrime.securesms.database.PermissionDatabase;


import java.util.List;
import java.util.Optional;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DatabaseFactory.class})
public class GroupDatabaseTest extends BaseUnitTest {

  private DatabaseFactory databaseFactory;
  private PermissionDatabase permissionDatabase;
  private GroupDatabase groupDatabase;
  private PermissionDatabase.PermissionRecord permissionRecord;

  private String moderator;
  private String groupId;

  @Override
  public void setUp() throws Exception {
    super.setUp();

    permissionDatabase = mock(PermissionDatabase.class);
    groupDatabase = mock(GroupDatabase.class);
    permissionRecord = mock(PermissionDatabase.PermissionRecord.class);

    setupDatabase();
    setUpUpdateModeratorColumn();
    setIsUpModerator();

  }

  private void setupDatabase() {
    PowerMockito.mockStatic(DatabaseFactory.class);
    BDDMockito.given(DatabaseFactory.getInstance(context)).willReturn(databaseFactory);
    BDDMockito.given(DatabaseFactory.getPermissionDatabase(context)).willReturn(permissionDatabase);
    BDDMockito.given(DatabaseFactory.getGroupDatabase(context)).willReturn(groupDatabase);
  }


  public void setIsUpModerator() {
    when(groupDatabase.isModerator("111", groupId)).thenReturn(true);
    when(groupDatabase.isModerator("222", groupId)).thenReturn(true);
    when(groupDatabase.isModerator("333", groupId)).thenReturn(false);
    when(groupDatabase.isModerator("444", groupId)).thenReturn(false);
  }

  public void setUpUpdateModeratorColumn() {
    moderator = "999";
    groupId = "ABC";
    when(groupDatabase.getGroupModerator(groupId)).thenReturn(moderator);
  }

  @Test
  public void testUpdateModeratorColumnByGroupName() {

  }

  @Test
  public void testUpdateModeratorColumnByGroupId() {
    System.out.println("testUpdateModeratorColumnByGroupId()");
    groupDatabase.updateModeratorColumnByGroupId("999", groupId);
    String newModerator = groupDatabase.getGroupModerator(groupId);
    System.out.println(newModerator);
    assertEquals("999", newModerator);
  }

  @Test
  public void testIsModerator() {
    System.out.println("testIsModerator()");
    assertTrue(groupDatabase.isModerator("111", groupId));
    assertTrue(groupDatabase.isModerator("222", groupId));
    assertFalse(groupDatabase.isModerator("333", groupId));
    assertFalse(groupDatabase.isModerator("444", groupId));
  }


}
