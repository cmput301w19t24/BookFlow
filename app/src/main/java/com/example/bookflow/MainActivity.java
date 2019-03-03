package com.example.bookflow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

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
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("Hello, World!", null);
    }
}
