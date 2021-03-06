/**
 * author: Yuhan Ye
 * date: 2019/3/11
 * version: 1.0
 */
package com.example.bookflow;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.bookflow.Model.InAppNotif;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Basic model Activity page
 * implement main page button, search page button, add button, notification page button and profile button
 * extended by most activity pages in our project
 */
public class BasicActivity extends AppCompatActivity {
    private ArrayList<Class> mActivityClasses;
    private String uid;
    private InAppNotif notif = InitActivity.getNotif();

    /**
     * on create
     * @param savedInstanceState saved instance state
     */
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

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        try {
            Query query = FirebaseDatabase.getInstance().getReference().child("InAppNotif").child(uid);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    InAppNotif notif1 = dataSnapshot.getValue(InAppNotif.class);
                    if (null != notif1) {
                        notif.setFirstIn(notif1.isFirstIn());
                        notif.setNotif_count(notif1.getNotif_count());
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    notif = InitActivity.getNotif();
                }
            });
        } catch (Exception e) {
            notif = InitActivity.getNotif();
        }
    }

    /**
     * on start
     */
    @Override
    protected void onStart() {
        super.onStart();
        handleNotif();
    }

    /**
     * handle a notification so that all activities can be notified
     */
    private void handleNotif() {
        FirebaseDatabase database =FirebaseDatabase.getInstance();
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ValueEventListener notificationListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long tmp = dataSnapshot.getChildrenCount();
                // grab the number of count when enter the activity for the first time
                if (notif.isFirstIn()) {
                    notif.setNotif_count(tmp);
                    notif.setFirstIn(false);
                    InitActivity.pushData(uid);
                } else if (notif.getNotif_count() < tmp) {
                    // set notification count
                    TextView count_text = findViewById(R.id.basic_noti_count);
                    count_text.setVisibility(View.VISIBLE);
                    count_text.setText(String.valueOf(tmp-notif.getNotif_count()));
                } else if (notif.getNotif_count() > tmp) {
                    notif.setNotif_count(tmp);
                    InitActivity.pushData(uid);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                notif = InitActivity.getNotif();
            }
        };
        database.getReference().child("Notifications").child(uid).addValueEventListener(notificationListener);
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

    public void clickAddButton(View v){
        Intent intent = new Intent(this, AddBookActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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
    public void clickProfileButton(View v) {
        // grab the uid
        // if equal to current uid, don't allow the user to re-enter current activity
        String passuid = String.valueOf(uid);
        try {
            Class<?> c = this.getClass();
            Field f = this.getClass().getField("uid");
            f.setAccessible(true);
            passuid = String.valueOf((String)f.get(this));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if(this.getClass() != UserProfileActivity.class || !passuid.equals(uid)) {
            Intent intent = new Intent(this, UserProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            slideAnimation(UserProfileActivity.class);
        }
    }

    /**
     * select animation based on relative position of the buttons
     * @param targetClass destination activity class
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
