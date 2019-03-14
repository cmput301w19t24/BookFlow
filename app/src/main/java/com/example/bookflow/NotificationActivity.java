package com.example.bookflow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.bookflow.Model.Notification;
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
    private String photoUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notification);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String user_id = user.getUid();

        DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference().child("Notifications").child(user_id);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

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
                return new NotificationHolder(view);
            }

            @Override
            protected void onBindViewHolder(NotificationHolder holder, int position, Notification model) {
                String outString = "";
                String sender = model.getSenderName();
                String book = model.getBookTitle();
                String sender_id = model.getSenderId();
                String path = "users/" + sender_id;
                String timestamp = model.getTimestamp();
                StorageReference imageRef = FirebaseStorage.getInstance().getReference().child(path);

                if (model.getType().equals("request")) {
                    holder.setNotificationType("Book Request");
                    outString = sender + " has requested " + "\"" + book + "\"";
                }
                holder.setNotificationText(outString);
                holder.setNotificationSenderIcon(imageRef);
                holder.setNotificationTimestamp(timestamp);
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
