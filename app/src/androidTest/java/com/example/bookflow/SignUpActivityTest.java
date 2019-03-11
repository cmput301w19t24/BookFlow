package com.example.bookflow;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;
import android.widget.Button;
import android.widget.EditText;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.UUID;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

public class SignUpActivityTest extends ActivityTestRule<SignUpActivity> {

    private Solo solo;

    public SignUpActivityTest(){
        super(SignUpActivity.class, false, true);
    }

    @Rule
    public ActivityTestRule<SignUpActivity> rule =
            new ActivityTestRule<>(SignUpActivity.class, false, true);
    @Before
    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), rule.getActivity());
    }
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void clickSignUpFail(){
        SignUpActivity activity = (SignUpActivity) solo.getCurrentActivity();
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);

        // generate random email address
        String uuid = UUID.randomUUID().toString();
        String generatedString = uuid.replace("-", "").substring(0,8);

        // incomplete information filled
        solo.enterText((EditText) solo.getView(R.id.email), generatedString+"@ualberta.ca");
        solo.enterText((EditText) solo.getView(R.id.password), "123456");
        solo.enterText((EditText) solo.getView(R.id.repassword), "1345690");
        Button signup = (Button)solo.getView("signup");
        solo.clickOnView(signup);

        // shouldn't successfully sign up, and should remain in this page
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
    }

    @Test
    public void clickSignUp(){
        SignUpActivity activity = (SignUpActivity) solo.getCurrentActivity();
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);

        // generate email address
        String uuid = UUID.randomUUID().toString();
        String generatedString = uuid.replace("-", "").substring(0,6);

        // fill in information
        solo.enterText((EditText) solo.getView(R.id.email), generatedString+"@ualberta.ca");
        solo.enterText((EditText) solo.getView(R.id.password), "123456");
        solo.enterText((EditText) solo.getView(R.id.repassword), "123456");
        solo.enterText((EditText) solo.getView(R.id.id), "testintent");
        solo.enterText((EditText) solo.getView(R.id.phone), "780456890");

        // click on sign up button
        Button signup = (Button)solo.getView("signup");
        solo.clickOnView(signup);
        solo.waitForText("Signed Up",1,10000);

        // successfully signed up
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
