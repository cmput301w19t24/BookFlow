package com.example.bookflow;

import static org.junit.Assert.*;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import android.support.test.runner.AndroidJUnit4;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(AndroidJUnit4.class)
public class AddBookActivityTest extends ActivityTestRule<AddBookActivity> {
    private Solo solo;

    public AddBookActivityTest() {
        super(AddBookActivity.class, false, true);
    }

    @Rule
    public ActivityTestRule<AddBookActivity> rule =
            new ActivityTestRule<>(AddBookActivity.class, false, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), rule.getActivity());
    }

    @Test
    public void enterInfo() {
        AddBookActivity activity = (AddBookActivity) solo.getCurrentActivity();

        solo.enterText((EditText) solo.getView(R.id.add_book_title_et), "Solo Test Book");
        solo.enterText((EditText) solo.getView(R.id.add_book_author_name_et), "Solo");
        solo.enterText((EditText) solo.getView(R.id.add_book_isbn_et), "7010");
        solo.enterText((EditText) solo.getView(R.id.add_book_description_et), "Solo Test Book lalaba labala hei");

        ImageView addbook = (ImageView) solo.getView("add_book_save_iv");
        solo.clickOnView(addbook);

    }

}