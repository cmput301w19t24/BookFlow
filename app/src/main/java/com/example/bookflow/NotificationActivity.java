package com.example.bookflow;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookflow.Model.InAppNotif;
import com.example.bookflow.Model.Notification;
import com.example.bookflow.Model.User;
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
 * This class is for the notifications page
 * received accept or request notifications
 * are displayed in a recycler view, along
 * with the senders icon
 */
public class NotificationActivity extends BasicActivity {
    private FirebaseAuth mAuth;
    private FirebaseRecyclerAdapter<Notification, NotificationHolder> myFirebaseRecyclerAdapter;
    private InAppNotif notif = InitActivity.getNotif();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notification);

        //REMOVE
        TextView tempMyReqButton = findViewById(R.id.tempMySentRequests);
        tempMyReqButton.setVisibility(View.VISIBLE);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String user_id = user.getUid();

        DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference().child("Notifications").child(user_id);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true); // probably don't need this

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);

        recyclerView.setLayoutManager(llm);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                llm.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);



        FirebaseRecyclerOptions<Notification> options =
                new FirebaseRecyclerOptions.Builder<Notification>()
                        .setQuery(notificationRef, Notification.class)
                        .build();

        myFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Notification, NotificationHolder>(options) {
            @Override
            public NotificationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.notification_list_item, parent, false);
                NotificationHolder nh = new NotificationHolder(view);
                nh.setOnClickListener(new NotificationHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        final DatabaseReference dbRef = myFirebaseRecyclerAdapter.getRef(position);
                        ValueEventListener notificationListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //String notificationId = dbRef.getKey();
                                Notification n = dataSnapshot.getValue(Notification.class);
                                String requestId = n.getTransactionId();
                                String bookId = n.getBookId();
                                String type = n.getType();
                                dbRef.child("viewed").setValue("true");

                                if (type.equals("request")) {
                                    Intent intent = new Intent(NotificationActivity.this, RequestDetailActivity.class);
                                    intent.putExtra("requestId", requestId);
                                    intent.putExtra("bookId", bookId);
                                    startActivity(intent);
                                }
                                else if (type.equals("accept")){
                                    Intent intent = new Intent(NotificationActivity.this, BookDetailActivity.class);
                                    intent.putExtra("book_id", bookId);
                                    startActivity(intent);
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w("cancelled", databaseError.toException());
                            }
                        };
                        dbRef.addListenerForSingleValueEvent(notificationListener);
                    }
                });
                return nh;
            }

            @Override
            protected void onBindViewHolder(final NotificationHolder holder, int position, Notification model) {
                String outString = "";
                String sender = model.getSenderName();
                String book = model.getBookTitle();
                String senderId = model.getSenderId();
                String path = "users/" + senderId;
                String timestamp = model.getTimestamp();
                String viewed = model.getViewed();

                FirebaseDatabase.getInstance()
                        .getReference("Users")
                        .child(senderId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User currUser = dataSnapshot.getValue(User.class);
                                if (currUser.getImageurl() != null) {
                                    holder.setNotificationSenderIcon(currUser.getImageurl());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                if(viewed.equals("true")){
                    holder.itemView.setBackgroundColor(Color.parseColor("#CCE8FF"));
                }

                if (model.getType().equals("request")) {
                    holder.setNotificationType("Book Request");
                    outString = sender + " has requested " + "\"" + book + "\"";
                }
                else if (model.getType().equals("accept")){
                    holder.setNotificationType("Request Accepted");
                    outString = sender + " has accepted your request for \"" + book + "\"" ;
                }
                holder.setNotificationText(outString);
//                holder.setNotificationSenderIcon(imageRef);
                holder.setNotificationTimestamp(timestamp);
            }
        };
        recyclerView.setAdapter(myFirebaseRecyclerAdapter);
    }
    /**
     * onStart method
     * sets up onclick listener on notification list item
     */
    @Override
    protected void onStart() {
        super.onStart();
        findViewById(R.id.notification_button).setBackgroundResource(R.drawable.notif_select);
        myFirebaseRecyclerAdapter.startListening();
        notif.setFirstIn(true);
        notif.setNotif_count(0);
        InitActivity.pushData(mAuth.getCurrentUser().getUid());
    }
    /**
     * onStop method
     * removes onclick listener on notification list item
     */
    @Override
    protected void onStop() {
        super.onStop();

        if (myFirebaseRecyclerAdapter!= null) {
            myFirebaseRecyclerAdapter.stopListening();
        }
    }



    /**
     * tempMySentRequests method
     * onclick callback method, goes to sent requests list activity
     */
    public void tempMySentRequests(View v) {
        Intent intent = new Intent(NotificationActivity.this, SentRequestsListActivity.class);
        startActivity(intent);
    }
}
