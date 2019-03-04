package com.example.bookflow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SignUpActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void signedUpIn(View signedUpView) {
        Intent intent_signedUp = new Intent(this, MainActivity.class);
        startActivity(intent_signedUp);
    }
}
