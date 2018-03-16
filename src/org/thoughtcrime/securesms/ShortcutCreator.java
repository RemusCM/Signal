package org.thoughtcrime.securesms;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
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

public class ShortcutCreator implements ShortcutCreatorInterface {

  private Context context;
  private ShortcutManager shortcutManager;

  @SuppressLint("NewApi")
  @RequiresApi(api = Build.VERSION_CODES.M)
  public ShortcutCreator(Context context) {
    this.context = context;
    this.shortcutManager = context.getSystemService(ShortcutManager.class);
  }

  @TargetApi(Build.VERSION_CODES.N_MR1)
  private void reportShortcutUsed(String id) {
    shortcutManager.reportShortcutUsed(id);
  }

  @SuppressLint("StaticFieldLeak")
  public void createShortcuts() {
    this.reportShortcutUsed("shortcut");

    new AsyncTask<Void, Void, Void>() {


      @RequiresApi(api = Build.VERSION_CODES.N_MR1)
      @Override
      protected Void doInBackground(Void... voids) {

        // for storing shortcuts
        List<ShortcutInfo> shortcutInfoList = new LinkedList<>();

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

  /**
   * Creates an avatar using
   * recipient's name (initial)
   *
   * @param recipient used for configuration
   * @return a text drawable to be used for icon
   */
  public Drawable getTextDrawable(Recipient recipient) {
    return TextDrawable.builder()
            .beginConfig()
            .bold()
            .endConfig()
            .buildRound(getRecipientInitial(recipient),
                    recipient.getColor().toConversationColor(context));
  }

  /**
   * @param drawable to use for creating the icon
   * @return the icon created with drawable
   */
  @SuppressLint("NewApi")
  public Icon getIconBitmapFromDrawable(Drawable drawable) {
    Bitmap bitmap = BitmapUtil.createFromDrawable(drawable, 500, 500);
    return Icon.createWithBitmap(bitmap);
  }

  /**
   * Get the recipient initial by
   * full name. John Doe : JD
   *
   * @param recipient full name
   * @return initial of the recipient
   */
  public String getRecipientInitial(Recipient recipient) {
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