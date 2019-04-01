/**
 * LoginActivity Class
 *
 * Copyright 2019 Shengyao Lu
 *
 * @author shengyao
 * @version 1.0
 * @created 2019-03-01
 */

package com.example.bookflow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

/**
 * Handles login functionality
 */
public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private TextView signup;
    private Button login;
    private FirebaseAuth mAuth;
    private CheckBox checkBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    private ProgressBar progressBar;

    /**
     * on create activity
     * @param savedInstanceState saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.login);
        signup = (TextView)findViewById(R.id.signup);
        checkBox = (CheckBox)findViewById(R.id.checkBox);
        progressBar = findViewById(R.id.login_progress_bar);
        progressBar.setVisibility(View.GONE);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        // check if remembered from last time
        checkRemem();
        // if login button is clicked
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loggedIn();
            }
        });
        // if sign up button is clicked
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            }
        });

    }

    /**
     * check if email and password is remembered from last time
     */
    private void checkRemem() {
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        /**
         * if yes, set texts
         */
        if (saveLogin == true) {
            email.setText(loginPreferences.getString("email", ""));
            password.setText(loginPreferences.getString("password", ""));
            checkBox.setChecked(true);
        }
    }

    /**
     * login method
     */
    private void loggedIn() {
        // regex
        boolean valid = true;
        String emailPat = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$";
        if (!Pattern.matches(emailPat, email.getText().toString())) {
            email.setError("Invalid email address");
            email.setText("");
            valid = false;
        }
        String passwordPat = "^(.{6,20})$";
        if (!Pattern.matches(passwordPat, password.getText().toString())) {
            password.setError("password should more than 6 and less than 20 characters");
            password.setText("");
            valid = false;
        }
        if (!valid)
            return;

        // try to sign in
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            if (checkBox.isChecked()) {
                                loginPrefsEditor.putBoolean("saveLogin", true);
                                loginPrefsEditor.putString("email", email.getText().toString());
                                loginPrefsEditor.putString("password", password.getText().toString());
                                loginPrefsEditor.commit();
                            } else {
                                loginPrefsEditor.clear();
                                loginPrefsEditor.commit();
                            }
                            Toast.makeText(LoginActivity.this, getString(R.string.logged_in), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(LoginActivity.this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                        }
                    }
        });
    }
}
