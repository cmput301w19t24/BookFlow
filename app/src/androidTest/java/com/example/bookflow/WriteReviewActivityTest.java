package com.example.bookflow;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class WriteReviewActivityTest extends ActivityTestRule<WriteReviewActivity>{
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public WriteReviewActivityTest() {
        super(WriteReviewActivity.class);
        mAuth.signInWithEmailAndPassword("jinming4@ualberta.ca", "112233");
    }

    @Rule
    public ActivityTestRule<WriteReviewActivity> activityRule
            = new ActivityTestRule<>(WriteReviewActivity.class);

    @Test
    public void checkInputCancel() {
        onView(withId(R.id.review_Paragraph)).perform(
                clearText(),
                typeText("This is test review!"),
                closeSoftKeyboard()
        );
        onView(withId(R.id.reviewCancel)).perform(click());
    }

    @Test
    public void checkInputRatingDone() {
        onView(withId(R.id.review_Paragraph)).perform(
                clearText(),
                typeText("This is test review!"),
                closeSoftKeyboard()
        );

        onView(withId(R.id.seekBar)).perform(
                swipeRight()
        );

        onView(withId(R.id.show_rating)).check(matches(withText("5")));

        onView(withId(R.id.reviewDone)).perform(click());
    }

}