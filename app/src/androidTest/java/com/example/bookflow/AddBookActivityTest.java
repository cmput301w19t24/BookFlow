package com.example.bookflow;

import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Rule;

import static android.support.test.InstrumentationRegistry.getInstrumentation;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddBookActivityTest extends ActivityTestRule<AddBookActivity> {

    private Solo solo;
    private FirebaseAuth mAuth;

    public AddBookActivityTest() {
        super(AddBookActivity.class);
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword("shengyao@ualberta.ca", "123456");
    }

    @Rule
    public ActivityTestRule<AddBookActivity> rule =
            new ActivityTestRule<>(AddBookActivity.class, false, false);

    @Before
    public void setUp() throws Exception{

        Intent intent = new Intent();
        rule.launchActivity(intent);
        solo = new Solo(getInstrumentation(), rule.getActivity());

    }

    @Test
    public void enterInfo() {
        solo.assertCurrentActivity("wrong activity!", AddBookActivity.class);

        solo.enterText((EditText) solo.getView(R.id.add_book_title_et), "Solo Test Book");
        solo.enterText((EditText) solo.getView(R.id.add_book_author_name_et), "Solo");
        solo.enterText((EditText) solo.getView(R.id.add_book_isbn_et), "7010");
        solo.enterText((EditText) solo.getView(R.id.add_book_description_et), "Solo Test Book lalaba labala hei");

        ImageView addbook = (ImageView) solo.getView("add_book_save_iv");
        solo.clickOnView(addbook);

    }

    @Test
    public void enterScan() {
        solo.assertCurrentActivity("wrong activity!", AddBookActivity.class);

        solo.clickOnButton(0);
        solo.assertCurrentActivity("wrong activity", ScanActivity.class);

        solo.goBack();

        solo.assertCurrentActivity("wrong activity!", AddBookActivity.class);

    }

}