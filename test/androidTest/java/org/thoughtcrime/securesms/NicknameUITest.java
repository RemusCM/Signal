package org.thoughtcrime.securesms;


import android.support.test.espresso.DataInteraction;
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

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class NicknameUITest {

    @Rule
    public ActivityTestRule<ConversationListActivity> mActivityTestRule = new ActivityTestRule<>(ConversationListActivity.class);

    @Test
    public void nicknameUITest() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.number),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.registration_container),
                                        1),
                                2)));
        appCompatEditText.perform(scrollTo(), click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.number),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.registration_container),
                                        1),
                                2)));
        appCompatEditText2.perform(scrollTo(), replaceText("55"), closeSoftKeyboard());

        ViewInteraction editText = onView(
                allOf(withId(R.id.country_search),
                        childAtPosition(
                                allOf(withId(R.id.fragment_content),
                                        childAtPosition(
                                                withClassName(is("android.widget.FrameLayout")),
                                                0)),
                                0),
                        isDisplayed()));
        editText.perform(click());

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.country_search),
                        childAtPosition(
                                allOf(withId(R.id.fragment_content),
                                        childAtPosition(
                                                withClassName(is("android.widget.FrameLayout")),
                                                0)),
                                0),
                        isDisplayed()));
        editText2.perform(click());

        ViewInteraction editText3 = onView(
                allOf(withId(R.id.country_search),
                        childAtPosition(
                                allOf(withId(R.id.fragment_content),
                                        childAtPosition(
                                                withClassName(is("android.widget.FrameLayout")),
                                                0)),
                                0),
                        isDisplayed()));
        editText3.perform(click());

        ViewInteraction editText4 = onView(
                allOf(withId(R.id.country_search),
                        childAtPosition(
                                allOf(withId(R.id.fragment_content),
                                        childAtPosition(
                                                withClassName(is("android.widget.FrameLayout")),
                                                0)),
                                0),
                        isDisplayed()));
        editText4.perform(replaceText("canada"), closeSoftKeyboard());

        DataInteraction linearLayout = onData(anything())
                .inAdapterView(allOf(withId(android.R.id.list),
                        childAtPosition(
                                withId(R.id.fragment_content),
                                1)))
                .atPosition(0);
        linearLayout.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.number), withText("55"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.registration_container),
                                        1),
                                2)));
        appCompatEditText3.perform(scrollTo(), click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.number), withText("55"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.registration_container),
                                        1),
                                2)));
        appCompatEditText4.perform(scrollTo(), click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.number), withText("55"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.registration_container),
                                        1),
                                2)));
        appCompatEditText5.perform(scrollTo(), replaceText("5"));

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.number), withText("5"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.registration_container),
                                        1),
                                2),
                        isDisplayed()));
        appCompatEditText6.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.number), withText("5"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.registration_container),
                                        1),
                                2)));
        appCompatEditText7.perform(scrollTo(), click());

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.number), withText("5"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.registration_container),
                                        1),
                                2)));
        appCompatEditText8.perform(scrollTo(), replaceText("(514) 679-1670"));

        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.number), withText("(514) 679-1670"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.registration_container),
                                        1),
                                2),
                        isDisplayed()));
        appCompatEditText9.perform(closeSoftKeyboard());

        ViewInteraction circularProgressButton = onView(
                allOf(withId(R.id.registerButton), withText("Register"),
                        childAtPosition(
                                allOf(withId(R.id.registration_container),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                2)),
                                2)));
        circularProgressButton.perform(scrollTo(), click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction emojiEditText = onView(
                allOf(withId(R.id.name), withText("glasslake514"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.container),
                                        0),
                                1),
                        isDisplayed()));
        emojiEditText.perform(replaceText("glasslake51"));

        ViewInteraction emojiEditText2 = onView(
                allOf(withId(R.id.name), withText("glasslake51"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.container),
                                        0),
                                1),
                        isDisplayed()));
        emojiEditText2.perform(closeSoftKeyboard());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction emojiEditText3 = onView(
                allOf(withId(R.id.name), withText("glasslake51"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.container),
                                        0),
                                1),
                        isDisplayed()));
        emojiEditText3.perform(replaceText("glasslake"));

        ViewInteraction emojiEditText4 = onView(
                allOf(withId(R.id.name), withText("glasslake"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.container),
                                        0),
                                1),
                        isDisplayed()));
        emojiEditText4.perform(closeSoftKeyboard());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction emojiEditText5 = onView(
                allOf(withId(R.id.name), withText("glasslake"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.container),
                                        0),
                                1),
                        isDisplayed()));
        emojiEditText5.perform(replaceText("glasslak"));

        ViewInteraction emojiEditText6 = onView(
                allOf(withId(R.id.name), withText("glasslak"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.container),
                                        0),
                                1),
                        isDisplayed()));
        emojiEditText6.perform(closeSoftKeyboard());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction emojiEditText7 = onView(
                allOf(withId(R.id.name), withText("glasslak"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.container),
                                        0),
                                1),
                        isDisplayed()));
        emojiEditText7.perform(replaceText("glassla"));

        ViewInteraction emojiEditText8 = onView(
                allOf(withId(R.id.name), withText("glassla"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.container),
                                        0),
                                1),
                        isDisplayed()));
        emojiEditText8.perform(closeSoftKeyboard());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction emojiEditText9 = onView(
                allOf(withId(R.id.name), withText("glassla"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.container),
                                        0),
                                1),
                        isDisplayed()));
        emojiEditText9.perform(replaceText("glassl"));

        ViewInteraction emojiEditText10 = onView(
                allOf(withId(R.id.name), withText("glassl"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.container),
                                        0),
                                1),
                        isDisplayed()));
        emojiEditText10.perform(closeSoftKeyboard());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction emojiEditText11 = onView(
                allOf(withId(R.id.name), withText("glassl"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.container),
                                        0),
                                1),
                        isDisplayed()));
        emojiEditText11.perform(replaceText(""));

        ViewInteraction emojiEditText12 = onView(
                allOf(withId(R.id.name),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.container),
                                        0),
                                1),
                        isDisplayed()));
        emojiEditText12.perform(closeSoftKeyboard());

        ViewInteraction emojiEditText13 = onView(
                allOf(withId(R.id.name),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.container),
                                        0),
                                1),
                        isDisplayed()));
        emojiEditText13.perform(click());

        ViewInteraction emojiEditText14 = onView(
                allOf(withId(R.id.name),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.container),
                                        0),
                                1),
                        isDisplayed()));
        emojiEditText14.perform(click());

        ViewInteraction emojiEditText15 = onView(
                allOf(withId(R.id.name),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.container),
                                        0),
                                1),
                        isDisplayed()));
        emojiEditText15.perform(replaceText("dennis"), closeSoftKeyboard());

        ViewInteraction circularProgressButton2 = onView(
                allOf(withId(R.id.finish_button), withText("FINISH"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.container),
                                        2),
                                0),
                        isDisplayed()));
        circularProgressButton2.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button2), withText("No thanks"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttonPanel),
                                        0),
                                2)));
        appCompatButton.perform(scrollTo(), click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.cancel), withContentDescription("Cancel"),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(R.id.reminder),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction pulsingFloatingActionButton = onView(
                allOf(withId(R.id.fab), withContentDescription("New conversation"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_container),
                                        0),
                                3),
                        isDisplayed()));
        pulsingFloatingActionButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.list),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                1)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.title), withText("Conversation settings"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.v7.view.menu.ListMenuItemView")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.list),
                        childAtPosition(
                                withClassName(is("android.widget.FrameLayout")),
                                0)));
        recyclerView2.perform(actionOnItemAtPosition(5, click()));

        ViewInteraction editText5 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.custom),
                                childAtPosition(
                                        withId(R.id.customPanel),
                                        0)),
                        0),
                        isDisplayed()));
        editText5.perform(replaceText("ray"), closeSoftKeyboard());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(android.R.id.button1), withText("SAVE"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttonPanel),
                                        0),
                                3)));
        appCompatButton2.perform(scrollTo(), click());

        ViewInteraction recyclerView3 = onView(
                allOf(withId(R.id.list),
                        childAtPosition(
                                withClassName(is("android.widget.FrameLayout")),
                                0)));
        recyclerView3.perform(actionOnItemAtPosition(5, click()));

        ViewInteraction editText6 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.custom),
                                childAtPosition(
                                        withId(R.id.customPanel),
                                        0)),
                        0),
                        isDisplayed()));
        editText6.check(matches(withText("ray")));

        ViewInteraction editText7 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.custom),
                                childAtPosition(
                                        withId(R.id.customPanel),
                                        0)),
                        0),
                        isDisplayed()));
        editText7.check(matches(withText("test131241rqw")));

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(android.R.id.button2), withText("CANCEL"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttonPanel),
                                        0),
                                2)));
        appCompatButton3.perform(scrollTo(), click());

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withId(R.id.collapsing_toolbar),
                                                3)),
                                1),
                        isDisplayed()));
        appCompatImageButton3.perform(click());

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.up_button),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.action_bar),
                                        0),
                                0),
                        isDisplayed()));
        appCompatImageView.perform(click());

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(R.id.title), withText("Settings"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.v7.view.menu.ListMenuItemView")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction recyclerView4 = onView(
                allOf(withId(R.id.list),
                        childAtPosition(
                                withClassName(is("android.widget.FrameLayout")),
                                0)));
        recyclerView4.perform(actionOnItemAtPosition(4, click()));

        ViewInteraction recyclerView5 = onView(
                allOf(withId(R.id.list),
                        childAtPosition(
                                withClassName(is("android.widget.FrameLayout")),
                                0)));
        recyclerView5.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction checkedTextView = onView(
                allOf(withId(android.R.id.text1),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        childAtPosition(
                                                withId(R.id.contentPanel),
                                                0)),
                                2),
                        isDisplayed()));
        checkedTextView.check(matches(isDisplayed()));

        DataInteraction appCompatCheckedTextView = onData(anything())
                .inAdapterView(allOf(withId(R.id.select_dialog_listview),
                        childAtPosition(
                                withId(R.id.contentPanel),
                                1)))
                .atPosition(2);
        appCompatCheckedTextView.perform(click());

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
