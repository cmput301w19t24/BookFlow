package com.example.bookflow;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.EditText;
import android.widget.TextView;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;




import static android.support.test.InstrumentationRegistry.getInstrumentation;

@RunWith(AndroidJUnit4.class)

public class SearchActivityTest extends ActivityTestRule<SearchActivity> {
    private Solo solo;
    public SearchActivityTest() {
        super(SearchActivity.class);
    }

    @Rule
    public ActivityTestRule<SearchActivity> rule =
            new ActivityTestRule<>(SearchActivity.class, false, true);

    @Before
    public void setUp() throws Exception{

        solo = new Solo(getInstrumentation(), rule.getActivity());
    }
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void checkUserSearch(){
        solo.assertCurrentActivity("Wrong Activity", SearchActivity.class);
        solo.enterText((EditText) solo.getView(R.id.searchText), "test1");
        solo.clickOnView(solo.getView(R.id.goSearch));
        solo.waitForText("test1");
        solo.waitForText("test1@ualberta.ca");
        solo.waitForText("1111");
    }


//    @Test
//    public void checkBookNameSearch(){
//        solo.assertCurrentActivity("Wrong Activity", SearchActivity.class);
//        solo.clickOnView(solo.getView(R.id.spinner));
//        solo.pressSpinnerItem(0,2);
//        solo.enterText((EditText) solo.getView(R.id.searchText), "Geo");
//        solo.clickOnView(solo.getView(R.id.goSearch));
//        solo.waitForText("9788711222102");
//        solo.waitForText("George's Marvelous Medicine");
//        solo.waitForText("Roald Dahl");
//    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}