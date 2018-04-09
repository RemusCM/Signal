package org.thoughtcrime.securesms;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DrawingToolUITest {

    @Rule
    public ActivityTestRule<ConversationListActivity> mActivityTestRule = new ActivityTestRule<>(ConversationListActivity.class);

    @Test
    public void drawingToolUITest() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("Continue"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton.perform(scrollTo(), click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.number), withText("5149163416"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.registration_container),
                                        1),
                                2)));
        appCompatEditText.perform(scrollTo(), replaceText("(514) 916-3416"));

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.number), withText("(514) 916-3416"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.registration_container),
                                        1),
                                2),
                        isDisplayed()));
        appCompatEditText2.perform(closeSoftKeyboard());

        ViewInteraction circularProgressButton = onView(
                allOf(withId(R.id.registerButton), withText("Register"),
                        childAtPosition(
                                allOf(withId(R.id.registration_container),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                2)),
                                2)));
        circularProgressButton.perform(scrollTo(), click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(android.R.id.button1), withText("Continue"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton2.perform(scrollTo(), click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.skip_button), withText("Set later"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.container),
                                        2),
                                1),
                        isDisplayed()));
        appCompatTextView.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(android.R.id.button2), withText("No thanks"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttonPanel),
                                        0),
                                2)));
        appCompatButton3.perform(scrollTo(), click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction pulsingFloatingActionButton = onView(
                allOf(withId(R.id.fab), withContentDescription("New conversation"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_container),
                                        0),
                                3),
                        isDisplayed()));
        pulsingFloatingActionButton.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recycler_view),
                        childAtPosition(
                                withId(R.id.swipe_refresh),
                                0)));
        recyclerView.perform(actionOnItemAtPosition(4, click()));

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.attach_button), withContentDescription("Add attachment"),
                        childAtPosition(
                                allOf(withId(R.id.button_toggle),
                                        childAtPosition(
                                                withId(R.id.bottom_panel),
                                                1)),
                                0),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction circleColorImageView = onView(
                allOf(withId(R.id.drawing_button), withContentDescription("Toggle attachment drawer"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                0),
                        isDisplayed()));
        circleColorImageView.perform(click());

        ViewInteraction button = onView(
                allOf(withId(R.id.pen_button), withText("PEN"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                0),
                        isDisplayed()));
        button.perform(click());

        ViewInteraction button2 = onView(
                allOf(withId(R.id.save_button), withText("SAVE"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                2),
                        isDisplayed()));
        button2.perform(click());

        ViewInteraction button3 = onView(
                allOf(withId(android.R.id.button1), withText("Yes"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        button3.perform(scrollTo(), click());

        ViewInteraction sendButton = onView(
                allOf(withId(R.id.send_button), withContentDescription("Signal"),
                        childAtPosition(
                                allOf(withId(R.id.button_toggle),
                                        childAtPosition(
                                                withId(R.id.bottom_panel),
                                                1)),
                                1),
                        isDisplayed()));
        sendButton.perform(click());

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
