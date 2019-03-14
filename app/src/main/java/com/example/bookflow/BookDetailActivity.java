package com.example.bookflow;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookflow.Model.Book;
import com.example.bookflow.Model.Request;
import com.example.bookflow.Model.Notification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


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

    private String book_id;
    private String owner_id;
    private String borrower_id;
    private String title;
    private String author;
    private String isbn;
    private String book_status;
    private String borrower_name;
    private String username;
    private String photoUri;
    private String comments;

    private FirebaseDatabase mDatabase;
    private DatabaseReference requestRef;
    private DatabaseReference notificationRef;


    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        // retrieves book_id passed from SearchActivity
        Bundle extras = getIntent().getExtras();
        book_id = extras.getString("book_id");

        mDatabase = FirebaseDatabase.getInstance();
        notificationRef = mDatabase.getReference("Notifications");
        requestRef = mDatabase.getReference("Requests");

        titleField = findViewById(R.id.bookName);
        authorField = findViewById(R.id.author);
        isbnField = findViewById(R.id.isbn);
        statusField = findViewById(R.id.book_status);
        bookImage = findViewById(R.id.bookImage);
        commentField = findViewById(R.id.book_comments);

        mAuth = FirebaseAuth.getInstance();

        ValueEventListener bookListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

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
                    owner_id = dataSnapshot.child("ownerId").getValue().toString();
                }
                catch(Exception e) {
                    owner_id = "Not Found";
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
                    book_status= dataSnapshot.child("status").getValue().toString();

                }
                catch(Exception e){
                    book_status = "Not Found";
                }


                //Log.e("ahhhhhh", owner_id);

                titleField.setText(title);
                authorField.setText("by " + author);
                isbnField.setText("ISBN: " + isbn);
                statusField.setText(book_status);
                commentField.setText(comments);
                Glide.with(BookDetailActivity.this)
                        .load(photoUri)
                        .into(bookImage);

                if(book_status.equals("AVAILABLE")|| book_status.equals("REQUESTED")) {
                    statusField.setTextColor(Color.parseColor("#7CFC00"));
                }
                String curUser = mAuth.getCurrentUser().getUid();

                if(curUser.equals(owner_id)) {
                    requestButton = findViewById(R.id.requestButton);
                    editButton = findViewById(R.id.edit_book_button);
                    viewRequestsButton = findViewById(R.id.view_requests_button);
                    requestButton.setVisibility(View.GONE);
                    editButton.setVisibility(View.VISIBLE);
                    viewRequestsButton.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("cancelled", databaseError.toException());
            }
        };
        mDatabase.getReference().child("Books").child(book_id).addListenerForSingleValueEvent(bookListener);




    }

    /**
     * When the request button is clicked, this function creates a request
     * and sends a notification to the owner of the book
     */

    public void request(View v) {
        FirebaseUser user = mAuth.getCurrentUser();
        borrower_id = user.getUid();
        //Log.e("what",borrower_id);
        //Log.e("what",owner_id);

        if (owner_id.equals(borrower_id)) {
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
                    DatabaseReference requestReference = mDatabase.getReference("Requests");
                    String request_id = requestReference.push().getKey();
                    requestReference.child(request_id).setValue(new Request(owner_id, borrower_id, book_id));

                    // add request to list of sent requests by user
                    DatabaseReference requestsSentReference = mDatabase.getReference("RequestsSentByUser");
                    requestsSentReference.child(borrower_id).child(request_id).setValue(true);

                    // add request to list of received requests for book
                    DatabaseReference receivedRequestsByBookReference = mDatabase.getReference("RequestsReceivedByBook");
                    receivedRequestsByBookReference.child(book_id).child(request_id).setValue(true);

                    // send notification
                    DatabaseReference receiverRef = notificationRef.child(owner_id);
                    String notification_id = receiverRef.push().getKey();
                    receiverRef.child(notification_id).setValue(new Notification(borrower_id, book_id, "request", request_id, title, username));
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("cancelled", databaseError.toException());
                }
            };
            mDatabase.getReference().child("Users").child(borrower_id).addListenerForSingleValueEvent(userListener);
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
        intent.putExtra("bookid",book_id);
        Log.e("bookid", book_id);
        startActivity(intent);
    }

}
