package org.thoughtcrime.securesms.webrtc;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.thoughtcrime.securesms.R;

public class ChangeNickname extends Activity implements View.OnClickListener{
    private Button button1;
    final Context context = this;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipient_preference_activity);

        button1 = (Button) findViewById(R.id.nicknameBtn);
        button1.setOnClickListener(this);
    }


    public void onClick(View view){
        final Dialog dialog = new Dialog(context);
        

    }
}
