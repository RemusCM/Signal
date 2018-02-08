package org.thoughtcrime.securesms;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;


import org.thoughtcrime.securesms.R;

public class ChangeNickname extends Activity implements OnClickListener {
    private Button button1;
    final Context context = this;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipient_preference_activity);

        button1 = (Button) findViewById(R.id.nicknameBtn);
        button1.setOnClickListener(this);
    }


    @Override
    public void onClick(View view){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.add_nickname_dialog);
        dialog.setTitle("Title...");

        TextView txt = (TextView) dialog.findViewById(R.id.new_text);
        txt.setText("Android custom dialog example");
        ImageView image = (ImageView) dialog.findViewById(R.id.image);
//        image.setImageResource(R.drawable.);

        Button dialogButtonOk = (Button) dialog.findViewById(R.id.dialogButtonOK);
        dialogButtonOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}
