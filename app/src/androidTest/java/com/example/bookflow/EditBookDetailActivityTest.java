package com.example.bookflow;

import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.view.menu.ActionMenuItemView;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.bookflow.Util.FirebaseIO;
import com.google.firebase.auth.FirebaseAuth;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Rule;

import static android.support.test.InstrumentationRegistry.getInstrumentation;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class EditBookDetailActivityTest extends ActivityTestRule<EditBookDetailActivity> {

    private Solo solo;
    private FirebaseAuth mAuth;
    private FirebaseIO mFirebaseIO;

    public EditBookDetailActivityTest() {
        super(EditBookDetailActivity.class);
        mFirebaseIO = FirebaseIO.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword("shengyao@ualberta.ca", "112233");
    }

    @Rule
    public ActivityTestRule<EditBookDetailActivity> rule =
            new ActivityTestRule<>(EditBookDetailActivity.class, false, false);

    @Before
    public void setUp() throws Exception{

        Intent intent = new Intent();
        intent.putExtra("bookid", "-LbPJ2lKb7u3P__0BGH4");

        rule.launchActivity(intent);
        solo = new Solo(getInstrumentation(), rule.getActivity());

    }

    @Test
    public void editBook() {
        solo.assertCurrentActivity("wrong activity!", EditBookDetailActivity.class);

        solo.enterText((EditText) solo.getView(R.id.edit_book_title_et), "Solo Test Book");
        solo.enterText((EditText) solo.getView(R.id.edit_book_author_name_et), "Solo");
        solo.enterText((EditText) solo.getView(R.id.edit_book_isbn_et), "7010");
        solo.enterText((EditText) solo.getView(R.id.edit_book_description_it), "Solo Test Book lalaba labala hei");

        ActionMenuItemView editBook = (ActionMenuItemView) solo.getView("action_edit_book_save");
        solo.clickOnView(editBook);

    }

    @Test
    public void enterScan() {
        solo.assertCurrentActivity("wrong activity!", EditBookDetailActivity.class);

        solo.clickOnButton("scan");
        solo.assertCurrentActivity("wrong activity", ScanActivity.class);

        solo.goBack();

        solo.assertCurrentActivity("wrong activity!", EditBookDetailActivity.class);

    }
}