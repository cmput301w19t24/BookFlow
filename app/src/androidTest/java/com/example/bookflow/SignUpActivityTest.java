package com.example.bookflow;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

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
    public void clickSignup(){
        LoginActivity activity = (LoginActivity) solo.getCurrentActivity();

        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        TextView signup = (TextView)solo.getView("signup");
        solo.clickOnView(signup);
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
    }

    @Test
    public void clickLogin(){
        LoginActivity activity = (LoginActivity) solo.getCurrentActivity();

        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.email));
        solo.clearEditText((EditText) solo.getView(R.id.password));
        solo.enterText((EditText) solo.getView(R.id.email), "shengyao@ualberta.ca");
        solo.enterText((EditText) solo.getView(R.id.password), "123456");
        Button loginbtn = (Button)solo.getView("login");
        solo.clickOnView(loginbtn);
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    @Test
    public void rememberLogin(){
        LoginActivity activity = (LoginActivity) solo.getCurrentActivity();
        solo.clearEditText((EditText) solo.getView(R.id.email));
        solo.clearEditText((EditText) solo.getView(R.id.password));
        solo.enterText((EditText) solo.getView(R.id.email), "shengyao@ualberta.ca");
        solo.enterText((EditText) solo.getView(R.id.password), "123456");
        Button loginbtn = (Button)solo.getView("login");
        solo.clickOnView(loginbtn);
        CheckBox checkBox = (CheckBox) solo.getView(R.id.checkBox);
        solo.clickOnView(checkBox);
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}