package org.thoughtcrime.securesms;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.amulyakhare.textdrawable.TextDrawable;

import org.junit.Test;
import org.thoughtcrime.securesms.crypto.MasterCipher;
import org.thoughtcrime.securesms.crypto.MasterSecret;
import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.ThreadDatabase;
import org.thoughtcrime.securesms.database.model.ThreadRecord;
import org.thoughtcrime.securesms.recipients.Recipient;
import org.thoughtcrime.securesms.service.KeyCachingService;

import java.util.LinkedList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class ShortcutCreatorTest {

  private class FakeShortcutCreator implements ShortcutCreatorInterface {

    private Context context;
    ShortcutManager shortcutManager;
    List<ShortcutInfo> shortcutInfoList = mock(LinkedList.class);


    @Override
    public void createShortcuts() {
      context = mock(Context.class);
      shortcutManager = context.getSystemService(ShortcutManager.class);;

      new AsyncTask<Void, Void, Void>() {


        @RequiresApi(api = Build.VERSION_CODES.N_MR1)
        @Override
        protected Void doInBackground(Void... voids) {

          // required to create a reader
          MasterSecret masterSecret = KeyCachingService.getMasterSecret(context);
          if (masterSecret == null) {
            return null;
          }
          // get the most recent conversations from the database
          ThreadDatabase threadDatabase = DatabaseFactory.getThreadDatabase(context);
          // limit the results to 5 conversations
          Cursor cursor = threadDatabase.getRecentConversationList(5);

          try {
            // record contains the five most recent conversation
            // reader is used for reading the result you get from the database
            ThreadRecord record;
            ThreadDatabase.Reader reader = threadDatabase.readerFor(cursor, new MasterCipher(masterSecret));

            // initialized the intent with each recipient so that when you click the shortcut you get
            // to conversation with all the necessary information i.e. messages and last seen date
            while ((record = reader.getNext()) != null) {
              Recipient recipient = record.getRecipient();
              if (recipient.getName() != null) {
                Intent intent = new Intent(context, ConversationActivity.class);
                if (recipient.getAddress().isPhone()) {
                  intent.putExtra(ConversationActivity.ADDRESS_EXTRA, recipient.getAddress().toPhoneString());
                } else if (recipient.getAddress().isGroup()) {
                  intent.putExtra(ConversationActivity.ADDRESS_EXTRA, recipient.getAddress().toGroupString());
                }
                intent.putExtra(ConversationActivity.THREAD_ID_EXTRA, record.getThreadId());
                intent.putExtra(ConversationActivity.DISTRIBUTION_TYPE_EXTRA, record.getDistributionType());
                intent.putExtra(ConversationActivity.TIMING_EXTRA, System.currentTimeMillis());
                intent.putExtra(ConversationActivity.LAST_SEEN_EXTRA, record.getLastSeen());
                intent.setAction(Intent.ACTION_VIEW);

                String shortLabel = recipient.getName();
                String longLabel = shortLabel;
                if (shortLabel == null) {
                  shortLabel = recipient.getAddress().serialize();
                }

                Drawable drawable = getTextDrawable(recipient);
                shortcutInfoList.add(new ShortcutInfo.Builder(context, record.getRecipient().getName())
                        .setIntent(intent)
                        .setRank(shortcutInfoList.size())
                        .setShortLabel(shortLabel)
                        .setLongLabel(longLabel)
                        .setIcon(getIconBitmapFromDrawable(drawable))
                        .build());

              }
            }

            when(!shortcutInfoList.isEmpty()).thenReturn(true);

          } finally {
            if (cursor != null) {
              cursor.close();
            }
            if (!shortcutInfoList.isEmpty()) {
              shortcutManager.setDynamicShortcuts(shortcutInfoList);
            }
          }

          return null;
        }
      }.execute();


    }

    @Override
    public Drawable getTextDrawable(Recipient recipient) {
      //Drawable drawable = TextDrawable.builder().buildRect("A", Color.BLACK);
      //when(drawable.canApplyTheme()).thenReturn(true);
      //return drawable;

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
    FakeShortcutCreator fakeShortcutCreator = new FakeShortcutCreator();
    fakeShortcutCreator.createShortcuts();

    assertTrue(!fakeShortcutCreator.shortcutInfoList.isEmpty());
  }

  @Test
  public void testGetTextDrawable() {
    //FakeShortcutCreator fakeShortcutCreator = new FakeShortcutCreator();
    //Recipient recipient = mock(Recipient.class);

    //Drawable d = TextDrawable.builder().buildRect("A", Color.BLACK);

    //assertTrue(d.canApplyTheme());
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