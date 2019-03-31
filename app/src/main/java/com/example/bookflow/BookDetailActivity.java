package com.example.bookflow;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
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
import com.example.bookflow.Util.FirebaseIO;
import com.example.bookflow.Util.ScanUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private Button locationButton;

    private String bookId;
    private Book mThisBook;

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
        ratingBar = findViewById(R.id.ratingBar);
        requestButton = findViewById(R.id.book_detail_request_button);
        editButton = findViewById(R.id.book_detail_edit_book_button);
        viewRequestsButton = findViewById(R.id.book_detail_view_requests_button);
        transactionButton = findViewById(R.id.book_detail_scan_for_transaction);
        locationButton = findViewById(R.id.book_detail_view_map);


        mAuth = FirebaseAuth.getInstance();

        mBookRef.child(bookId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mThisBook = dataSnapshot.getValue(Book.class);
                if (mThisBook != null) {
                    populateBookData();

                    controlVisual();
                }

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

        String bookStatus = mThisBook.getStatus().toString();

        boolean isReadyForTrans = bookStatus.equals("ACCEPTED") || bookStatus.equals("BORROWED");
        boolean isOwner = curUser.equals(mThisBook.getOwnerId());
        boolean isBorrower = curUser.equals(mThisBook.getBorrowerId());
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

        if (bookStatus.equals("ACCEPTED") && (isBorrower || isOwner)) {
            locationButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Populate data from firebase database into views
     */
    private void populateBookData() {
        if (mThisBook != null) {
            titleField.setText(mThisBook.getTitle());
            authorField.setText("by " + mThisBook.getAuthor());
            isbnField.setText("ISBN: " + mThisBook.getIsbn());
            statusField.setText(mThisBook.getStatus().toString());
            commentField.setText(mThisBook.getDescription());
            ratingBar.setRating(mThisBook.getRating());
            Glide.with(getApplicationContext())
                    .load(mThisBook.getPhotoUri())
                    .into(bookImage);
        }
    }

    /**
     * When the request button is clicked, this function creates a request
     * and sends a notification to the owner of the book
     */
    public void request(View v) {
        FirebaseUser user = mAuth.getCurrentUser();
        mThisBook.setBorrowerId(user.getUid());

        if (mThisBook.getOwnerId().equals(mThisBook.getBorrowerId())) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "you cannot request your own book",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            mDatabase.getReference()
                    .child("Users/" + mThisBook.getBorrowerId())
                    .addListenerForSingleValueEvent( new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String username = dataSnapshot.child("username").getValue().toString();

                    //create request
                    Request req = new Request(mThisBook.getOwnerId(), mThisBook.getBorrowerId(), bookId);

                    // add request to list of sent requests by user
                    DatabaseReference requestsSentReference = mDatabase.getReference("RequestsSentByUser");
                    String requestId = requestsSentReference.push().getKey();
                    requestsSentReference.child(mThisBook.getBorrowerId()).child(requestId).setValue(req);

                    // add request to list of received requests for book
                    DatabaseReference receivedRequestsByBookReference = mDatabase.getReference("RequestsReceivedByBook");
                    receivedRequestsByBookReference.child(bookId).child(requestId).setValue(req);

                    // send notification
                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm MM/dd");
                    String timestamp = formatter.format(new Date());
                    DatabaseReference receiverRef = notificationRef.child(mThisBook.getOwnerId());
                    String notification_id = receiverRef.push().getKey();
                    receiverRef.child(notification_id).setValue(new Notification(mThisBook.getBorrowerId(), bookId, "request", requestId, mThisBook.getTitle(), username, timestamp));

                    // transition book state
                    DatabaseReference bookRef = mDatabase.getReference("Books");
                    if (mThisBook.getStatus().toString().equals("AVAILABLE")) {
                        bookRef.child(bookId).child("status").setValue("REQUESTED");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("cancelled", databaseError.toException());
                }
            });
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
     *
     * @param v view of the button, not used
     */
    public void edit(View v) {
        Intent intent = new Intent(BookDetailActivity.this, EditBookDetailActivity.class);
        intent.putExtra(EditBookDetailActivity.INTENT_KEY, bookId);
        Log.e("bookid", bookId);
        startActivity(intent);
    }

    /**
     * when the "view request" button is clicked, this transitions
     * user to the edit book page where they see all requests of
     * the book
     *
     * @param v view of the button, not used
     */
    public void viewRequests(View v) {
        Intent intent = new Intent(BookDetailActivity.this, RequestsForBookListActivity.class);
        intent.putExtra(RequestsForBookListActivity.INTENT_KEY, bookId);
        startActivity(intent);
    }

    /**
     * start a transaction process by opening the scanner
     * @param view view of the button, not used
     */
    public void startTransactionProcess(View view) {
        Intent intent = new Intent(BookDetailActivity.this, ScanActivity.class);
        startActivityForResult(intent, ScanUtility.RC_SCAN);
    }

    /**
     * handles the return of other activities, including EditBookActivity
     *
     * @param requestCode request code
     * @param resultCode result code
     * @param data intent data
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
        }
    }

    /**
     * Update the book status after scanning the ISBN.
     * @param isbn the isbn of the book to be borrowed/returned
     */
    private void completeTransaction(String isbn) {

        boolean isTransactionSuccessful = false;
        boolean isReturn = false;

        // first check if the scanned isbn matches the target book
        if (isbn == null || !isbn.equals(mThisBook.getIsbn())) {
            Toast.makeText(this, getString(R.string.isbn_doesnt_match), Toast.LENGTH_LONG).show();
            return;
        }

        String currUserId = mAuth.getCurrentUser().getUid();
        DatabaseReference thisBookRef = mBookRef.child(bookId);
        DatabaseReference statusRef = thisBookRef.child("status");
        DatabaseReference borrowerIdRef = thisBookRef.child("borrowerId");

        final boolean isParticipant = mThisBook.getOwnerId().equals(currUserId) || mThisBook.getBorrowerId().equals(currUserId);
        if (mThisBook.getStatus().toString().equals("ACCEPTED") && isParticipant) {
            // the owner marks the book as borrowed
            statusRef.setValue(Book.BookStatus.BORROWED);
            isTransactionSuccessful = true;
            isReturn = false;

        } else if (mThisBook.getStatus().toString().equals("BORROWED") && isParticipant) {
            /* the borrowed marks the book as available */
            statusRef.setValue(Book.BookStatus.AVAILABLE);
            borrowerIdRef.removeValue();
            isTransactionSuccessful = true;
            isReturn = true;
        }

        if (isTransactionSuccessful) {
            Toast.makeText(BookDetailActivity.this, getString(R.string.scan_successful), Toast.LENGTH_SHORT).show();
            //Todo Dialog rating
            if (isReturn) {
                showEditbox();
            }
        } else {
            Toast.makeText(this, getString(R.string.invalid_operation), Toast.LENGTH_LONG).show();
        }
    }

    // Use dialog interface for editing and deleting feelings
    public void showEditbox() {
        final Dialog dialog = new Dialog(BookDetailActivity.this);
        dialog.setTitle("Rating book");
        dialog.setContentView(R.layout.editbox);
        Button confirmButton = dialog.findViewById(R.id.reviewButton);
        final RatingBar ratingBar = dialog.findViewById(R.id.ratingBar2);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final float rating = ratingBar.getRating();
                mThisBook.transCountInc();
                mThisBook.setRating((mThisBook.getRating()*(mThisBook.getTransCount()-1)+rating)/ mThisBook.getTransCount());
                mBookRef.child(mThisBook.getBookId()).child("rating").setValue(mThisBook.getRating());
                mBookRef.child(mThisBook.getBookId()).child("transCount").setValue(mThisBook.getTransCount());
                //FirebaseIO.getInstance().updateBook(mThisBook,null,null);
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    public void showLocation(View view) {

        DatabaseReference dbRef = mDatabase.getReference("RequestsReceivedByBook")
                .child(bookId);

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> latlon = new ArrayList<>();
                for (DataSnapshot request: dataSnapshot.getChildren()) {
                    try {
                        String lat = request.child("latitude").getValue().toString();
                        String lon = request.child("longitude").getValue().toString();
                        latlon.add(lat);
                        latlon.add(lon);
                        break;
                    } catch (NullPointerException e) {
                        // request doesn't have location info.
                        Toast.makeText(getApplicationContext(), getString(R.string.location_not_set), Toast.LENGTH_LONG)
                                .show();
                        return;
                    }
                }
                Intent mapActivity = new Intent(BookDetailActivity.this, MapsActivity.class);
                mapActivity.putExtra("lat_lon", latlon);
                startActivity(mapActivity);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

