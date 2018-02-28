package org.thoughtcrime.securesms;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.thoughtcrime.securesms.database.Address;
import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.GroupDatabase;
import org.thoughtcrime.securesms.database.ThreadDatabase;
import org.thoughtcrime.securesms.recipients.Recipient;

import java.util.ArrayList;

@SuppressLint({"Registered", "ValidFragment"})
public class Privilege  {

  private static final String TAG = Privilege.class.getSimpleName();

  private Context context;
  private Recipient recipient;

  public Privilege(Recipient recipient, Context context) {
    this.recipient = recipient;
    this.context = context;

  }

  public String getSomethin() {
    DatabaseFactory.getGroupDatabase(context);
    return " ";
  }
}