package org.thoughtcrime.securesms.util;

import android.app.Activity;
import android.content.Intent;

import org.thoughtcrime.securesms.R;

public class DynamicTheme extends Activity {

  public static final String DARK  = "dark";
  public static final String LIGHT = "light";
  public static final String PINK = "pink";

  private int currentTheme;

  public void onCreate(Activity activity) {
    currentTheme = getSelectedTheme(activity);
    activity.setTheme(currentTheme);
  }

  public void onResume(Activity activity) {
    if (currentTheme != getSelectedTheme(activity)) {
      Intent intent = activity.getIntent();
      activity.finish();
      OverridePendingTransition.invoke(activity);
      activity.startActivity(intent);
      OverridePendingTransition.invoke(activity);
    }
  }

  protected int getSelectedTheme(Activity activity) {
    String theme = TextSecurePreferences.getTheme(activity);

    if (theme.equals(DARK)) return R.style.TextSecure_DarkTheme;
    if (theme.equals(PINK)) return R.style.TextSecure_PinkTheme;
    return R.style.TextSecure_LightTheme;
  }

  private static final class OverridePendingTransition {
    static void invoke(Activity activity) {
      activity.overridePendingTransition(0, 0);
    }
  }
}
