package org.thoughtcrime.securesms;

import android.support.test.rule.ActivityTestRule;
import android.app.Activity;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.filters.LargeTest;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Rule;
import org.junit.runners.JUnit4;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.thoughtcrime.securesms.util.DynamicTheme;
import org.thoughtcrime.securesms.util.TextSecurePreferences;

import android.content.Context;
import android.test.InstrumentationTestCase;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;

@RunWith(JUnit4.class)
public class ChangeThemeTest {

    private static final String PINK_THEME_TEXT = "pink";

    @Rule
    public ActivityTestRule<DynamicTheme> mActivityRule = new ActivityTestRule<>(
            DynamicTheme.class);

    @Test
    public void testSomething() {
        mActivityRule.getActivity();
        String theme = TextSecurePreferences.getTheme(mActivityRule.getActivity());
        assertEquals("pink", theme);
    }

}
