package com.example.bookflow;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

public class RequestActivityTest extends ActivityTestRule<SearchActivity> {
    private Solo solo;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public RequestActivityTest() {
        super(SearchActivity.class);
        mAuth.signInWithEmailAndPassword("shengyao@ualberta.ca", "123456");
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
    public void searchAndRequestBook(){
        solo.assertCurrentActivity("Wrong Activity", SearchActivity.class);
        View view1 = solo.getView(Spinner.class, 0);
        solo.clickOnView(view1);
        solo.scrollToTop();
        solo.clickOnText("search by book name");
        solo.enterText((EditText) solo.getView(R.id.searchText), "Geo");
        solo.clickOnView(solo.getView(R.id.goSearch));
        solo.waitForText("9788711222102");
        solo.waitForText("George's Marvelous Medicine");
        solo.waitForText("Roald Dahl");
        solo.clickInRecyclerView(0);
        solo.assertCurrentActivity("Wrong Activity", BookDetailActivity.class);
        solo.clickOnView(solo.getView(R.id.requestButton));
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
