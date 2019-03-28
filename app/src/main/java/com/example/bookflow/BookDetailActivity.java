package com.example.bookflow;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookflow.Model.Book;
import com.example.bookflow.Model.Request;
import com.example.bookflow.Model.Notification;
import com.example.bookflow.Util.ScanUtility;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * This class is for the book details page
 * this displays a picture of the book and
 * a book description
 * an owner can edit their book here
 * a borrower can request a book here
 */

public class BookDetailActivity extends BasicActivity {
    private TextView titleField;
    private TextView authorField;
    private TextView isbnField;
    private TextView statusField;
    private ImageView bookImage;
    private TextView commentField;
    private Button requestButton;
    private ImageButton editButton;
    private Button viewRequestsButton;
    private RatingBar ratingBar;

    private String bookId;
    private String ownerId;
    private String borrowerId;
    private String title;
    private String author;
    private String isbn;
    private String bookStatus;
    private String borrowerName;
    private String username;
    private String photoUri;
    private String comments;
    private float rating;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRequestRef;
    private DatabaseReference notificationRef;
    private DatabaseReference mBookRef;


    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        // retrieves bookId passed from SearchActivity
        Bundle extras = getIntent().getExtras();
        bookId  = extras.getString("book_id");

        mDatabase = FirebaseDatabase.getInstance();
        notificationRef = mDatabase.getReference("Notifications");
        mRequestRef = mDatabase.getReference("Requests");
        mBookRef = mDatabase.getReference("Books");

