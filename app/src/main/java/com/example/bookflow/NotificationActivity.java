package com.example.bookflow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookflow.Model.Notification;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class NotificationActivity extends BasicActivity {
    private FirebaseAuth mAuth;
    private FirebaseRecyclerAdapter<Notification, NotificationHolder> myFirebaseRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notification);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String user_id = user.getUid();

        DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference().child("Notifications").child(user_id);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        LinearLayoutManager llm = new LinearLayoutManager(this);

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
                // Bind the image_details object to the BlogViewHolder
                // ...
                String outString = "";
                String sender = model.getSenderName();
                String book = model.getBookTitle();
                if (model.getType().equals("request")) {
                    holder.setNotificationType("Book Request");
                    outString = sender + " has requested " + "\"" + book + "\"";
                }
                holder.setNotificationText(outString);
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



//        FirebaseRecyclerAdapter<com.example.bookflow.Model.Notification, NotificationHolder> adapter = new FirebaseRecyclerAdapter<com.example.bookflow.Model.Notification, NotificationHolder>(com.example.bookflow.Model.Notification.class, R.layout.notification_list_item, NotificationHolder.class, notificationRef) {
//            @Override
//            protected void populateViewHolder(NotificationHolder viewHolder, Notification model, int position) {
//                String outString = "";
//                String sender = model.getSenderName();
//                String book = model.getBookTitle();
//                if (model.getType().equals("request")) {
//                    viewHolder.setNotificationType("Book Request");
//                    outString = sender + " has requested " + "\"" + book + "\"";
//                }
//
//                viewHolder.setNotificationText(outString);
//            }
//        };
//        recyclerView.setAdapter(adapter);


}
