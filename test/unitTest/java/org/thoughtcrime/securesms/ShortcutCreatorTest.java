package org.thoughtcrime.securesms;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;

import org.junit.Test;
import org.thoughtcrime.securesms.recipients.Recipient;

import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class ShortcutCreatorTest {

  private class FakeShortcutCreator implements ShortcutCreatorInterface {

    @Override
    public void createShortcuts() {
      // TODO
    }

    @Override
    public Drawable getTextDrawable(Recipient recipient) {
      // TODO
      return null;
    }

    @Override
    public Icon getIconBitmapFromDrawable(Drawable drawable) {
      // TODO
      return null;
    }

    @Override
    public String getRecipientInitial(Recipient recipient) {
      // TODO
      return null;
    }
  }

  /***************************************
   * start unit test for ShortcutCreator *
   ***************************************/

  @Test
  public void testCreateShortcuts() {
    // TODO
  }

  @Test
  public void testGetTextDrawable() {
    // TODO
  }

  @Test
  public void testGetIconBitmapFromDrawable() {
    // TODO
  }

  @Test
  public void testGetRecipientInitial() {
    // TODO
  }
}