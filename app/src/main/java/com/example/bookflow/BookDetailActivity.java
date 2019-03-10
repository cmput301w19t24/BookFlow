package com.example.bookflow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookflow.Model.Book;
import com.example.bookflow.Model.Request;
import com.example.bookflow.Model.Notification;
import com.example.bookflow.Model.TestRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookDetailActivity extends BasicActivity {
    private TextView titleField;
    private TextView authorField;
    private TextView isbnField;
    private TextView statusField;

    private String book_id;
    private String owner_id;
    private String borrower_id;
    private String title;
    private String author;
    private String isbn;
    private String status;
    private String borrower_name;
    private String username;

    private FirebaseDatabase mDatabase;
    private DatabaseReference requestRef;
    private DatabaseReference notificationRef;


    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        // retreives book_id passed from SearchActivity
        Bundle extras = getIntent().getExtras();
        //book_id = extras.getString("book_id");
        //book_id = "-L_UVcRjH_7D3MHna9Oe";
        //owned by me
        //book_id = "-L_WfB7zG6uE4QHbecb1";
        //owned by a@a
        book_id = "-L__rwKWq_t63ywH4Rk1";

        mDatabase = FirebaseDatabase.getInstance();
        notificationRef = mDatabase.getReference("Notifications");
        requestRef = mDatabase.getReference("Requests");

        titleField = findViewById(R.id.bookName);
        authorField = findViewById(R.id.author);
        isbnField = findViewById(R.id.isbn);
        statusField = findViewById(R.id.status);

        mAuth = FirebaseAuth.getInstance();


        ValueEventListener bookListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                title = dataSnapshot.child("title").getValue().toString();
                author = dataSnapshot.child("author").getValue().toString();
                isbn = dataSnapshot.child("isbn").getValue().toString();
                //status = dataSnapshot.child("status").getValue().toString();
                owner_id = dataSnapshot.child("ownerId").getValue().toString();
                //Log.e("ahhhhhh", owner_id);

                titleField.setText(title);
                authorField.setText(author);
                isbnField.setText(isbn);
                //statusField.setText(status);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("cancelled", databaseError.toException());
            }
        };
        mDatabase.getReference().child("Books").child(book_id).addListenerForSingleValueEvent(bookListener);

    }

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
                    requestReference.child(request_id).setValue(new TestRequest(owner_id, borrower_id, book_id));

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

}
