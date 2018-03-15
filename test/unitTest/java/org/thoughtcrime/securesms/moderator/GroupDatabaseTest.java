package org.thoughtcrime.securesms.moderator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.thoughtcrime.securesms.BaseUnitTest;
import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.GroupDatabase;
import org.thoughtcrime.securesms.database.PermissionDatabase;


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

  private String groupId = "ABC123";

  @Override
  public void setUp() throws Exception {
    super.setUp();

    permissionDatabase = mock(PermissionDatabase.class);
    groupDatabase = mock(GroupDatabase.class);

    setupDatabase();
    setUpGroupDatabase();

  }

  private void setupDatabase() {
    PowerMockito.mockStatic(DatabaseFactory.class);
    BDDMockito.given(DatabaseFactory.getInstance(context)).willReturn(databaseFactory);
    BDDMockito.given(DatabaseFactory.getPermissionDatabase(context)).willReturn(permissionDatabase);
    BDDMockito.given(DatabaseFactory.getGroupDatabase(context)).willReturn(groupDatabase);
  }

  private void setUpGroupDatabase() {
    when(groupDatabase.isModerator("111", groupId)).thenReturn(true);
    when(groupDatabase.isModerator("222", groupId)).thenReturn(true);
    when(groupDatabase.isModerator("333", groupId)).thenReturn(false);
    when(groupDatabase.isModerator("444", groupId)).thenReturn(false);

    when(groupDatabase.updateModeratorColumnByGroupId("777", "ABC123")).thenReturn(true);
    when(groupDatabase.getGroupModeratorByGroupId("ABC123")).thenReturn("777");
    when(groupDatabase.updateModeratorColumnByGroupName("999", "SOEN390")).thenReturn(true);
    when(groupDatabase.getMembersByGroupId("SOEN390")).thenReturn("999");
  }

  @Test
  public void testUpdateModeratorColumnByGroupName() {
    System.out.println("\ntestUpdateModeratorColumnByGroupName(): - " + "Will pass");
    boolean isUpdated = groupDatabase.updateModeratorColumnByGroupName("999", "SOEN390");
    assertEquals("999", groupDatabase.getMembersByGroupId("SOEN390"));
    assertTrue(isUpdated);
  }

  @Test
  public void testUpdateModeratorColumnByGroupId() {
    System.out.println("\ntestUpdateModeratorColumnByGroupId(): - " + "Will pass");
    boolean isUpdated = groupDatabase.updateModeratorColumnByGroupId("777", "ABC123");
    assertEquals("777", groupDatabase.getGroupModeratorByGroupId("ABC123"));
    assertTrue(isUpdated);
  }

  @Test
  public void testIsModerator() {
    System.out.println("\ntestIsModerator(): - " + "Will pass");
    assertTrue(groupDatabase.isModerator("111", groupId));
    assertTrue(groupDatabase.isModerator("222", groupId));
    assertFalse(groupDatabase.isModerator("333", groupId));
    assertFalse(groupDatabase.isModerator("444", groupId));
  }

  @Test
  public void testIsModeratorFail() {
    System.out.println("\ntestIsModeratorFail(): - " + "Will fail");
    assertFalse(groupDatabase.isModerator("111", groupId));
    assertFalse(groupDatabase.isModerator("222", groupId));
    assertTrue(groupDatabase.isModerator("333", groupId));
    assertTrue(groupDatabase.isModerator("444", groupId));
  }


}
