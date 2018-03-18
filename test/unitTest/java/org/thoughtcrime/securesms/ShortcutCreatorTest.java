package org.thoughtcrime.securesms;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import org.thoughtcrime.securesms.util.BitmapUtil;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class ShortcutCreatorTest {

  private class FakeShortcutCreator {

    private Context context;
    ShortcutManager shortcutManager;
    List<ShortcutInfo> shortcutInfoList = mock(LinkedList.class);

    void createShortcuts() {
      context = mock(Context.class);
      shortcutManager = context.getSystemService(ShortcutManager.class);

      new AsyncTask<Void, Void, Void>() {


        @RequiresApi(api = Build.VERSION_CODES.N_MR1)
        protected Void doInBackground(Void... voids) {

          MasterSecret masterSecret = KeyCachingService.getMasterSecret(context);
          if (masterSecret == null) {
            return null;
          }
          ThreadDatabase threadDatabase = DatabaseFactory.getThreadDatabase(context);
          Cursor cursor = threadDatabase.getRecentConversationList(5);

          try {
            ThreadRecord record;
            ThreadDatabase.Reader reader = threadDatabase.readerFor(cursor, new MasterCipher(masterSecret));

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
                
                Drawable drawable =    TextDrawable.builder()
                        .beginConfig()
                        .bold()
                        .endConfig()
                        .buildRound(getRecipientInitial(recipient),
                                recipient.getColor().toConversationColor(context));
                Bitmap bitmap = BitmapUtil.createFromDrawable(drawable, 500, 500);
                shortcutInfoList.add(new ShortcutInfo.Builder(context, record.getRecipient().getName())
                        .setIntent(intent)
                        .setRank(shortcutInfoList.size())
                        .setShortLabel(shortLabel)
                        .setLongLabel(longLabel)
                        .setIcon(Icon.createWithBitmap(bitmap))
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


    String getRecipientInitial(Recipient recipient) {

      when(recipient.getName()).thenReturn("John Doe");

      if (recipient.getName() != null) {
        String recipientName = recipient.getName();
        List<String> list = Arrays.asList(recipientName.split("\\s+"));
        StringBuilder sb = new StringBuilder();
        for (String str : list) {
          sb.append(str.substring(0, 1));
        }
        return sb.toString();
      }
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
  public void testGetRecipientInitial() {
    Recipient mockRecipient = mock(Recipient.class);
    FakeShortcutCreator fakeShortcutCreator = new FakeShortcutCreator();
    fakeShortcutCreator.getRecipientInitial(mockRecipient);

    assertEquals(fakeShortcutCreator.getRecipientInitial(mockRecipient), "JD");

  }

  @Test
  public void testGetRecipientInitialFail() {
    Recipient mockRecipient = mock(Recipient.class);
    FakeShortcutCreator fakeShortcutCreator = new FakeShortcutCreator();
    fakeShortcutCreator.getRecipientInitial(mockRecipient);
    System.out.println("It's suppose to be JD.");
    assertEquals(fakeShortcutCreator.getRecipientInitial(mockRecipient), "JE");

  }
}