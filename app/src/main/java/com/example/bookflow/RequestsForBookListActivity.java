package com.example.bookflow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bookflow.Model.Notification;
import com.example.bookflow.Model.Request;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class displays the requests received by a particular
 * book
 */

public class RequestsForBookListActivity extends BasicActivity {
    private FirebaseAuth mAuth;
    private String photoUri;
    private FirebaseDatabase mDatabase;
    private FirebaseRecyclerAdapter<Request, RequestHolder> myFirebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notification);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        //String bookId = "-L_dvKIh04uGafLdQzh9";
        Bundle extras = getIntent().getExtras();
        final String bookId = extras.getString("bookid");

        DatabaseReference requestsByBookReference = mDatabase.getReference("RequestsReceivedByBook").child(bookId);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);

        recyclerView.setLayoutManager(llm);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                llm.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);

        FirebaseRecyclerOptions<Request> options =
                new FirebaseRecyclerOptions.Builder<Request>()
                        .setQuery(requestsByBookReference, Request.class)
                        .build();

        mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference userRef = mDatabase.getReference("Books");

        ValueEventListener bookListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView bookTitleView = findViewById(R.id.notification_header_text);
                String bookTitle = dataSnapshot.child(bookId).child("title").getValue().toString();
                bookTitleView.setText("Requests for \"" + bookTitle + "\"");
                //Log.e("help", requesterName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("cancelled", databaseError.toException());
            }
        };
        userRef.addListenerForSingleValueEvent(bookListener);

        myFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Request, RequestHolder>(options) {
            @Override
            public RequestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.request_list_item, parent, false);
                RequestHolder vh = new RequestHolder(view);
                vh.setOnClickListener(new RequestHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        final DatabaseReference dbRef = myFirebaseRecyclerAdapter.getRef(position);
                        //String requestId = dbRef.getKey();
                        //intent.putExtra("requestId", requestId);
                        //startActivity(intent);
                        ValueEventListener requestListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String requestId = dbRef.getKey();

                                String ownerId = dataSnapshot.child("ownerId").getValue().toString();
                                String borrowerId = dataSnapshot.child("borrowerId").getValue().toString();
                                String bookId = dataSnapshot.child("bookId").getValue().toString();
                                String status = dataSnapshot.child("status").getValue().toString();

                                Intent intent = new Intent(RequestsForBookListActivity.this, RequestDetailActivity.class);
                                intent.putExtra("ownerId", ownerId);
                                intent.putExtra("borrowerId", borrowerId);
                                intent.putExtra("bookId", bookId);
                                intent.putExtra("status", status);
                                intent.putExtra("requestId", requestId);
                                startActivity(intent);
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w("cancelled", databaseError.toException());
                            }
                        };
                        dbRef.addListenerForSingleValueEvent(requestListener);
                    }
                });
                return vh;
            }

            @Override
            protected void onBindViewHolder(RequestHolder holder, int position, Request model) {
                String outString = "";

                String requesterId = model.getBorrowerId();
                String bookId = model.getBookId();
                String ownerId = model.getOwnerId();
                String status = model.getStatus();
                String path = "users/" + requesterId;
                StorageReference imageRef = FirebaseStorage.getInstance().getReference().child(path);


                //holder.setRequesterName(requesterID);
                holder.setRequesterIcon(imageRef);
                //holder.setBookTitle(bookId);
                //holder.setRequestText("has requested");
                holder.setRequestItemText(ownerId, bookId, requesterId, "received", status);
                holder.setStatus(status);

            }
        };
        recyclerView.setAdapter(myFirebaseRecyclerAdapter);



    }

    @Override
    protected void onStart() {
        super.onStart();
        myFirebaseRecyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (myFirebaseRecyclerAdapter!= null) {
            myFirebaseRecyclerAdapter.stopListening();
        }
    }
}
