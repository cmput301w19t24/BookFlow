package com.example.bookflow;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.EditText;
import android.widget.ImageView;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Rule;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddBookActivityTest extends ActivityTestRule<AddBookActivity> {

    private Solo solo;

    public AddBookActivityTest() {
        super(AddBookActivity.class);
    }

    @Rule
    public ActivityTestRule<AddBookActivity> rule =
            new ActivityTestRule<>(AddBookActivity.class, false, false);

    @Before
    public void setUp() throws Exception{

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

        onView(withText("hello")).check(matches(isDisplayed()));

    }

}