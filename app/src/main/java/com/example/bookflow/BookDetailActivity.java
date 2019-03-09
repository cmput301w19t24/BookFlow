package com.example.bookflow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.bookflow.Model.Book;
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
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        // retreives book_id passed from SearchActivity
        Bundle extras = getIntent().getExtras();
        book_id = extras.getString("book_id");
        //book_id = "-L_Vhzx3FIpaIdDqjmOO"
        mDatabase = FirebaseDatabase.getInstance().getReference();

        titleField = findViewById(R.id.bookName);
        authorField = findViewById(R.id.author);
        isbnField = findViewById(R.id.isbn);
        statusField = findViewById(R.id.status);


        ValueEventListener bookListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String title = dataSnapshot.child("title").getValue().toString();
                String author = dataSnapshot.child("author").getValue().toString();
                String isbn = dataSnapshot.child("isbn").getValue().toString();
                //String status = dataSnapshot.child("status").getValue().toString();


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
        mDatabase.child("Books").child(book_id).addListenerForSingleValueEvent(bookListener);

    }

    public void request(View v) {
        Log.e("hello", "hello");
    }

}
