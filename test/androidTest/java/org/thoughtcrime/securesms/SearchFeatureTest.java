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

import static android.support.test.InstrumentationRegistry.getInstrumentation;
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
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SearchFeatureTest {

    @Rule
    public ActivityTestRule<ConversationListActivity> mActivityTestRule = new ActivityTestRule<>(ConversationListActivity.class);

    @Test
    public void searchFeatureTest() {

        //START OF THE REGISTRATION PROCESS
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.number), withText("5149912693"),//Step 1. entering phone number
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.registration_container),
                                        1),
                                2)));
        appCompatEditText.perform(scrollTo(), replaceText("(514) 991-2693"));

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.number), withText("(514) 991-2693"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.registration_container),
                                        1),
                                2),
                        isDisplayed()));
        appCompatEditText2.perform(closeSoftKeyboard());

        ViewInteraction circularProgressButton = onView(
                allOf(withId(R.id.registerButton), withText("Register"),// Step 2. click on the register button
                        childAtPosition(
                                allOf(withId(R.id.registration_container),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                2)),
                                2)));
        circularProgressButton.perform(scrollTo(), click());

        ViewInteraction circularProgressButton2 = onView(
                allOf(withId(R.id.finish_button), withText("FINISH"),// Step 3. click on the finish button
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.container),
                                        2),
                                0),
                        isDisplayed()));
        circularProgressButton2.perform(click());
        //END OF THE REGISTRATION PROCESS

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------

        //START OF CONTACT INVITATION PROCESS
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());// Step 1. open the menu located at the top right corner of the main screen

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.title), withText("Invite friends"),// Step 2. click on the "Invite friends" option in the menu
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.v7.view.menu.ListMenuItemView")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.sms_button), withText("Choose contacts"), //Step 3. click on Choose contacts
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                1)));
        appCompatButton3.perform(scrollTo(), click());



        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.search_view),
                        childAtPosition(
                                allOf(withId(R.id.toggle_container),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("5149912693"), closeSoftKeyboard()); //Step 4. enter the contact number

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recycler_view), //Step 5. select the contact
                        childAtPosition(
                                withId(R.id.swipe_refresh),
                                0)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.send_sms_button), withText("SEND SMS TO 1 FRIEND"), //Step 6. click on the "SEND SMS TO 1 FRIEND" button at the bottom of the invitation screen
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.sms_send_frame),
                                        2),
                                1),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(android.R.id.button1), withText("Yes"),// Step 7. click yes
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttonPanel),
                                        0),
                                3)));
        appCompatButton5.perform(scrollTo(), click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Navigate up"),//Step 8. return to the main screen
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());
        //END OF THE CONTACT INVITATION PROCESS

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------

        //START OF THE SEARCH FEATURE PROCESS
        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.list),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                1)));
        recyclerView2.perform(actionOnItemAtPosition(0, click()));// Step 1. select a conversation


        ViewInteraction composeText = onView(
                allOf(withId(R.id.embedded_text_editor), withContentDescription("Message composition"),
                        childAtPosition(
                                allOf(withId(R.id.compose_bubble),
                                        childAtPosition(
                                                withClassName(is("android.widget.FrameLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        composeText.perform(click());// Step 2. click on the text field at the bottom of the conversation screen

        ViewInteraction composeText2 = onView(
                allOf(withId(R.id.embedded_text_editor), withContentDescription("Message composition"),
                        childAtPosition(
                                allOf(withId(R.id.compose_bubble),
                                        childAtPosition(
                                                withClassName(is("android.widget.FrameLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        composeText2.perform(replaceText("Hi"), closeSoftKeyboard());// Step 3. enter an arbitrary message (in this case, it is "Hi")

        ViewInteraction sendButton = onView(
                allOf(withId(R.id.send_button), withContentDescription("Signal"),
                        childAtPosition(
                                allOf(withId(R.id.button_toggle),
                                        childAtPosition(
                                                withId(R.id.bottom_panel),
                                                1)),
                                1),
                        isDisplayed()));
        sendButton.perform(click()); //Step 4. click on the send button located to the right of the text field


        //ASSERTION: assert that the "Search" icon is present at the top of the conversation screen
        ViewInteraction imageView = onView(
                allOf(withId(R.id.search_button), withContentDescription("Search"),
                        childAtPosition(
                                allOf(withId(R.id.search_bar),
                                        childAtPosition(
                                                withId(R.id.action_search_message),
                                                0)),
                                0),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.search_button), withContentDescription("Search"), //Step 6. click on the "Search" icon
                        childAtPosition(
                                allOf(withId(R.id.search_bar),
                                        childAtPosition(
                                                withId(R.id.action_search_message),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageView.perform(click());

        //ASSERTION: assert that the search field is active after the "Search" icon is pressed
        ViewInteraction editText = onView(
                allOf(withId(R.id.search_src_text), withText("Search…"),
                        childAtPosition(
                                allOf(withId(R.id.search_plate),
                                        childAtPosition(
                                                withId(R.id.search_edit_frame),
                                                0)),
                                0),
                        isDisplayed()));
        editText.check(matches(withText("Search…")));

    }

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------
    //A method to locate the widgets' position on the screen
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
