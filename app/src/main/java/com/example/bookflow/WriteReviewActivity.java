package com.example.bookflow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * You can write a review to a user and rate the user
 * Rating is 0,1,2,3,4,5.
 */
public class WriteReviewActivity extends AppCompatActivity {
    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private String reviewee, reviewer, comments;
    private int rating = 3;

    private EditText reviewTextView;
    private SeekBar seekBarView;
    private TextView showRating;

    /**
     * onCreate method
     * initialize reviewee and reviewer
     * get view of seekBar, review text and rating number
     * set seekBar function listener
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        reviewee = getIntent().getStringExtra("reviewee");
        reviewer = currentUser.getUid();

        reviewTextView = findViewById(R.id.review_Paragraph);
        seekBarView = findViewById(R.id.seekBar);
        showRating = findViewById(R.id.show_rating);

        seekBarView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rating = progress;
                showRating.setText(String.valueOf(rating));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    /**
     * If cancel is pressed, back to the previous activity
     * @param view button
     */
    public void reviewCancel(View view) {
        Intent intent = new Intent(this, UserProfileActivity.class);
        intent.putExtra("com.example.bookflow.MESSAGE",reviewee);
        startActivity(intent);
    }

    /**
     * If done is pressed, upload (comments, date, rating, reviewee, reviewer, uuid) to firebase
     * then go back to user profile activity
     * @param view button
     */
    public void reviewDone (View view) {
        String reviewId = dbRef.child("Reviews").push().getKey();

        // check if comment is empty
        comments = reviewTextView.getText().toString();
        if (comments.isEmpty()) {
            comments = "No Comments";
        }


        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd\nhh:mm:ss");
        String dateStr = df.format(date);
        dbRef.child("Reviews").child(reviewId).child("date").setValue(dateStr);

        // upload informations of a review
        dbRef.child("Reviews").child(reviewId).child("comments").setValue(comments);
        dbRef.child("Reviews").child(reviewId).child("rating").setValue(rating);
        dbRef.child("Reviews").child(reviewId).child("reviewee").setValue(reviewee);
        dbRef.child("Reviews").child(reviewId).child("reviewer").setValue(reviewer);
        dbRef.child("Reviews").child(reviewId).child("uuid").setValue(reviewId);

        Intent intent = new Intent(this, UserProfileActivity.class);
        intent.putExtra("com.example.bookflow.MESSAGE",reviewee);
        startActivity(intent);
    }
}
