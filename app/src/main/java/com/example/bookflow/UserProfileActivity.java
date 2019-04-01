package com.example.bookflow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bookflow.Model.Book;
import com.example.bookflow.Model.Review;
import com.example.bookflow.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * This class is for user profile page
 * It fills all information of a user profile, including
 * name, phone, self-intro, email, books offered and review list
 * button for edit profile (if it's your own profile)
 * button for write review (if you are visiting other's profile)
 */
public class UserProfileActivity extends BasicActivity {

    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    public String uid;

    private Query query_user;
    private Query query_review;
    private Query query_book;

    private ArrayList<Review> reviews;
    private ArrayList<Book> books;

    private static ListView bookList;
    private static ListView reviewList;

    private ReviewAdapter adpReview;
    private BookAdapter adpBook;

    /**
     * class of review list adapter
     * it puts comments, reviewer icon, date and rating into an item of the review list
     */
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
            // get three views
            TextView comments = v.findViewById(R.id.review_text);
            RatingBar rating = v.findViewById(R.id.review_rating);
            TextView date = v.findViewById(R.id.review_date);

            setReviewUser(v, String.valueOf(review.getReviewerID()));

            // set three views
            comments.setText(review.getComments());
            rating.setRating(Float.parseFloat(review.getRating()));
            date.setText(review.getDate());

            return v;
        }
    }

    /**
     * Class of offered book list, the same as the one in the main page
     */
    class BookAdapter extends ArrayAdapter<Book> {
        BookAdapter(Context c, ArrayList<Book> reviews) {
            super(c,R.layout.profile_book_item, reviews);
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {
            Book book = this.getItem(position);

            if (v == null) {
                v = LayoutInflater.from(getContext()).inflate(R.layout.profile_book_item, parent, false);
            }

            // get view of author, title, photo, status
            TextView mauthor = v.findViewById(R.id.uauthor);
            TextView mtitle = v.findViewById(R.id.utitle);
            ImageView mphoto = v.findViewById(R.id.uphoto);
            TextView mstatus = v.findViewById(R.id.ustatus);

            // set author, title, photo, and status
            mauthor.setText(book.getAuthor());
            mtitle.setText(book.getTitle());
            mstatus.setText(book.getStatus().toString());
            Glide.with(UserProfileActivity.this).load(book.getPhotoUri()).into(mphoto);
            return v;
        }
    }

    /**
     * get the reviewee data from the database and bind them to views
     * @param v view
     * @param tmpuid uid
     */
    private void setReviewUser(final View v, final String tmpuid) {
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

    /**
     * onCreate method
     * also initializes variables
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        query_user = database.getReference().child("Users");
        query_review = database.getReference().child("Reviews");
        query_book = database.getReference().child("Books");

        reviews = new ArrayList<Review>();
        books = new ArrayList<Book>();

        adpReview = new ReviewAdapter(this, reviews);
        adpBook = new BookAdapter(this, books);
    }

    /**
     * onStart method
     * check it's (visiting your own profile) or (viewing other's profile),
     * and according to this, set (write review button) or (edit profile button) to GONE
     * call other initializing methods
     */
    @Override
    protected void onStart() {
        super.onStart();

        books.clear();
        reviews.clear();

        // determine whether it's yourself visiting your profile or other user visiting your profile
        Intent intent = getIntent();
        String message = intent.getStringExtra("com.example.bookflow.MESSAGE");
        if (message != null) {
            ImageView imageButton = findViewById(R.id.editPersonInfo);
            imageButton.setEnabled(false);
            imageButton.setVisibility(View.INVISIBLE);
            Button writeReview = findViewById(R.id.write_review);
            writeReview.setVisibility(View.VISIBLE);
            uid = message;
        } else  {
            FirebaseUser user = mAuth.getCurrentUser();
            uid = user.getUid();
            findViewById(R.id.profile_button).setBackgroundResource(R.drawable.profile_select);
        }

        setUserProfile();
        loadReviewList();
        loadBookList();
    }

    /**
     * initialize user profile (basic informations)
     * like user name, user self intro, user phone, user email and user photo
     */
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

                ImageView userImage = findViewById(R.id.userPicture);
                Glide.with(UserProfileActivity.this).load(user.getImageurl()).into(userImage);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /**
     * load book list from database and add to adapter
     */
    private void loadBookList() {
        bookList = (ListView) findViewById(R.id.offerList);
        books.clear();
        adpBook.clear();
        // add user's book to adapter
        query_book.orderByChild("ownerId").equalTo(uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Book book = (Book)dataSnapshot.getValue(Book.class);
                if (!books.contains(book)) {
                    books.add(book);
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                loadBookList();
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                loadBookList();
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        bookList.setAdapter(adpBook);
        bookList.setClickable(true);
        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent_detail = new Intent(UserProfileActivity.this, BookDetailActivity.class);
                String book_id = adpBook.getItem(position).getBookId();
                intent_detail.putExtra(BookDetailActivity.INTENT_EXTRA, book_id);
                startActivity(intent_detail);
            }
        });
    }

    /**
     * load review list from database and add to adapter
     */
    private void loadReviewList() {
        reviewList = (ListView) findViewById(R.id.reviewList);
        // add user's book to adapter
        adpReview.clear();
        reviews.clear();

        query_review.orderByChild("reviewee").equalTo(uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Review review = (Review) dataSnapshot.getValue(Review.class);

                boolean pleaseAdd = true;

                // only add the review when all the fields of the review has been
                // received. i.e. not null
                DataSnapshot reviewerUid = dataSnapshot.child("reviewer");
                DataSnapshot uuidData = dataSnapshot.child("uuid");
                if (null != reviewerUid.getValue()) {
                    review.setReviewer(reviewerUid.getValue().toString());
                } else {
                    pleaseAdd = false;
                }

                if (null != uuidData.getValue()) {
                    review.setUUID(uuidData.getValue().toString());
                } else {
                    pleaseAdd = false;
                }

                if (!reviews.contains(review) && pleaseAdd) {
                    reviews.add(review);
                    reviewList.setAdapter(adpReview);
                    reviewList.setClickable(true);
                    reviewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent_detail = new Intent(UserProfileActivity.this, UserProfileActivity.class);
                            String targetUid = adpReview.getItem(position).getReviewerID();
                            String myUid = mAuth.getCurrentUser().getUid();
                            if (! targetUid.equals(myUid)) {
                                intent_detail.putExtra("com.example.bookflow.MESSAGE", targetUid);
                            }
                            startActivity(intent_detail);
                        }
                    });
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Review review = (Review) dataSnapshot.getValue(Review.class);
                boolean pleaseAdd = true;

                // only add the review when all the fields of the review has been
                // received. i.e. not null
                DataSnapshot reviewerUid = dataSnapshot.child("reviewer");
                DataSnapshot uuidData = dataSnapshot.child("uuid");
                if (null != reviewerUid.getValue()) {
                    review.setReviewer(reviewerUid.getValue().toString());
                } else {
                    pleaseAdd = false;
                }

                if (null != uuidData.getValue()) {
                    review.setUUID(uuidData.getValue().toString());
                } else {
                    pleaseAdd = false;
                }
                if (!reviews.contains(review) && pleaseAdd) {
                    reviews.add(review);
                    reviewList.setAdapter(adpReview);
                }
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
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }

    /**
     * onClick method of write Review button
     * got to write review activity
     * @param view
     */
    public void writeReview(View view) {
        Intent intent = new Intent(this, WriteReviewActivity.class);
        intent.putExtra("reviewee", uid);
        startActivity(intent);
    }
}
