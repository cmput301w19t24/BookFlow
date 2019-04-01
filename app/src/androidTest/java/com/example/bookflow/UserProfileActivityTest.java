package com.example.bookflow;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserProfileActivityTest extends ActivityTestRule<UserProfileActivity>{
    FirebaseAuth mAuth;
    Solo solo;

    public UserProfileActivityTest() {
        super(UserProfileActivity.class);
    }

    @Rule
    public ActivityTestRule<UserProfileActivity> activityRule
            = new ActivityTestRule<>(UserProfileActivity.class);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), activityRule.getActivity());
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword("shengyao@ualberta.ca", "112233");
    }

    @Test
    public void checkListsValid() {
        /* click on three buttons and see if review list, offer list and request list appears or
         * disappears as expected */
        onView(withId(R.id.offer_switch)).perform(click());
        onView(withId(R.id.offer_switch)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        onView(withId(R.id.review_switch)).perform(click());
        onView(withId(R.id.review_switch)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void clickReviewItem(){
        ListView reviewlist = (ListView)solo.getView("reviewList");

        solo.waitForText("Shengyao");

        TextView review_switch = (TextView)solo.getView("review_switch");
        solo.clickOnView(review_switch);

        if (reviewlist.getCount()>0) {
            View v = reviewlist.getChildAt(0);
            TextView reviewername = (TextView)v.findViewById(R.id.reviewer_name);
            solo.clickInList(0);
            solo.assertCurrentActivity("Wrong Activity", UserProfileActivity.class);
            String reviewer = String.valueOf(reviewername.getText());
            solo.waitForText(reviewer,1, 2000);
        }
    }

    @Test
    public void clickOfferItem(){
        UserProfileActivity activity = (UserProfileActivity) solo.getCurrentActivity();
        solo.assertCurrentActivity("Wrong Activity", UserProfileActivity.class);

        solo.waitForText("Shengyao");

        TextView offer_switch = (TextView)solo.getView("offer_switch");
        solo.clickOnView(offer_switch);

        ListView offerlist = (ListView)solo.getView("offerList");
        if (offerlist.getCount()>0) {
            View v = offerlist.getChildAt(0);
            TextView bookname = (TextView)v.findViewById(R.id.utitle);
            solo.clickInList(0);
            solo.assertCurrentActivity("Wrong Activity", BookDetailActivity.class);
            String book_name = String.valueOf(bookname.getText());
            solo.waitForText(book_name,1, 2000);
        }
    }

    @Test
    public void checkProfileEdit() {
        /* update the user information and see if they are changed */
        onView(withId(R.id.editPersonInfo)).perform(click());

        onView(withId(R.id.edit_username)).perform(
                clearText(),
                typeText("Shengyao"),
                closeSoftKeyboard());

        onView(withId(R.id.edit_self_intro)).perform(
                clearText(),
                typeText("this is a test"),
                closeSoftKeyboard());

        onView(withId(R.id.edit_phone)).perform(
                clearText(),
                typeText("1112223456"),
                closeSoftKeyboard());

        onView(withId(R.id.confirm_button)).perform(click());

        onView(withId(R.id.profileName)).check(matches(withText("Shengyao")));
        onView(withId(R.id.selfIntro)).check(matches(withText("this is a test")));
        onView(withId(R.id.phoneToBeChange)).check(matches(withText("1112223456")));
    }
}