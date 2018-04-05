/**
 * Copyright (C) 2012 Whisper Systems
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.thoughtcrime.securesms.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.thoughtcrime.securesms.ConversationAdapter;
import org.thoughtcrime.securesms.R;
import org.thoughtcrime.securesms.database.loaders.ConversationSearchLoader;
import org.thoughtcrime.securesms.database.model.MessageRecord;

public class SearchMessageUtil {

  private static final int PARTIAL_CONVERSATION_LIMIT = 500;

  private Context context;
  private ConversationAdapter conversationAdapter;
  private RecyclerView recyclerView; // contains item (message) info, i.e. position from the lists
  private long threadId;

  public SearchMessageUtil(Context context, ConversationAdapter conversationAdapter, RecyclerView recyclerView, long threadId) {
    this.context = context;
    this.conversationAdapter = conversationAdapter;
    this.recyclerView = recyclerView;
    this.threadId = threadId;
  }

  private int getSearchingMsg() {
    return R.string.conversationFragment_searching;
  }

  private int getNoFoundSoFarMsg() {
    return R.string.conversationFragment_no_message_found_so_far;
  }

  private int getNoFoundMsg() {
    return R.string.conversationFragment_no_message_found;
  }

  /**
   * It returns the cursor which is used in
   * MessageRecord for searching a message
   * @return the cursor of the conversation being search
   */
  private ConversationSearchLoader getConversation() {
    return new ConversationSearchLoader(context, threadId);
  }


  /**
   * It returns the message record given
   * a conversation cursor used
   * for searching the message
   * @param cursor the cursor for searching
   * @return message record of current conversation
   */
  private MessageRecord getRecordFromCursorForSearch(Cursor cursor) {
    return conversationAdapter.getRecordFromCursorForSearch(cursor);
  }

  @SuppressLint("StaticFieldLeak")
  public void search(String query) {
    if (conversationAdapter != null) {
      int maxPosition = recyclerView.getAdapter().getItemCount() - 1;

      Toast searchingToast = Toast.makeText(context, getSearchingMsg(), Toast.LENGTH_SHORT);
      searchingToast.show();

      Cursor cursor = getConversation().getCursor();

      new AsyncTask<String, Void, Integer>() {
        @Override
        protected Integer doInBackground(String... query) {
          // this returns the index of that found message
          return findMessagePosition(cursor, query[0], maxPosition);
        }
        // this uses the index to scroll to the message position
        @Override
        protected void onPostExecute(Integer matchingPosition) {
          searchingToast.cancel();
          if (matchingPosition >= 0) {
            // if match go to the message position
            Toast.makeText(context, "Message found!", Toast.LENGTH_SHORT).show();
            recyclerView.scrollToPosition(matchingPosition);
          } else {
            Toast.makeText(context, getNoFoundMsg(), Toast.LENGTH_SHORT).show();
          }
          // conversation has only 500 messages by default (Signal trim messages > 500)
          // if the message is out of scope display message not found
          if (matchingPosition >= PARTIAL_CONVERSATION_LIMIT) {
            recyclerView.scrollToPosition(maxPosition);
            Toast.makeText(context, getNoFoundSoFarMsg(), Toast.LENGTH_SHORT).show();
          }
        }
      }.execute(query);
    }
  }


  /**
   * Based on the query and the current conversation,
   * this method finds the message
   * from the message record
   *
   * @param query the message to search
   * @return the index of found message
   */
  public int findMessagePosition(Cursor cursor, String query, int maxPosition) {
    String q = query.trim().toLowerCase();
    int invalidPosition = -1;
    if (!q.isEmpty() && cursor != null && cursor.moveToFirst()) {
      while (cursor.moveToNext()) {
        MessageRecord messageRecord = getRecordFromCursorForSearch(cursor);
        if (!messageRecord.isMms()) {
          String message = messageRecord.getDisplayBody().toString().toLowerCase();
          if (message.contains(q)) {
            return cursor.getPosition();
          }
          if (cursor.getPosition() >= maxPosition) {
            return invalidPosition;
          }
        }
      }
    }
    return invalidPosition;
  }

}
