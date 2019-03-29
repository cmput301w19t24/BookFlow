package com.example.bookflow;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    public static final String INTENT_EXTRA = "book_id";

    private static final int RC_EDIT_BOOK = 1;
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
    private Button transactionButton;

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
    private DatabaseReference notificationRef;
    private DatabaseReference mBookRef;


    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        // retrieves bookId passed from SearchActivity
        Bundle extras = getIntent().getExtras();
        bookId  = extras.getString(INTENT_EXTRA);

        mDatabase = FirebaseDatabase.getInstance();
        notificationRef = mDatabase.getReference("Notifications");
        mBookRef = mDatabase.getReference("Books");

        titleField = findViewById(R.id.book_detail_book_name);
        authorField = findViewById(R.id.book_detail_author);
        isbnField = findViewById(R.id.book_detail_isbn);
        statusField = findViewById(R.id.book_detail_book_status);
        bookImage = findViewById(R.id.book_detail_book_image);
        commentField = findViewById(R.id.book_detail_book_comments);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        requestButton = findViewById(R.id.book_detail_request_button);
        editButton = findViewById(R.id.book_detail_edit_book_button);
        viewRequestsButton = findViewById(R.id.book_detail_view_requests_button);
        transactionButton = findViewById(R.id.book_detail_scan_for_transaction);


        mAuth = FirebaseAuth.getInstance();

        mBookRef.child(bookId).addValueEventListener(new ValueEventListener() {
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
        String curUser = mAuth.getCurrentUser().getUid();

        boolean isReadyForTrans = bookStatus.equals("ACCEPTED") || bookStatus.equals("BORROWED");
        boolean isOwner = curUser.equals(ownerId);
        boolean isBorrower = curUser.equals(borrowerId);
        boolean isParticipant = isOwner || isBorrower;


        if(!isReadyForTrans) {
            statusField.setTextColor(Color.parseColor("#7CFC00"));
            requestButton.setVisibility(View.VISIBLE);
            transactionButton.setVisibility(View.GONE);
        }

        if (isOwner) {
            requestButton.setVisibility(View.GONE);
            editButton.setVisibility(View.VISIBLE);
            viewRequestsButton.setVisibility(View.VISIBLE);
        }

        if (isParticipant && isReadyForTrans) {
            transactionButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Populate data from firebase database into views
     * @param dataSnapshot data snapshot from firebase
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
        Glide.with(getApplicationContext())
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
                    Request req = new Request(ownerId, borrowerId, bookId);

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
        startActivityForResult(intent, RC_EDIT_BOOK);
    }

    public void viewRequests(View v) {
        Intent intent = new Intent(BookDetailActivity.this, RequestsForBookListActivity.class);
        intent.putExtra(RequestsForBookListActivity.INTENT_KEY, bookId);
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

    /**
     * handles the return of other activities, including EditBookActivity
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == ScanUtility.RC_SCAN) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                String isbn = data.getStringExtra(ScanActivity.SCAN_RESULT);
                if (isbn != null) {
                    completeTransaction(isbn);
                }
            }
        } else if (requestCode == RC_EDIT_BOOK) {
            if (resultCode == RESULT_OK) {
                // reload the book data

            }
        }
    }

    /**
     * Update the book status after scanning the ISBN.
     * @param isbn
     */
    private void completeTransaction(String isbn) {

        boolean isTransactionSuccessful = false;

        // first check if the scanned isbn matches the target book
        if (isbn == null || !isbn.equals(this.isbn)) {
            Toast.makeText(this, getString(R.string.isbn_doesnt_match), Toast.LENGTH_LONG).show();
            return;
        }

        String currUserId = mAuth.getCurrentUser().getUid();
        DatabaseReference thisBookRef = mBookRef.child(bookId);
        DatabaseReference statusRef = thisBookRef.child("status");
        DatabaseReference borrowIdRef = thisBookRef.child("borrowerId");

        final boolean isParticipant = ownerId.equals(currUserId) || borrowerId.equals(currUserId);
        if (bookStatus.equals("ACCEPTED") && isParticipant) {
            // the owner marks the book as borrowed
            statusRef.setValue(Book.BookStatus.BORROWED);
            isTransactionSuccessful = true;

        } else if (bookStatus.equals("BORROWED") && isParticipant) {
            /* the borrowed marks the book as available */
            statusRef.setValue(Book.BookStatus.AVAILABLE);
            isTransactionSuccessful = true;
        }

        if (isTransactionSuccessful) {
            Toast.makeText(BookDetailActivity.this, getString(R.string.scan_successful), Toast.LENGTH_SHORT).show();
            //Todo Dialog rating
            //showEditbox();
        } else {
            Toast.makeText(this, getString(R.string.invalid_operation), Toast.LENGTH_LONG).show();
        }
    }

    // Use dialog interface for editting and deleting feelings
    public void showEditbox() {
        final Dialog dialog = new Dialog(BookDetailActivity.this);
        dialog.setTitle("Rating book");
        dialog.setContentView(R.layout.editbox);
        final EditText reviewText = dialog.findViewById(R.id.reviewText);
        Button confirmButton = dialog.findViewById(R.id.reviewButton);
        final RatingBar ratingBar = dialog.findViewById(R.id.ratingBar2);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String review = reviewText.getText().toString();
                float rating = ratingBar.getRating();


                dialog.dismiss();
            }
        });


        dialog.show();
    }

}

