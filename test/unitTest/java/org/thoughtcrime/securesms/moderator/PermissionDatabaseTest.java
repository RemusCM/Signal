package org.thoughtcrime.securesms.moderator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.thoughtcrime.securesms.PermissionMocking;
import org.thoughtcrime.securesms.database.Address;
import org.thoughtcrime.securesms.database.PermissionDatabase;
import org.thoughtcrime.securesms.util.Util;

import java.util.LinkedList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(JUnit4.class)
public class PermissionDatabaseTest extends PermissionMocking {


  @Override
  public void setUp() {
    super.setUp();
    setUpAddressList();
    setUpPermission();
    setUpPrivileges();
    setUpPermissionRecord();
    setUpRecipientPrivilegeString();
    setUpCreateRecord();
  }

  @Test
  public void testGetRecipientPrivilegesString() {
    System.out.println("\n- Testing getRecipientPrivilegesString : Outcome #1 -");
    System.out.println("    Expected: 64");
    System.out.println("    Actual: " + permissionDbMock.getRecipientPrivilegesString("123","111"));
    assertEquals("64",permissionDbMock.getRecipientPrivilegesString("123","111"));
  }

  @Test
  public void testHasEditGroupPermission() {
    System.out.println("\n- Testing hasEditGroupPermission : Outcome #1 -");
    System.out.println("    Expected: True");
    System.out.println("    Actual: " + permissionDbMock.hasEditGroupPermission("123","111"));
    assertTrue(permissionDbMock.hasEditGroupPermission("123","111"));
  }

  @Test
  public void testHasClearGroupChatPermission() {
    System.out.println("\n- Testing hasClearGroupChatPermission : Outcome #1 -");
    System.out.println("    Expected: True");
    System.out.println("    Actual: " + permissionDbMock.hasClearGroupChatPermission("123","111"));
    assertTrue(permissionDbMock.hasClearGroupChatPermission("123","111"));
  }

  @Test
  public void testGetPrivileges() {
    System.out.println("\n- Testing getPrivileges : Outcome #1 -");
    System.out.println("    Expected: 64");
    System.out.println("    Actual: " + pr1.getPrivileges());
    assertEquals("64",pr1.getPrivileges());
  }

  @Test
  public void testJoinStringPrivileges() {
    // Testing Util:joinStringElements(String[] str)
    String[] arr = {privileges[0], privileges[1]};
    System.out.println("\n- Testing joinStringPrivileges : Outcome #1 -");
    System.out.println("    Expected: 64,32");
    System.out.println("    Actual: " + Util.joinStringElements(arr));
    assertEquals("64,32", Util.joinStringElements(arr));
  }


  @Test
  public void testSplitStringIntoList() {
    // Testing Util:splitStringIntoList(String str)
    String str = "64,32,16,8,4,2";
    System.out.println("\n- Testing splitStringIntoList : Outcome #1 -");
    List<String> stringList = new LinkedList<>();
    stringList.add("64");
    stringList.add("32");
    stringList.add("16");
    stringList.add("8");
    stringList.add("4");
    stringList.add("2");
    System.out.println("    Expected: " + stringList.toString());
    System.out.println("    Actual: " + Util.splitStringIntoList(str));
    assertEquals(stringList, Util.splitStringIntoList(str));
  }

  @Test
  public void testCreateRecord() {
    System.out.println("\n- Testing testCreateRecord : Outcome #1 -");
    boolean isCreated = permissionDbMock.create("ABC123", "111", privileges, addressList);
    assertTrue(isCreated);
    assertEquals(privileges.toString(), permissionDbMock.getRecipientPrivilegesString("111", "ABC123"));
  }

  @Test
  public void testCreateRecordFail() {
    System.out.println("- FAIL case: testCreateRecordFail() -");
    boolean isCreated;
    isCreated = permissionDbMock.create("ABC123", "222", privileges, addressList);
    assertFalse(isCreated);
    assertNotSame(privileges.toString(), permissionDbMock.getRecipientPrivilegesString("222", "ABC123"));
  }

}
