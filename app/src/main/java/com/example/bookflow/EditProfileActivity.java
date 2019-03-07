package com.example.bookflow;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

import java.util.regex.Pattern;

public class EditProfileActivity extends AppCompatActivity {
    private EditText username, email, selfIntro,phone;
    private ImageView profile;
    private Uri imageUri = null;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profile = findViewById(R.id.edit_profile_img);
        username = findViewById(R.id.edit_username);
        email = findViewById(R.id.edit_email);
        phone = findViewById(R.id.edit_phone);

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
    }

    /**
     * The onClick method for button "CONFIRM"
     * It checks if each input is in correct format then call update
     * @param view onClick method needed
     */
    public void confirm(View view) {
        String usernameStr = username.getText().toString();
        String introStr = selfIntro.getText().toString();
        String emailStr = email.getText().toString();
        String phoneStr = phone.getText().toString();

        boolean valid = true;

        // check if user name is entered in correct format
        String usernamePat = "^([a-z0-9]{3,20})$";
        if (!Pattern.matches(usernamePat, usernameStr)) {
            username.setError("username should more 6 and less than 20 characters with only letters or numbers");
            username = null;
            valid = false;
        }

        // check if self introduction is valid
        if (introStr.length() > 20) {
            selfIntro.setError("self introduction cannot exceed 20 characters");
            selfIntro = null;
            valid = false;
        }

        // check if email is valid
        String emailPat = "[0-9a-z]+@[0-9a-z]+.[a-z]+";
        if (!Pattern.matches(emailPat, email.getText().toString())) {
            email.setError("Invalid email address");
            email = null;
            valid = false;
        }

        // check if phone is valid
        String phonePat = "[0-9]+";
        if (!Pattern.matches(phonePat, phone.getText().toString())) {
            phone.setError("Invalid phone number");
            phone = null;
            valid = false;
        }

        if (!valid) {return;}

        // TODO: connect to firebase and update the user information


    }

    private void update(){

    }
}
