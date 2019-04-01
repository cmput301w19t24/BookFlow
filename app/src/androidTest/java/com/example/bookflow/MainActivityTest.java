/**
 * MainActivityTest Class
 *
 * Copyright 2019 Shengyao Lu
 *
 * @author shengyao
 * @version 1.0
 * @created 2019-03-01
 */

package com.example.bookflow;

import android.app.Activity;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class MainActivityTest extends ActivityTestRule<MainActivity> {

    private Solo solo;
    private FirebaseAuth mAuth;


    public MainActivityTest(){
        super(MainActivity.class, false, true);
    }

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, false, true);
    @Before
    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), rule.getActivity());
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword("shengyao@ualberta.ca", "123456");
    }
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void clickMyBorrows(){
        MainActivity activity = (MainActivity) solo.getCurrentActivity();

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        TextView myBorrows = (TextView)solo.getView("title_myborrow");
        solo.clickOnView(myBorrows);
    }

    @Test
    public void clickItem(){
        MainActivity activity = (MainActivity) solo.getCurrentActivity();

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
//        onView(withId(R.id.offer_switch)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        ListView booklist = (ListView)solo.getView("myBookList");
        if (booklist.getVisibility() == ListView.VISIBLE) {
            solo.clickInList(0);
        }

        solo.assertCurrentActivity("Wrong Activity", BookDetailActivity.class);
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
