/**
 * author: Yuhan Ye
 * date: 2019/3/11
 * version: 1.0
 */
package com.example.bookflow;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;

/**
 * Basic model Activity page
 * implement main page button, search page button, add button, notification page button and profile button
 * extended by most activity pages in our project
 */
public class BasicActivity extends AppCompatActivity {
    private ArrayList<Class> mActivityClasses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);

        mActivityClasses = new ArrayList<>();
        mActivityClasses.add(MainActivity.class);
        mActivityClasses.add(SearchActivity.class);
        mActivityClasses.add(AddBookActivity.class);
        mActivityClasses.add(NotificationActivity.class);
        mActivityClasses.add(UserProfileActivity.class);
    }

    /**
     * main page button click on method
     * @param v main page button view
     */
    public void clickMainPageButton(View v){
        if(this.getClass() != MainActivity.class){
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            slideAnimation(MainActivity.class);
        }

    }

    /**
     * search page button click on method
     * @param v main page button view
     */
    public void clickSearchPageButton(View v){
        if(this.getClass() != SearchActivity.class) {
            Intent intent = new Intent(this, SearchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            slideAnimation(SearchActivity.class);
        }
    }

    //Todo: add book
    public void clickAddButton(View v){
        if(this.getClass() != AddBookActivity.class) {
            Intent intent = new Intent(this, AddBookActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            slideAnimation(AddBookActivity.class);
        }
    }

    /**
     * notification page button click on method
     * @param v main page button view
     */
    public void clickNotificationButton(View v){
        if(this.getClass() != NotificationActivity.class) {
            Intent intent = new Intent(this, NotificationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            slideAnimation(NotificationActivity.class);
        }
    }

    /**
     * profile page button click on method
     * @param v main page button view
     */
    public void clickProfileButton(View v){
        if(this.getClass() != UserProfileActivity.class) {
            Intent intent = new Intent(this, UserProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            slideAnimation(UserProfileActivity.class);
        }
    }

    /**
     * select animation based on relative position of the buttons
     * @param targetClass
     */
    private void slideAnimation(Class targetClass) {
        int currClassIdx = mActivityClasses.indexOf(this.getClass());
        int targetClassIdx = mActivityClasses.indexOf(targetClass);

        if (currClassIdx < targetClassIdx) {
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        } else {
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        }
    }

}
