package com.example.bookflow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void loggedIn(View loggedInView) {
        Intent intent_loggedIn = new Intent(this, MainActivity.class);
        startActivity(intent_loggedIn);
    }

    public void signUp(View signUpView) {
        Intent intent_signUp = new Intent(this, SignUpActivity.class);
        startActivity(intent_signUp);
    }
}
