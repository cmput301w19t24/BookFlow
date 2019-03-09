package com.example.bookflow;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.regex.Pattern;


public class EditProfileActivity extends AppCompatActivity {
    private EditText username, email, selfIntro,phone;
    private ImageView profile;
    private Uri imageUri = null;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private DatabaseReference dbRef;
    private static final int PICK_FROM_ALBUM = 10;

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
                upload();
            }
        });

        Button button = findViewById(R.id.confirm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm(v);
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        ValueEventListener userInfoListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseUser user = mAuth.getCurrentUser();
                String userUid = user.getUid();
                String emailHint = "";
                String phoneHint = "";
                String nameHint = "";
                String introHint = "";

                for (DataSnapshot eachUser: dataSnapshot.getChildren()) {
                    String getUid = eachUser.child("uid").getValue().toString();
                    if (getUid.equals(userUid)) {
                        emailHint = eachUser.child("email").getValue().toString();
                        phoneHint = eachUser.child("phoneNumber").getValue().toString();
                        nameHint = eachUser.child("username").getValue().toString();
                        introHint = eachUser.child("selfIntro").getValue().toString();
                        break;
                    }
                }

                // TODO: user image upload to storage and get it

                username.setText(nameHint);
                selfIntro.setText(introHint);
                email.setText(emailHint);
                phone.setText(phoneHint);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("listener cancelled", databaseError.toException());
            }
        };

        dbRef.child("Users").addListenerForSingleValueEvent(userInfoListener);
    }

    public void test(View v) {
        return;
    }

    private void upload() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
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

        if (!valid)
            return ;

        FirebaseUser user = mAuth.getCurrentUser();
        dbRef.child("Users");
    }
}
