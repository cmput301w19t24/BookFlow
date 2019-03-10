package com.example.bookflow;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bookflow.Model.Review;
import com.example.bookflow.Model.ReviewList;
import com.example.bookflow.Model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.UUID;

/**
 * This class is for user profile page
 * It fills all information of a user profile, including
 * name, phone, self-intro, email, books offered and review list
 * it also provides a button, an access to edit profile page
 */
public class UserProfileActivity extends BasicActivity {
    private DatabaseReference dbRef;
    private String email, phoneNum, username, selfIntro, uid, photo;
    private ReviewList reviewList = new ReviewList();
    private ListView reviewListView;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
    }


    /**
     * onStart method get all needed data in this page from firebase
     * and convert them to strings, integers, users which we can directly use
     */
    @Override
    protected void onStart() {
        super.onStart();
        reviewListView = (ListView) findViewById(R.id.reviewList);
        dbRef = FirebaseDatabase.getInstance().getReference();

//        Intent intent = getIntent();
//        String message = intent.getStringExtra(SearchActivity.EXTRA_MESSAGE);
//        if (message != null) {
//            ImageView imageButton = findViewById(R.id.editPersonInfo);
//            imageButton.setEnabled(false);
//            imageButton.setVisibility(View.INVISIBLE);
//            uid = message;
//        } else  {
//            FirebaseUser user = mAuth.getCurrentUser();
//            uid = user.getUid();
//        }
//        setUpImageView();


        // get user information from the database
        ValueEventListener userInfoListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                DataSnapshot targetUser = dataSnapshot.child(uid);
                email = targetUser.child("email").getValue().toString();
                phoneNum = targetUser.child("phoneNumber").getValue().toString();
                username = targetUser.child("username").getValue().toString();
                selfIntro = targetUser.child("selfIntro").getValue().toString();


                setupTextView(username, selfIntro, email, phoneNum);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("listener cancelled", databaseError.toException());
            }
        };

        // get all reviews from database
        ValueEventListener reviewsListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot eachReview: dataSnapshot.getChildren()){
                    if (uid.equals(eachReview.child("reviewee").getValue().toString())){
                        prepareReviewList(eachReview);
                    }
                }
                ArrayList<String> stringList = reviewList.toStringArray();
                if (stringList.size() == 0) {
                    stringList.add("No Review");
                }
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(UserProfileActivity.this,
                        R.layout.review_list_adapter, stringList);
                reviewListView.setAdapter(adapter1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("listener cancelled", databaseError.toException());
            }
        };

        // activate listeners
        dbRef.child("Users").addListenerForSingleValueEvent(userInfoListener);
        dbRef.child("Reviews").addListenerForSingleValueEvent(reviewsListener);
    }

    /**
     * This function prepares a review and add it to review list
     * @param eachReview a review object from firebase
     */
    private void prepareReviewList(@NonNull DataSnapshot eachReview) {
        // prepare uuid, rating, comments
        String test1 = eachReview.child("rating").getValue().toString();
        String comments = eachReview.child("comments").getValue().toString();
        int rating = Integer.parseInt(eachReview.child("rating").getValue().toString());


        // prepare reviewer and reviewee
        User reviewer = new User();
        User reviewee = new User();

        reviewer.setUsername(eachReview.child("reviewer").getValue().toString());

        reviewee.setUsername(username);
        reviewee.setEmail(email);
        reviewee.setPhoneNumber(phoneNum);
        reviewee.setUid(uid);

        // create a review base on prepared information
        Review review = new Review(reviewer, reviewee, comments, rating);

        // append to review list
        reviewList.addReview(review);
    }


    /**
     * setupTextView just fill text views with corresponding data
     * @param name  the user name string
     * @param intro the self introduction string
     * @param email the email string
     * @param phone the phone number string
     */
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


    private void setUpImageView() {
        // download user image from storage and update
        StorageReference storageRef = storage.getReference().child("users").child(uid);
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ImageView userImage = findViewById(R.id.userPicture);
                Glide.with(UserProfileActivity.this).load(uri).into(userImage);
            }
        });
    }


    /**
     * This is onClick method for button "edit profile"
     * @param view  onClick method needed
     */
    public void editProfile(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }
}
