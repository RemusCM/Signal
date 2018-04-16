package org.thoughtcrime.securesms.database;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.thoughtcrime.securesms.NicknameMocking;

import static junit.framework.Assert.assertEquals;

@RunWith(JUnit4.class)
public class NicknameTest extends NicknameMocking {

  @Override
  public void setUp() {
    super.setUp();
    setUpDisplayName();
    setUpCustomLabel();
  }

  @Test
  public void testGetDisplayName() {
    System.out.println("Testing DisplayName; Expected: Dennis, Actual: " + recipientDb.getDisplayName());
    assertEquals("Dennis", recipientDb.getDisplayName());
  }

  @Test
  public void testGetCustomLabel() {
    System.out.println("Testing CustomLabel; Expected: CEO, Actual: " + recipientDb.getCustomLabel());
    assertEquals("CEO", recipientDb.getCustomLabel());
  }

}