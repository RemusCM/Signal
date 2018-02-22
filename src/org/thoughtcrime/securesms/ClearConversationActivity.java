package org.thoughtcrime.securesms;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

@SuppressLint("Registered")
public class ClearConversationActivity implements DialogInterface.OnClickListener{

  private static final String TAG = ClearConversationActivity.class.getSimpleName();

  private final FragmentActivity activity;

  ClearConversationActivity(FragmentActivity activity) {
    this.activity = activity;
  }

  @SuppressLint("StaticFieldLeak")
  @Override
  public void onClick(DialogInterface dialog, int which) {
    new AsyncTask<Void, Void, Void>() {
      @Override
      protected Void doInBackground(Void... params) {
        // TODO
        return null;
      }
    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
  }
}