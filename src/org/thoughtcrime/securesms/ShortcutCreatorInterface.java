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

public interface ShortcutCreatorInterface {

  void createShortcuts();

  Drawable getTextDrawable(Recipient recipient);

  Icon getIconBitmapFromDrawable(Drawable drawable);

  String getRecipientInitial(Recipient recipient);

}