package org.thoughtcrime.securesms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by daanish on 3/26/2018.
 */

public class PasscodeActivity extends Activity {

    //ListView passcodeActions;
    public static final String THREAD_ID = "threadId";
    static final String ADD = "ADD";
    static final String UPDATE= "UPDATE";
    static final String DELETE= "DELETE";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passcode_list_actions);
        ListView  passcodeListView = findViewById(R.id.passcode_action_list);

        Intent intent = this.getIntent();
        String threadId = null;
        if(intent != null){
            threadId = intent.getStringExtra(THREAD_ID);
        }

        String[] data= new String[3];
        data[0] = ADD;
        data[1] = UPDATE;
        data[2] = DELETE;

        PasscodeAdapter adapter = new PasscodeAdapter(this, data, threadId);
        passcodeListView.setAdapter(adapter);


    }

    public class PasscodeAdapter extends BaseAdapter{
        Context context;
        String[] data;
        String threadId;
        LayoutInflater inflater;

        public PasscodeAdapter(Context context, String[] data, String threadId){
            this.context = context;
            this.data = data;
            this.threadId = threadId;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }


        @Override
        public int getCount() {
            return data.length;
        }

        @Override
        public Object getItem(int position) {
            return data[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if(view == null){
                view = inflater.inflate(R.layout.passcode_row_action_item, null);
            }

            TextView actionText = view.findViewById(R.id.action_text);
            actionText.setText(data[position]);
            TextView threadIdText = view.findViewById(R.id.thread_id_text);
            threadIdText.setText(threadId);

            return view;
        }
    }

}
