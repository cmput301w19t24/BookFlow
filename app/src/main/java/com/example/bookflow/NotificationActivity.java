package com.example.bookflow;

import com.example.bookflow.Model.Notification;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bookflow.Model.Book;
import com.example.bookflow.Model.Notification;
import com.example.bookflow.Model.Request;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class NotificationActivity extends BasicActivity {
    private FirebaseAuth mAuth;
    private ListView recyclerView;
    private FirebaseListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notification);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String user_id = user.getUid();

        Query query = FirebaseDatabase.getInstance().getReference().child("Notifications").child(user_id);
        FirebaseListOptions<Notification> options = new FirebaseListOptions.Builder<Notification>()
                .setLayout(R.layout.notification_list_item)
                .setLifecycleOwner(NotificationActivity.this)
                .setQuery(query, Notification.class).build();

        recyclerView = (ListView) findViewById(R.id.recyclerView);
        adapter = new FirebaseListAdapter<Notification>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Notification model, int position) {
                String sender = model.getSender_name();
                String book = model.getBook_title();
                TextView text = v.findViewById(R.id.notification_text);
                TextView type = v.findViewById(R.id.notification_type);

                if (model.getType().equals("request")) {
                    text.setText(sender + " has requested " + "\"" + book + "\"");
                    type.setText("Book Request");
                } else {
                    text.setText("");
                }

//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        LinearLayoutManager llm = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(llm);
//        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
//                llm.getOrientation());
//        recyclerView.addItemDecoration(mDividerItemDecoration);
//        FirebaseRecyclerAdapter<Notification, NotificationHolder> adapter = new FirebaseRecyclerAdapter<com.example.bookflow.Model.Notification, NotificationHolder>(com.example.bookflow.Model.Notification.class, R.layout.notification_list_item, NotificationHolder.class, notificationRef) {
//            @Override
//            protected void populateViewHolder(NotificationHolder viewHolder, Notification model, int position) {
//                String outString = "";
//                String sender = model.getSender_name();
//                String book = model.getBook_title();
//                if (model.getType().equals("request")) {
//                    viewHolder.setNotificationType("Book Request");
//                    outString = sender + " has requested " + "\"" + book + "\"";
//                }
//                viewHolder.setNotificationText(outString);
//            }
//        };
                recyclerView.setAdapter(adapter);
            }
        };
    }

}
