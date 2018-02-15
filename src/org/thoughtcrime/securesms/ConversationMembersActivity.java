package org.thoughtcrime.securesms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;
import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.RecipientDatabase;
import org.thoughtcrime.securesms.jobs.MultiDeviceProfileKeyUpdateJob;
import org.thoughtcrime.securesms.recipients.Recipient;
import org.thoughtcrime.securesms.util.Util;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class ConversationMembersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<GroupMembersList> membersList;
    private Recipient recipient;
    private Context context;
    private String[] names;
    public ConversationMembersActivity(Context context, Recipient recipient){
        this.recipient = recipient;
        this.context = context;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_members);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        

        membersList = new ArrayList<>();


        for(int i = 0; i< names.length; i++){
            GroupMembersList item = new GroupMembersList(names[i]);
            membersList.add(item);
        }


        adapter = new ConversationMembersAdapter(this, membersList);
        recyclerView.setAdapter(adapter);

    }



    private class GroupMembers extends AsyncTask<Void,Void,List<Recipient>>{

        @Override
        public void onPreExecute() {}

        @Override
        protected List<Recipient> doInBackground(Void... params) {
            return DatabaseFactory.getGroupDatabase(context).getGroupMembers(recipient.getAddress().toGroupString(), true);
        }
        @Override
        public void onPostExecute(List<Recipient> members) {
            ConversationMembersActivity.GroupMembers groupMembers = new ConversationMembersActivity.GroupMembers(members);
             names = groupMembers.getRecipientStrings();

        }
        public void display() {
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); }
        private final String TAG = ConversationMembersActivity.GroupMembers.class.getSimpleName();


        private final LinkedList<Recipient> members = new LinkedList<>();

        public GroupMembers(List<Recipient> recipients) {
            for (Recipient recipient : recipients) {
                if (isLocalNumber(recipient)) {
                    members.push(recipient);
                } else {
                    members.add(recipient);
                }
            }
        }

        public String[] getRecipientStrings() {
            List<String> recipientStrings = new LinkedList<>();

            for (Recipient recipient : members) {
                if (isLocalNumber(recipient)) {
                    recipientStrings.add(context.getString(R.string.GroupMembersDialog_me));
                } else {
                    String name = recipient.toShortString();

                    if (recipient.getName() == null && !TextUtils.isEmpty(recipient.getProfileName())) {
                        name += " ~" + recipient.getProfileName();
                    }

                    recipientStrings.add(name);
                }
            }

            return recipientStrings.toArray(new String[members.size()]);
        }

        public Recipient get(int index) {
            return members.get(index);
        }

        private boolean isLocalNumber(Recipient recipient) {
            return Util.isOwnNumber(context, recipient.getAddress());
        }
    }

}
