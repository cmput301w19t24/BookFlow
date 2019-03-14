/**
 * author: Yuhan Ye
 * date: 2019/3/11
 * version: 1.0
 */
package com.example.bookflow;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.function.ToDoubleBiFunction;

/**
 * Basic model Activity page
 * implement main page button, search page button, add button, notification page button and profile button
 * extended by most activity pages in our project
 */
public class BasicActivity extends AppCompatActivity {
    ImageButton mainPageButton;
    private FirebaseAuth mAuth;
    boolean initialCall = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);

        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference notificationRef =  FirebaseDatabase.getInstance().getReference("Notifications").child(userId);


        ValueEventListener notificationListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (initialCall) {
                    initialCall = false;
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Notification Received",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
                
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("cancelled", databaseError.toException());
            }
        };
        notificationRef.addValueEventListener(notificationListener);
    }

    /**
     * main page button click on method
     * @param v main page button view
     */
    public void clickMainPageButton(View v){
        if(!(this.getClass() == MainActivity.class)){
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    }

    /**
     * search page button click on method
     * @param v main page button view
     */
    public void clickSearchPageButton(View v){
        if(!(this.getClass() == SearchActivity.class)) {
            Intent intent = new Intent(this, SearchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    //Todo: add book
    public void clickAddButton(View v){
        if(!(this.getClass() == AddBookActivity.class)) {
            Intent intent = new Intent(this, AddBookActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    /**
     * notification page button click on method
     * @param v main page button view
     */
    public void clickNotificationButton(View v){
        if(!(this.getClass() == NotificationActivity.class)) {
            Intent intent = new Intent(this, NotificationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    /**
     * profile page button click on method
     * @param v main page button view
     */
    public void clickProfileButton(View v){
        if(!(this.getClass() == UserProfileActivity.class)) {
            Intent intent = new Intent(this, UserProfileActivity.class);
            startActivity(intent);
        }
    }

    /**
     * This function is to get current activity.
     * User won't jump to a new current page if he is already in that page
     * @return current activity
     */
    /*https://blog.csdn.net/linh0911111026/article/details/78895253 */
    public static Activity getCurrentActivity () {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(
                    null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            Map activities = (Map) activitiesField.get(activityThread);
            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    Activity activity = (Activity) activityField.get(activityRecord);
                    return activity;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
