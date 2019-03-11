package com.example.bookflow;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;


public class mainTest {

//    public mainTest() {}

    @Rule
    public ActivityTestRule<MainActivity> intentsTestRule =
            new ActivityTestRule<>(MainActivity.class);




}
