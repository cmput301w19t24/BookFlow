package com.example.bookflow;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookflow.Model.Book;
import com.example.bookflow.Model.Notification;
import com.example.bookflow.Model.Request;
import com.example.bookflow.Model.User;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Request Detail Activity
 * Activity responsible for populating detailed information about a request
 * handles the transactions of rejecting or accepting requests
 */
public class RequestDetailActivity extends BasicActivity {
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;

    private TextView userLabel;
    private TextView bookLabel;
    private TextView requestText;
    private ImageView userIcon;
    private ImageView bookIcon;
    private Button cancelButton;
    private Button acceptButton;
    private Button rejectButton;

    private String ownerId;
    private String bookId;
    private String borrowerId;
    private String status;
    private String requestId;
    private String ownerName;
    private String bookTitle;


    /**
     * activity onCreate
     * @param savedInstanceState saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);

        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        final String curUser = mAuth.getCurrentUser().getUid();

        userLabel = findViewById(R.id.request_detail_user_label);
        bookLabel = findViewById(R.id.request_detail_book_label);
        requestText = findViewById(R.id.request_detail_text);
        userIcon = findViewById(R.id.request_user_icon);
        bookIcon = findViewById(R.id.request_book_icon);
        cancelButton = findViewById(R.id.cancel_sent_request_button);
        acceptButton = findViewById(R.id.accept_received_request_button);
        rejectButton = findViewById(R.id.reject_received_request_button);


        Bundle extras = getIntent().getExtras();
        //ownerId = extras.getString("ownerId");
        bookId = extras.getString("bookId");
        //borrowerId = extras.getString("borrowerId");
        //status = extras.getString("status");
        requestId = extras.getString("requestId");


        DatabaseReference requestReference = mDatabase.getReference("RequestsReceivedByBook").child(bookId).child(requestId);
        bookLabel.setText("Book");

        /**
         * request listener
         * retrieves owner and borrower information from request object to populate request detail page
         */
        ValueEventListener requestListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Request r = dataSnapshot.getValue(Request.class);
                    borrowerId = r.getBorrowerId();
                    status = r.getStatus();
                    ownerId = r.getOwnerId();

                    DatabaseReference bookReference = mDatabase.getReference("Books").child(bookId);

                    ValueEventListener bookListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Object photoUriObj = dataSnapshot.child("photoUri").getValue();
                            final String bookTitle = dataSnapshot.child("title").getValue().toString();

                            if (photoUriObj != null) {
                                String bookImgUri = photoUriObj.toString();
                                Glide.with(RequestDetailActivity.this)
                                        .load(bookImgUri).into(bookIcon);
                            }

