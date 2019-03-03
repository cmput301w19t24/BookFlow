package com.example.bookflow;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.function.ToDoubleBiFunction;

public class BasicActivity extends AppCompatActivity {
    ImageButton mainPageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        mainPageButton = (ImageButton)findViewById(R.id.main_page_button);
    }

    public void clickMainPageButton(View v){
        mainPageButton.setBackgroundColor(Color.GRAY);
        mainPageButton.setBackgroundColor(Color.WHITE);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void clickSearchPageButton(View v){
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    //Todo: add book

    /*public void clickAddButton(View v){
        Intent intent = new Intent(this, AddBookActivity.class);
        startActivity(intent);
    }
    */

    //Todo: notification
   /* public void clickNotificationButton(View v){
        Intent intent = new Intent(this, NotificationActivity.class);
        startActivity(intent);
    }
    */

    public void clickProfileButton(View v){
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }



}
