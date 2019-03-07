package com.example.bookflow;

import android.service.autofill.Dataset;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends BasicActivity {
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
    }

    @Override
    protected void onStart() {
        super.onStart();

        dbRef = FirebaseDatabase.getInstance().getReference();

        ValueEventListener userInfoListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String email = dataSnapshot.child("email").toString();
                String  phoneNum = dataSnapshot.child("phoneNumber").toString();
                String username = dataSnapshot.child("username").toString();
                String selfIntro = dataSnapshot.child("selfIntro").toString();
                // TODO: user image upload to storage and get it

                setupTextView(username, selfIntro, email, phoneNum);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("listener cancelled", databaseError.toException());
            }
        };
    }

    private void setupTextView(String name, String intro, String email, String phone) {
        TextView textView = findViewById(R.id.profileName);
        textView.setText(name);

        textView = findViewById(R.id.selfIntro);
        textView.setText(intro);

        textView = findViewById(R.id.emailToBeChange);
        textView.setText(email);

        textView = findViewById(R.id.phoneToBeChange);
        textView.setText(phone);
    }


}
