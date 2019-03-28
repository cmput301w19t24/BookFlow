package com.example.bookflow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class RequestDetailActivity extends BasicActivity {
    public static final String PARAMETERS= "com.example.bookflow.MESSAGE";
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
        ownerId = extras.getString("ownerId");
        bookId = extras.getString("bookId");
        borrowerId = extras.getString("borrowerId");
        status = extras.getString("status");
        requestId = extras.getString("requestId");

        DatabaseReference dbRef = mDatabase.getReference("Books").child(bookId);

        bookLabel.setText("Book");

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
                    String path = "users/" + borrowerId;
                    StorageReference imageRef = FirebaseStorage.getInstance().getReference().child(path);
                    Glide.with(RequestDetailActivity.this)
                            .load(imageRef).into(userIcon);
                    DatabaseReference userDbRef =  mDatabase.getReference("Users").child(borrowerId);
                    ValueEventListener userListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String borrowerUsername = dataSnapshot.child("username").getValue().toString();
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
        dbRef.addListenerForSingleValueEvent(bookListener);
    }

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
        finish();

    }

    public void accept(View v) {
        Intent intent = new Intent(this, MapsActivity.class);
        ArrayList<String> infos = new ArrayList<>();
        infos.add(this.ownerId);
        infos.add(this.bookId);
        infos.add(this.status);
        infos.add(this.requestId);
        intent.putExtra(PARAMETERS, infos);
        startActivity(intent);
    }
}