                            if (curUser.equals(ownerId)) {
                                userLabel.setText("Requester");

                                DatabaseReference userDbRef =  mDatabase.getReference("Users").child(borrowerId);
                                ValueEventListener userListener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String borrowerUsername = dataSnapshot.child("username").getValue().toString();
                                        Object borrowerPhotoUrl = dataSnapshot.child("imageurl").getValue();

                                        if (borrowerPhotoUrl != null) {
                                            Glide.with(getApplicationContext())
                                                    .load(borrowerPhotoUrl.toString())
                                                    .into(userIcon);
                                        }

                                        if (status.equals("Pending")) {
                                            requestText.setText(borrowerUsername + " has requested \"" + bookTitle + "\"");
                                            acceptButton.setVisibility(View.VISIBLE);
                                            rejectButton.setVisibility(View.VISIBLE);
                                        }

                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.w("cancelled", databaseError.toException());
                                    }
                                };
                                userDbRef.addListenerForSingleValueEvent(userListener);

                            }
                            else if (curUser.equals(borrowerId)) {
                                userLabel.setText("Owner");
                                String path = "users/" + ownerId;
                                StorageReference imageRef = FirebaseStorage.getInstance().getReference().child(path);
                                Glide.with(RequestDetailActivity.this)
                                        .load(imageRef).into(userIcon);

                                DatabaseReference userDbRef =  mDatabase.getReference("Users").child(ownerId);
                                ValueEventListener userListener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String ownerUsername = dataSnapshot.child("username").getValue().toString();
                                        if (status.equals("Pending")) {
                                            requestText.setText("Your request for \"" + bookTitle + "\" from "  +ownerUsername +" is still pending.");
                                            cancelButton.setVisibility(View.VISIBLE);
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.w("cancelled", databaseError.toException());
                                    }
                                };
                                userDbRef.addListenerForSingleValueEvent(userListener);

                            }

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("cancelled", databaseError.toException());
                        }
                    };
                    bookReference.addListenerForSingleValueEvent(bookListener);
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Request Rejected",
                            Toast.LENGTH_LONG);
                    finish();
                }
        }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("cancelled", databaseError.toException());
            }
        };
        requestReference.addListenerForSingleValueEvent(requestListener);
    }

    /**
     * reject button onClick
     * rejects a received requests, makes relevant updates to to database
     * @param v view
     */
    public void reject(View v) {
        Log.e("hello",requestId);
        // display toast
        Toast toast = Toast.makeText(getApplicationContext(),
                "Request Rejected",
                Toast.LENGTH_LONG);
        toast.show();

        // remove from owner's received requests
        DatabaseReference bookReqRef = mDatabase.getReference("RequestsReceivedByBook").child(bookId).child(requestId);
        bookReqRef.removeValue();

        // set requester's status to rejected
        DatabaseReference sentReqRef = mDatabase.getReference("RequestsSentByUser").child(borrowerId).child(requestId).child("status");
        sentReqRef.setValue("Rejected");

        // if only request for book rejected, set back to available
        setToAvailableIfNoRequests();


        // delete notification
        final DatabaseReference notificationRef = mDatabase.getReference("Notifications").child(ownerId);
        ValueEventListener notificationListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren() ){
                    Notification n = child.getValue(Notification.class);
                    if (n.getTransactionId().equals(requestId)) {
                        notificationRef.child(child.getKey()).removeValue();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("cancelled", databaseError.toException());
            }
        };
        notificationRef.addListenerForSingleValueEvent(notificationListener);
        finish();
    }

    /**
     * set location onClick
     * sets location for where users will meet up
     * @param v view
     */
    public void selectLocation(View v) {
        Intent intent = new Intent(this, MapsActivity.class);
        ArrayList<String> infos = new ArrayList<>();
        infos.add(this.ownerId);
        infos.add(this.bookId);
        infos.add(this.status);
        infos.add(this.requestId);
        intent.putExtra("parameter", infos);
        startActivity(intent);
    }

    /**
     * accept button onClick
     * when user presses accept, sends notifications and sends updates request and book status
     * @param v view
     */
    public void accept(View v) {
        DatabaseReference thebookRef = mDatabase.getReference("Books").child(bookId);
        thebookRef.child("status").setValue("ACCEPTED");
        thebookRef.child("borrowerId").setValue(borrowerId);

        // set accept for AllUserRequests - Borrower - Request
        DatabaseReference sentReqRef = mDatabase.getReference("RequestsSentByUser").child(borrowerId).child(requestId);
        sentReqRef.child("status").setValue("Accepted");

        // set accept for AllBookRequests - Book - Request
        DatabaseReference bookReqRef = mDatabase.getReference("RequestsReceivedByBook").child(bookId).child(requestId);
        bookReqRef.child("status").setValue("Accepted");


        DatabaseReference notificationRef = mDatabase.getReference("Notifications");
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm MM/dd");
        final String timestamp = formatter.format(new Date());
        final DatabaseReference receiverRef = notificationRef.child(borrowerId);
        final String notification_id = receiverRef.push().getKey();

        DatabaseReference userRef = mDatabase.getReference("Users").child(ownerId);
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                ownerName = u.getUsername();

                DatabaseReference bookRef = mDatabase.getReference("Books").child(bookId);
                ValueEventListener bookListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Book b = dataSnapshot.getValue(Book.class);
                        bookTitle = b.getTitle();
                        receiverRef.child(notification_id).setValue(new Notification(ownerId, bookId, "accept", requestId, bookTitle, ownerName, timestamp));

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("cancelled", databaseError.toException());
                    }
                };
                bookRef.addListenerForSingleValueEvent(bookListener);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("cancelled", databaseError.toException());
            }
        };
        userRef.addListenerForSingleValueEvent(userListener);


        DatabaseReference bookRef = mDatabase.getReference("RequestsReceivedByBook").child(bookId);
        ValueEventListener reqListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren() ){
                    Request r = child.getValue(Request.class);
                    String borId = r.getBorrowerId();
                    final String reqId = child.getKey();
                    if (!reqId.equals(requestId)){
                        Log.e("TESTING", borId);
                        Log.e("TESTING", reqId);
                        DatabaseReference sentRef = mDatabase.getReference("RequestsSentByUser").child(borId).child(reqId).child("status");
                        sentRef.setValue("Rejected");
                        DatabaseReference bookReqRef = mDatabase.getReference("RequestsReceivedByBook").child(bookId).child(reqId);
                        bookReqRef.removeValue();
                    }
                    final DatabaseReference notificationRef = mDatabase.getReference("Notifications").child(ownerId);
                    ValueEventListener notificationListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot child : dataSnapshot.getChildren() ){
                                Notification n = child.getValue(Notification.class);
                                if (n.getTransactionId().equals(reqId)) {
                                    notificationRef.child(child.getKey()).removeValue();
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("cancelled", databaseError.toException());
                        }
                    };
                    notificationRef.addListenerForSingleValueEvent(notificationListener);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("cancelled", databaseError.toException());
            }
        };
        bookRef.addListenerForSingleValueEvent(reqListener);
        selectLocation(v);
        //finish();
    }


    /**
     * cancel Request onClick
     * @param v view
     */
    public void cancelRequest(View v) {
        DatabaseReference sentReqRef = mDatabase.getReference("RequestsSentByUser").child(borrowerId).child(requestId);
        sentReqRef.removeValue();

        DatabaseReference bookReqRef = mDatabase.getReference("RequestsReceivedByBook").child(bookId).child(requestId).child("status");
        bookReqRef.setValue("Cancelled");

        setToAvailableIfNoRequests();

        // delete notification
        final DatabaseReference notificationRef = mDatabase.getReference("Notifications").child(ownerId);
        ValueEventListener notificationListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren() ){
                    Notification n = child.getValue(Notification.class);
                    if (n.getTransactionId().equals(requestId)) {
                        notificationRef.child(child.getKey()).removeValue();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("cancelled", databaseError.toException());
            }
        };
        notificationRef.addListenerForSingleValueEvent(notificationListener);
        finish();
    }

    /**
     * set to available if there are no outstanding requests for a book
     */
    public void setToAvailableIfNoRequests() {
        DatabaseReference bookReqRef1 = mDatabase.getReference("RequestsReceivedByBook").child(bookId);
        ValueEventListener bookReqListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    DatabaseReference bookRef = mDatabase.getReference("Books").child(bookId).child("status");
                    bookRef.setValue("AVAILABLE");
                }
                else {
                    boolean allCancelled = true;
                    for(DataSnapshot child : dataSnapshot.getChildren() ){
                        Request r = child.getValue(Request.class);
                        if (!r.getStatus().equals("Cancelled")) {
                            allCancelled = false;
                            break;
                        }
                    }
                    if (allCancelled) {
                        DatabaseReference bookRef = mDatabase.getReference("Books").child(bookId).child("status");
                        bookRef.setValue("AVAILABLE");
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("cancelled", databaseError.toException());
            }
        };
        bookReqRef1.addListenerForSingleValueEvent(bookReqListener);
    }

}
