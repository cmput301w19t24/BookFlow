package com.example.bookflow;

import android.support.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Rule;

public class MainActivityTest extends ActivityTestRule<MainActivity> {

    private Solo solo;
    public MainActivityTest(){
        super(MainActivity.class, false, true);
    }

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, false, true);




}
