package org.thoughtcrime.securesms.database;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.thoughtcrime.securesms.PermissionMocking;
import org.thoughtcrime.securesms.util.Util;

import java.util.LinkedList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(JUnit4.class)
public class PermissionDatabaseTest extends PermissionMocking {


  @Override
  public void setUp() {
    addressMock = mock(Address.class);
    permissionDbMock = mock(PermissionDatabase.class);
    privileges = new String[2];

    //setUpAddressList();
    //setUpPermission();
    setUpPrivileges();
  }

  @Test
  public void testGetRecipientPrivilegesString() {

  }

  @Test
  public void testHasEditGroupPermission() {

  }

  @Test
  public void testHasClearGroupChatPermission() {

  }

  @Test
  public void testCreate() {

  }

  @Test
  public void testJoinStringPrivileges() {
    // Testing Util:joinStringElements(String[] str)
    String[] arr = {privileges[0], privileges[1]};
    System.out.println("- Testing joinStringPrivileges : Outcome #1 -");
    System.out.println("    Expected: 64,32");
    System.out.println("    Actual: " + Util.joinStringElements(arr));
    assertEquals("64,32", Util.joinStringElements(arr));
  }


  @Test
  public void testSplitStringIntoList() {
    // Testing Util:splitStringIntoList(String str)
    String str = "64,32,16,8,4,2";
    System.out.println("- Testing splitStringIntoList : Outcome #1 -");
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

}
