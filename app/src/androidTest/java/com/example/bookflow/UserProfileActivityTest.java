package com.example.bookflow;

import android.content.Intent;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserProfileActivityTest extends ActivityTestRule<UserProfileActivity>{
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public UserProfileActivityTest() {
        super(UserProfileActivity.class);
        mAuth.signInWithEmailAndPassword("jinming4@ualberta.ca", "112233");
    }

    @Rule
    public ActivityTestRule<UserProfileActivity> activityRule
            = new ActivityTestRule<>(UserProfileActivity.class);
    @Test
    public void checkListsValid() {
        /* click on three buttons and see if review list, offer list and request list appears or
         * disappears as expected */
        onView(withId(R.id.offer_switch)).perform(click());
        onView(withId(R.id.offer_switch)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        onView(withId(R.id.request_switch)).perform(click());
        onView(withId(R.id.request_switch)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        onView(withId(R.id.review_switch)).perform(click());
        onView(withId(R.id.review_switch)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void checkProfileEdit() {
        onView(withId(R.id.editPersonInfo)).perform(click());

        onView(withId(R.id.edit_username)).perform(
                clearText(),
                typeText("jimmy"),
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

        onView(withId(R.id.profileName)).check(matches(withText("jimmy")));
        onView(withId(R.id.selfIntro)).check(matches(withText("this is a test")));
        onView(withId(R.id.phoneToBeChange)).check(matches(withText("1112223456")));
    }
}