        titleField = findViewById(R.id.bookName);
        authorField = findViewById(R.id.author);
        isbnField = findViewById(R.id.isbn);
        statusField = findViewById(R.id.book_status);
        bookImage = findViewById(R.id.bookImage);
        commentField = findViewById(R.id.book_comments);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);


        mAuth = FirebaseAuth.getInstance();

        mBookRef.child(bookId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                populateBookData(dataSnapshot);

                controlVisual();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("cancelled", databaseError.toException());
            }
        });

    }

    /**
     * Control the visibility of UI components based on book status
     */
    private void controlVisual() {
        if(bookStatus.equals("AVAILABLE")|| bookStatus.equals("REQUESTED")) {
            statusField.setTextColor(Color.parseColor("#7CFC00"));
        } else if (bookStatus.equals("ACCEPTED")) {
            // find the corresponding request object
            // TODO: change request in firebase first
//            mRequestRef.orderByChild()

        }
        String curUser = mAuth.getCurrentUser().getUid();

        if(curUser.equals(ownerId)) {
            requestButton = findViewById(R.id.requestButton);
            editButton = findViewById(R.id.edit_book_button);
            viewRequestsButton = findViewById(R.id.view_requests_button);

            requestButton.setVisibility(View.GONE);
            editButton.setVisibility(View.VISIBLE);
            viewRequestsButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Populate data from firebase database into views
     * @param dataSnapshot
     */
    private void populateBookData(DataSnapshot dataSnapshot) {
        try {
            title = dataSnapshot.child("title").getValue().toString();
        }
        catch (Exception e){
            title = "None Found";
        }

        try {
            author = dataSnapshot.child("author").getValue().toString();
        }
        catch (Exception e){
            author = "None Found";
        }

        try {
            isbn = dataSnapshot.child("isbn").getValue().toString();
        }
        catch (Exception e){
            isbn = "Not Found";
        }
        try {
            ownerId = dataSnapshot.child("ownerId").getValue().toString();
        }
        catch(Exception e) {
            ownerId = "Not Found";
        }
        try {
            photoUri = dataSnapshot.child("photoUri").getValue().toString();
        }
        catch (Exception e){
            photoUri = "android.resource://com.example.bookflow/" + R.drawable.image_placeholder;
        }

        try {
            comments = dataSnapshot.child("description").getValue().toString();
        }
        catch (Exception e) {
            comments = "";
        }

        try {
            bookStatus = dataSnapshot.child("status").getValue().toString();

        }
        catch(Exception e){
            bookStatus = "Not Found";
        }

        try{
            rating = Float.valueOf(dataSnapshot.child("rating").getValue().toString());
        }catch(Exception e){
            rating = 3;
        }

        titleField.setText(title);
        authorField.setText("by " + author);
        isbnField.setText("ISBN: " + isbn);
        statusField.setText(bookStatus);
        commentField.setText(comments);
        ratingBar.setRating(rating);
        Glide.with(BookDetailActivity.this)
                .load(photoUri)
                .into(bookImage);
    }

    /**
     * When the request button is clicked, this function creates a request
     * and sends a notification to the owner of the book
     */

    public void request(View v) {
        FirebaseUser user = mAuth.getCurrentUser();
        borrowerId = user.getUid();

        if (ownerId.equals(borrowerId)) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "you cannot request your own book",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            ValueEventListener userListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    username = dataSnapshot.child("username").getValue().toString();

                    //create request
                    //DatabaseReference requestReference = mDatabase.getReference("Requests");
                    //String request_id = requestReference.push().getKey();
                    Request req = new Request(ownerId, borrowerId, bookId);
                    //requestReference.child(request_id).setValue();

                    // add request to list of sent requests by user
                    DatabaseReference requestsSentReference = mDatabase.getReference("RequestsSentByUser");
                    String request_id = requestsSentReference.push().getKey();
                    requestsSentReference.child(borrowerId).child(request_id).setValue(req);

                    // add request to list of received requests for book
                    DatabaseReference receivedRequestsByBookReference = mDatabase.getReference("RequestsReceivedByBook");
                    receivedRequestsByBookReference.child(bookId).child(request_id).setValue(req);

                    // send notification
                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm MM/dd");
                    String timestamp = formatter.format(new Date());
                    DatabaseReference receiverRef = notificationRef.child(ownerId);
                    String notification_id = receiverRef.push().getKey();
                    receiverRef.child(notification_id).setValue(new Notification(borrowerId, bookId, "request", request_id, title, username, timestamp));
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("cancelled", databaseError.toException());
                }
            };
            mDatabase.getReference().child("Users").child(borrowerId).addListenerForSingleValueEvent(userListener);
            Toast toast = Toast.makeText(getApplicationContext(),
                    "request sent",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * when the edit button is clicked, this transitions
     * user to the edit book page where they can edit
     * the book description
     */

    public void edit(View v) {
        Intent intent = new Intent(BookDetailActivity.this, EditBookDetailActivity.class);
        intent.putExtra(EditBookDetailActivity.INTENT_KEY, bookId);
        Log.e("bookid", bookId);
        startActivity(intent);
    }

    public void viewRequests(View v) {
        Intent intent = new Intent(BookDetailActivity.this, RequestsForBookListActivity.class);
        intent.putExtra("bookid", bookId);
        startActivity(intent);
    }

    /**
     * start a transaction process by opening the scanner
     * @param view
     */
    public void startTransactionProcess(View view) {
        Intent intent = new Intent(BookDetailActivity.this, ScanActivity.class);
        startActivityForResult(intent, ScanUtility.RC_SCAN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == ScanUtility.RC_SCAN) {
            if (resultCode == RESULT_OK) {
                String isbn = data.getStringExtra(ScanActivity.SCAN_RESULT);
                completeTransaction(isbn);
            }
        }
    }

    /**
     * Update the book status after scanning the ISBN.
     * @param isbn
     */
    private void completeTransaction(String isbn) {
        // first check if the scanned isbn matches the target book
        if (isbn == null || !isbn.equals(this.isbn)) {
            Toast.makeText(this, getString(R.string.isbn_doesnt_match), Toast.LENGTH_LONG).show();
            return;
        }

        String currUserId = mAuth.getCurrentUser().getUid();
        DatabaseReference thisBookRef = mBookRef.child(bookId);
        DatabaseReference statusRef = thisBookRef.child("status");
        DatabaseReference borrowIdRef = thisBookRef.child("borrowerId");

        if (bookStatus.equals("ACCEPTED") && ownerId.equals(currUserId)) {
            // the owner marks the book as borrowed
            statusRef.setValue(Book.BookStatus.BORROWED);
//            borrowIdRef.setValue();

        } else if (bookStatus.equals("BORROWED") && borrowerId.equals(currUserId)) {
            /* the borrowed marks the book as available */
            statusRef.setValue(Book.BookStatus.AVAILABLE);
        } else {
            Toast.makeText(this, getString(R.string.invalid_operation), Toast.LENGTH_LONG).show();
        }

        Toast.makeText(BookDetailActivity.this, getString(R.string.scan_successful), Toast.LENGTH_SHORT).show();
    }
}
