package com.example.bookflow;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bookflow.Model.Review;
import com.example.bookflow.Model.ReviewList;
import com.example.bookflow.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class UserProfileActivity extends BasicActivity {
    private DatabaseReference dbRef;
    private String email, phoneNum, username, selfIntro;
    private ReviewList reviewList;
    private ListView reviewListView;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
    }

    @Override
    protected void onStart() {
        super.onStart();
        reviewListView = findViewById(R.id.reviewList);
        dbRef = FirebaseDatabase.getInstance().getReference();


        ValueEventListener userInfoListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseUser user = mAuth.getCurrentUser();
                String userEmail = user.getEmail();

                for (DataSnapshot eachUser: dataSnapshot.getChildren()) {
                    String getEmail = eachUser.child("email").getValue().toString();
                    if (getEmail.equals(userEmail)) {
                        email = userEmail;
                        phoneNum = eachUser.child("phoneNumber").getValue().toString();
                        username = eachUser.child("username").getValue().toString();
                        selfIntro = eachUser.child("selfIntro").getValue().toString();
                    }
                }

                // TODO: user image upload to storage and get it

                setupTextView(username, selfIntro, email, phoneNum);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("listener cancelled", databaseError.toException());
            }
        };

        ValueEventListener reviewsListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot eachReview: dataSnapshot.getChildren()){
                    if (username.equals(eachReview.child("reviewee").toString())){

                        /*
                        TODO: not able to initialize a review with User reviewer and reviewee
                        TODO: option 1: users in Review class change to FirebaseUser
                        TODO: option 2: users in Review class only store name or uid or email
                        */
                        UUID uuid = UUID.fromString(eachReview.child("uuid").getValue().toString());
                        int rating = Integer.getInteger(eachReview.child("rating").getValue().toString());
                        String comments = eachReview.child("comments").getValue().toString();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("listener cancelled", databaseError.toException());
            }
        };


    dbRef.child("Users").addListenerForSingleValueEvent(userInfoListener);
    dbRef.child("Reviews").addListenerForSingleValueEvent(reviewsListener);
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
