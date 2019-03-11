package com.example.bookflow;


import android.app.Activity;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;




import static android.support.test.InstrumentationRegistry.getInstrumentation;

@RunWith(AndroidJUnit4.class)

public class BasicActivityTest  extends ActivityTestRule<BasicActivity> {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Solo solo;

    public BasicActivityTest() {
        super(BasicActivity.class);
        mAuth.signInWithEmailAndPassword("jinming4@ualberta.ca","112233");
    }


    @Rule
    public ActivityTestRule<BasicActivity> rule =
            new ActivityTestRule<>(BasicActivity.class, false, true);

    @Before
    public void setUp() throws Exception{

        solo = new Solo(getInstrumentation(), rule.getActivity());
    }
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void checkMainPage(){
        solo.assertCurrentActivity("Wrong Activity", BasicActivity.class);
        solo.clickOnView(solo.getView(R.id.main_page_button));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    @Test
    public void checkSearchPage(){
        solo.assertCurrentActivity("Wrong Activity", BasicActivity.class);
        solo.clickOnView(solo.getView(R.id.search_button));
        solo.assertCurrentActivity("Wrong Activity", SearchActivity.class);
    }

    @Test
    public void checkAddBookPage(){
        solo.assertCurrentActivity("Wrong Activity", BasicActivity.class);
        solo.clickOnView(solo.getView(R.id.add_button));
        solo.assertCurrentActivity("Wrong Activity", AddBookActivity.class);
    }

    @Test
    public void checkNotificationPage(){
        solo.assertCurrentActivity("Wrong Activity", BasicActivity.class);
        solo.clickOnView(solo.getView(R.id.notification_button));
        solo.assertCurrentActivity("Wrong Activity", NotificationActivity.class);
    }

    @Test
    public void checkProfilePage(){
        solo.assertCurrentActivity("Wrong Activity", BasicActivity.class);
        solo.clickOnView(solo.getView(R.id.profile_button));
        solo.assertCurrentActivity("Wrong Activity", UserProfileActivity.class);
    }


    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
