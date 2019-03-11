package com.example.bookflow;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.w3c.dom.Text;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

public class BorrowListActivityTest extends ActivityTestRule<BorrowListActivity> {

    private Solo solo;
    private FirebaseAuth mAuth;


    public BorrowListActivityTest(){
        super(BorrowListActivity.class, false, true);
    }

    @Rule
    public ActivityTestRule<BorrowListActivity> rule =
            new ActivityTestRule<>(BorrowListActivity.class, false, true);
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
        BorrowListActivity activity = (BorrowListActivity) solo.getCurrentActivity();

        solo.assertCurrentActivity("Wrong Activity", BorrowListActivity.class);
        TextView myBooks = (TextView)solo.getView("title_mybook");
        solo.clickOnView(myBooks);
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    @Test
    public void clickItem(){
        BorrowListActivity activity = (BorrowListActivity) solo.getCurrentActivity();

        solo.assertCurrentActivity("Wrong Activity", BorrowListActivity.class);
        solo.clickInList(0);
        solo.assertCurrentActivity("Wrong Activity", BookDetailActivity.class);
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
