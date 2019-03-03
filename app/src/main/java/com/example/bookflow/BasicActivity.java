package com.example.bookflow;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.function.ToDoubleBiFunction;

public class BasicActivity extends AppCompatActivity {
    ImageButton mainPageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
    }

    public void clickMainPageButton(View v){
        if(!(this.getClass() == MainActivity.class)){
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    }

    public void clickSearchPageButton(View v){
        if(!(this.getClass() == SearchActivity.class)) {
            Intent intent = new Intent(this, SearchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    //Todo: add book
    /*public void clickAddButton(View v){
        Intent intent = new Intent(this, AddBookActivity.class);
        startActivity(intent);
    }
    */


    public void clickNotificationButton(View v){
        if(!(this.getClass() == NotificationActivity.class)) {
            Intent intent = new Intent(this, NotificationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }


    public void clickProfileButton(View v){
        if(!(this.getClass() == UserProfileActivity.class)) {
            Intent intent = new Intent(this, UserProfileActivity.class);
            startActivity(intent);
        }
    }

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
