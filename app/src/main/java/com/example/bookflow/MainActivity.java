package com.example.bookflow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends BasicActivity {

    private static ListView myBookList;
    private static ListView myBorrowList;

//    private static MyAdapter adapter;

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myBookList = (ListView) findViewById(R.id.myBookList);
        myBorrowList = (ListView) findViewById(R.id.myBorrowList);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        adapter = new MyAdapter();
        DatabaseReference myRef = database.getReference("Books");
        myRef.child("Author").setValue("", null);

    }

    public void seeMoreBooks(View moreBookView) {
        Intent intent_booklist = new Intent(this, BookListActivity.class);
        startActivity(intent_booklist);
    }

    public void seeMoreBorrows(View moreBorrowView) {
        Intent intent_borrowlist = new Intent(this, BorrowListActivity.class);
        startActivity(intent_borrowlist);
    }
}
