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

import com.bumptech.glide.Glide;
import com.example.bookflow.Model.Book;
import com.example.bookflow.Model.Notification;
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
    private ListView notifListview;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notification);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String user_id = user.getUid();

        DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference().child("Notifications").child(user_id);

        notifListview = (ListView) findViewById(R.id.recyclerView);
        Query query = database.getReference().child("Books");


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

    private void notifList() {
        myBookList = (ListView) findViewById(R.id.myBookList);
        Query query = database.getReference().child("Books");
        FirebaseListOptions<Book> options = new FirebaseListOptions.Builder<Book>()
                .setLayout(R.layout.main_listitem)
                .setLifecycleOwner(MainActivity.this)
                .setQuery(query,Book.class).build();
        adapterBook = new FirebaseListAdapter<Book>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Book model, int position) {
                TextView mauthor = v.findViewById(R.id.iauthor);
                TextView mtitle = v.findViewById(R.id.ititle);
                ImageView mphoto = v.findViewById(R.id.iphoto);

                Book book = (Book) model;
                mauthor.setText(book.getAuthor());
                mtitle.setText(book.getTitle());
                Glide.with(MainActivity.this).load(book.getPhotoUri()).into(mphoto);
            }

//            @Override
//            public int getCount() {
//                return 3;
//            }
        };
        myBookList.setAdapter(adapterBook);
    }
}
