/**
 * MainActivity Class
 *
 * Copyright 2019 Shengyao Lu
 *
 * @author shengyao
 * @version 1.0
 * @created 2019-03-01
 */
package com.example.bookflow;
import com.bumptech.glide.Glide;
import com.example.bookflow.Model.Book;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

/**
 * Main Activity Class
 */
public class MainActivity extends BasicActivity {

    private static ListView myBookList;
    private MyAdapter adpBook;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private String uid;
    private Query query;

    ArrayList<Book> books;
    ArrayList<Book> nonfiltered_books;
    boolean firstgrab = true;
    ArrayList<Book> filtered_books;

    // class of Adapter
    class MyAdapter extends ArrayAdapter<Book> {
        MyAdapter(Context c, ArrayList<Book> books) {
            super(c,R.layout.main_listitem, books);
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {
            Book book = this.getItem(position);

            if (v == null) {
                v = LayoutInflater.from(getContext()).inflate(R.layout.main_listitem, parent, false);
            }
            TextView mauthor = v.findViewById(R.id.iauthor);
            TextView mtitle = v.findViewById(R.id.ititle);
            ImageView mphoto = v.findViewById(R.id.iphoto);

            mauthor.setText(book.getAuthor());
            mtitle.setText(book.getTitle());
            Glide.with(MainActivity.this).load(book.getPhotoUri()).into(mphoto);
            return v;
        }
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        query = database.getReference().child("Books");

        books = new ArrayList<Book>();
        filtered_books = new ArrayList<Book>();
        nonfiltered_books = new ArrayList<Book>();
        adpBook = new MyAdapter(this,books);
        bookList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
        item.setChecked(!item.isChecked());

        switch (item.getItemId()) {
            case R.id.check_available:
                if (item.isChecked()) {
                    addToList(Book.BookStatus.AVAILABLE);
                } else {
                    removeFromList(Book.BookStatus.AVAILABLE);
                }
                return true;
            case R.id.check_accepted:
                if (item.isChecked()) {
                    addToList(Book.BookStatus.ACCEPTED);
                } else {
                    removeFromList(Book.BookStatus.ACCEPTED);
                }
                return true;
            case R.id.check_requested:
                if (item.isChecked()) {
                    addToList(Book.BookStatus.REQUESTED);
                } else {
                    removeFromList(Book.BookStatus.REQUESTED);
                }
                return true;
            case R.id.check_borrowed:
                if (item.isChecked()) {
                    addToList(Book.BookStatus.BORROWED);
                } else {
                    removeFromList(Book.BookStatus.BORROWED);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addToList(Book.BookStatus status) {
        filtered_books = (ArrayList<Book>)books.clone();
        for (int i=0; i<nonfiltered_books.size(); i++) {
            Book book = nonfiltered_books.get(i);
            if (book.getStatus().equals(status)
                    || book.getStatus().toString().equals(status.toString())) {
                if(!filtered_books.contains(book))
                    adpBook.add(book);
            }
        }
        myBookList.setAdapter(adpBook);
    }

    private void removeFromList(Book.BookStatus status) {
        if (firstgrab) {
            nonfiltered_books = (ArrayList<Book>) books.clone();
            firstgrab = false;
        }
        filtered_books = (ArrayList<Book>)books.clone();
        for (int i=0; i<filtered_books.size(); i++) {
            Book book = filtered_books.get(i);
            if (book.getStatus().equals(status)
                || book.getStatus().toString().equals(status.toString())) {
                if(filtered_books.contains(book))
                    adpBook.remove(book);
            }
        }
        myBookList.setAdapter(adpBook);
    }



    /**
     * view book list method
     */
    private void bookList() {
        myBookList = (ListView) findViewById(R.id.myBookList);
        // add user's book to adapter
        query.orderByChild("ownerId").equalTo(uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                System.out.println("Added " + " " +dataSnapshot.getValue());
                Book book = (Book)dataSnapshot.getValue(Book.class);
                if (!books.contains(book)) {
                    adpBook.add(book);
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        myBookList.setAdapter(adpBook);
        myBookList.setClickable(true);
        myBookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * @param parent
             * @param view
             * @param position
             * @param id
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent_detail = new Intent(MainActivity.this, BookDetailActivity.class);
                String book_id = adpBook.getItem(position).getBookId();
                intent_detail.putExtra("book_id",book_id);
                startActivity(intent_detail);
            }
        });
        nonfiltered_books = (ArrayList<Book>) books.clone();
    }

    public void seeBorrowList(View borrowlistV) {
        Intent intent_borrowlist = new Intent(this, BorrowListActivity.class);
        intent_borrowlist.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent_borrowlist);
    }
}
