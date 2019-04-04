package com.example.bookflow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.bookflow.Model.InAppNotif;

public class InitActivity extends AppCompatActivity {

    static private InAppNotif notif = new InAppNotif();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
    }

    static public InAppNotif getNotif() {
        return notif;
    }
}
