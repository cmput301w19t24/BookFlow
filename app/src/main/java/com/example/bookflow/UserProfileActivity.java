package com.example.bookflow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bookflow.Model.Book;
import com.example.bookflow.Model.Review;
import com.example.bookflow.Model.User;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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

    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private String uid;
    private Query query_user;
    private Query query_review;
    private ArrayList<Review> reviews;
    private ArrayList<Book> books;
    private static ListView bookList;
    private static ListView reviewList;
    private ReviewAdapter adpReview;

    // class of Review Adapter
    class ReviewAdapter extends ArrayAdapter<Review> {
        ReviewAdapter(Context c, ArrayList<Review> reviews) {
            super(c,R.layout.user_list, reviews);
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {
            Review review = this.getItem(position);

            if (v == null) {
                v = LayoutInflater.from(getContext()).inflate(R.layout.user_list, parent, false);
            }
            TextView comments = v.findViewById(R.id.review_text);
            TextView rating = v.findViewById(R.id.rating);

            setReviewUser(v, String.valueOf(review.getReviewerID()));

            comments.setText(review.getComments());
            rating.setText(review.getRating());

            return v;
        }
    }

    // class of Book Adapter
    class BookAdapter extends ArrayAdapter<Book> {
        BookAdapter(Context c, ArrayList<Book> reviews) {
            // TODO:
            super(c,R.layout.user_list, reviews);
        }

//        @Override
//        public View getView(int position, View v, ViewGroup parent) {
//            Book book = this.getItem(position);
//
//            if (v == null) {
//                v = LayoutInflater.from(getContext()).inflate(R.layout.user_list, parent, false);
//            }
//            TextView comments = v.findViewById(R.id.review_text);
//            TextView rating = v.findViewById(R.id.rating);
//
//            setReviewUser(v, String.valueOf(review.getReviewerID()));
//
//            comments.setText(review.getComments());
//            rating.setText(review.getRating());
//
//            return v;
//        }
    }

    public void setReviewUser(final View v, final String tmpuid) {
        query_user.orderByChild("uid").equalTo(tmpuid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = (User) dataSnapshot.getValue(User.class);
                TextView rname = v.findViewById(R.id.reviewer_name);
                rname.setText(user.getUsername());
                ImageView mphoto = v.findViewById(R.id.reviewer_photo);
                Glide.with(UserProfileActivity.this).load(user.getImageurl()).into(mphoto);
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        query_user = database.getReference().child("Users");
        query_review = database.getReference().child("Reviews");

        reviews = new ArrayList<Review>();
        books = new ArrayList<Book>();

        adpReview = new ReviewAdapter(this, reviews);

        setUserProfile();
        loadReviewList();
    }

    private void setUserProfile() {
        query_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child(uid).getValue(User.class);
                TextView textView = findViewById(R.id.profileName);
                textView.setText(user.getUsername());

                textView = findViewById(R.id.selfIntro);
                textView.setText(user.getSelfIntro());

                textView = findViewById(R.id.emailToBeChange);
                textView.setText(user.getEmail());

                textView = findViewById(R.id.phoneToBeChange);
                textView.setText(user.getPhoneNumber());

                // TODO: replace the photo uri in the database

                ImageView userImage = findViewById(R.id.userPicture);
                Glide.with(UserProfileActivity.this).load(user.getImageurl()).into(userImage);

//                // download user image from storage and update
//                StorageReference storageRef;
//                FirebaseStorage storage = FirebaseStorage.getInstance();
//                try {
//                    storageRef = storage.getReference().child("users").child(uid);
//                } catch (Exception e) {
//                    return ;
//                }
//
//                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        ImageView userImage = findViewById(R.id.userPicture);
//                        Glide.with(UserProfileActivity.this).load(uri).into(userImage);
//                    }
//                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void loadReviewList() {
        reviewList = (ListView) findViewById(R.id.reviewList);
        reviews.clear();
        // add user's book to adapter
        query_review.orderByChild("reviewee").equalTo(uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Review review = (Review) dataSnapshot.getValue(Review.class);
                review.setReviewer(dataSnapshot.child("reviewer").getValue().toString());
                if (!reviews.contains(review)) {
                    adpReview.add(review);
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                loadReviewList();
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                loadReviewList();
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        reviewList.setAdapter(adpReview);
    }

    /**
     * onClick method of Review button
     * @param view button view
     */
    public void switchReview(View view){
        Button button = findViewById(R.id.review_switch);
        button.setBackgroundResource(R.drawable.tab_select);
        ((TextView)findViewById(R.id.review_switch)).setTextColor(getResources().getColor(R.color.colorPrimary));

        button = findViewById(R.id.offer_switch);
        button.setBackgroundResource(R.drawable.tab_notselect);
        ((TextView)findViewById(R.id.offer_switch)).setTextColor(Color.WHITE);

        LinearLayout layout = findViewById(R.id.reviewsLayout);
        layout.setVisibility(LinearLayout.VISIBLE);

        layout = findViewById(R.id.offerLayout);
        layout.setVisibility(LinearLayout.GONE);
    }

    /**
     * onClick method of Offer button
     * @param view button view
     */
    public void switchOffer(View view){
        Button button = findViewById(R.id.review_switch);
        button.setBackgroundResource(R.drawable.tab_notselect);
        ((TextView)findViewById(R.id.review_switch)).setTextColor(Color.WHITE);

        button = findViewById(R.id.offer_switch);
        button.setBackgroundResource(R.drawable.tab_select);
        ((TextView)findViewById(R.id.offer_switch)).setTextColor(getResources().getColor(R.color.colorPrimary));

        LinearLayout layout = findViewById(R.id.reviewsLayout);
        layout.setVisibility(LinearLayout.GONE);

        layout = findViewById(R.id.offerLayout);
        layout.setVisibility(LinearLayout.VISIBLE);
    }

    /**
     * This is onClick method for button "edit profile"
     * @param view  onClick method needed
     */
    public void editProfile(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
