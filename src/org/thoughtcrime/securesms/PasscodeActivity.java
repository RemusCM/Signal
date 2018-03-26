package org.thoughtcrime.securesms;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

/**
 * Created by daanish on 3/26/2018.
 */

public class PasscodeActivity extends Activity {


    public static final String THREAD_ID = "threadId";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passcode_list_actions);
    }

}
