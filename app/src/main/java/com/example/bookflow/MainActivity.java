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
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import com.example.bookflow.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;

/**
 * Main Activity Class
 */
public class MainActivity extends BasicActivity {

    private static final String TAG = "MainActivity";

    private static ListView myBookList;
    private static ListView myBorrowList;
    private MyAdapter adpBook;
    private MyAdapter adpBorrow;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private String uid;
    private Query query;

    private ArrayList<Book> books;
    private ArrayList<Book> nonfiltered_books;
    private ArrayList<Book> filtered_books;
    private boolean firstgrab;
    private ArrayList<Book> borrows;
    private  ArrayList<Book> nonfiltered_borrows;
    private ArrayList<Book> filtered_borrows;


    /**
     * adapter class that combines book with view
     */
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
            TextView mstatus = v.findViewById(R.id.istatus);
            TextView mtitle = v.findViewById(R.id.ititle);
            ImageView mphoto = v.findViewById(R.id.iphoto);

            // get userID to display
            String userID = "null";
            if (parent == findViewById(R.id.myBookList)) {
                userID = String.valueOf(book.getBorrowerId());
                if(book.getStatus().toString().equals("ACCEPTED")) {
                    ((TextView)v.findViewById(R.id.iby2)).setText("to");
                }
            } else if (parent == findViewById(R.id.myBorrowList)){
                userID = String.valueOf(book.getOwnerId());
                if(book.getStatus().toString().equals("BORROWED")) {
                    ((TextView)v.findViewById(R.id.iby2)).setText("from");
                }
            }
            if (!userID.equals("null")) {
                setUser(v, userID);
            }

            mauthor.setText(book.getAuthor());
            mstatus.setText(book.getStatus().toString());
            mtitle.setText(book.getTitle());
            Glide.with(MainActivity.this).load(book.getPhotoUri()).into(mphoto);

            return v;
        }
    }

    /**
     * Set the borrower or owner of the book when applicable
     * @param v view
     * @param tmpuid temporary user ID
     */
    private void setUser (final View v, final String tmpuid) {
        database.getReference().child("Users").child(tmpuid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = (User) dataSnapshot.getValue(User.class);
                TextView rname = v.findViewById(R.id.iuser);
                TextView rby = v.findViewById(R.id.iby2);
                TextView status = v.findViewById(R.id.istatus);
                // only accepted books and borrowed books are applicable
                if (status.getText().toString().equals("ACCEPTED") ||
                        status.getText().toString().equals("BORROWED")) {
                    rname.setText(user.getUsername());
                    rby.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    /**
     * onCreate activity
     * @param savedInstanceState saved instance state
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

        // books declarations
        books = new ArrayList<Book>();
        filtered_books = new ArrayList<Book>();
        nonfiltered_books = new ArrayList<Book>();
        adpBook = new MyAdapter(this,books);

        // borrows declarations
        borrows = new ArrayList<Book>();
        filtered_borrows = new ArrayList<Book>();
        nonfiltered_borrows = new ArrayList<Book>();
        adpBorrow = new MyAdapter(this, borrows);

        // add firebase token to user
        final DatabaseReference currUserRef = database.getReference("Users").child(uid);
        currUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseInstanceId.getInstance()
                        .getInstanceId()
                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (task.isSuccessful()) {
                                    // Get new Instance ID token
                                    String token = task.getResult().getToken();

                                    Log.i(TAG, "ntfctn token = " + token);

                                    currUserRef.child("notificationToken")
                                            .setValue(token);
                                }
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    /**
     * on start activity
     */
    @Override
    protected void onStart() {
        super.onStart();

        // UI
        findViewById(R.id.main_page_button).setBackgroundResource(R.drawable.home_select);
        // initialization
        books.clear();
        borrows.clear();
        firstgrab = true;
        // load lists
        bookList();
        borrowList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * When a checkbox is toggled, load the list view accordingly
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

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

    /**
     * Add the books of the specific status to the listview
     * @param status
     */
    private void addToList(Book.BookStatus status) {
        // if the boolean is set to true re-copy the nonfiltered arrays
        if (firstgrab) {
            nonfiltered_books = (ArrayList<Book>) books.clone();
            nonfiltered_borrows = (ArrayList<Book>) borrows.clone();
            firstgrab = false;
        }
        // get current book list
        filtered_books = (ArrayList<Book>)books.clone();
        // go through all the books of the user and add corresponding books to the book list
        for (int i=0; i<nonfiltered_books.size(); i++) {
            Book book = nonfiltered_books.get(i);
            if (book.getStatus().equals(status)
                    || book.getStatus().toString().equals(status.toString())) {
                if(!filtered_books.contains(book))
                    adpBook.add(book);
            }
        }
        myBookList.setAdapter(adpBook);

        // get current borrow list
        filtered_borrows = (ArrayList<Book>)borrows.clone();
        // go through all the borrows of the user and add corresponding books to the borrows list
        for (int i=0; i<nonfiltered_borrows.size(); i++) {
            Book book = nonfiltered_borrows.get(i);
            if (book.getStatus().equals(status)
                    || book.getStatus().toString().equals(status.toString())) {
                if(!filtered_borrows.contains(book))
                    adpBorrow.add(book);
            }
        }
        myBorrowList.setAdapter(adpBorrow);
    }

    /**
     * Remove the books of the specific status to the listview
     * @param status
     */
    private void removeFromList(Book.BookStatus status) {
        if (firstgrab) {
            nonfiltered_books = (ArrayList<Book>) books.clone();
            nonfiltered_borrows = (ArrayList<Book>) borrows.clone();
            firstgrab = false;
        }

        // remove the books of the user which are not of the specific status
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

        // remove the borrows of the user which are not of the specific status
        filtered_borrows = (ArrayList<Book>)borrows.clone();
        for (int i=0; i<filtered_borrows.size(); i++) {
            Book book = filtered_borrows.get(i);
            if (book.getStatus().equals(status)
                    || book.getStatus().toString().equals(status.toString())) {
                if(filtered_borrows.contains(book))
                    adpBorrow.remove(book);
            }
        }
        myBorrowList.setAdapter(adpBorrow);
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
                Book book = (Book)dataSnapshot.getValue(Book.class);
                if (!books.contains(book)) {
                    adpBook.add(book);
                    // a boolean helps us to decide if we want to
                    // re-copy the retrieved data from the firebase to the non-filtered books
                    firstgrab = true;
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Book book = (Book)dataSnapshot.getValue(Book.class);
                String bookID = book.getBookId();
                for (int i=0; i<books.size(); i++) {
                    if (bookID.equals(books.get(i).getBookId())) {
                        books.remove(books.get(i));
                    }
                }
                books.add(book);
                myBookList.setAdapter(adpBook);
                firstgrab = true;
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Book book = (Book)dataSnapshot.getValue(Book.class);
                String bookID = book.getBookId();
                for (int i=0; i<books.size(); i++) {
                    if (bookID.equals(books.get(i).getBookId())) {
                        books.remove(books.get(i));
                    }
                }
                myBookList.setAdapter(adpBook);
                firstgrab = true;
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        myBookList.setAdapter(adpBook);
        myBookList.setClickable(true);
        myBookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent_detail = new Intent(MainActivity.this, BookDetailActivity.class);
                String book_id = adpBook.getItem(position).getBookId();
                intent_detail.putExtra(BookDetailActivity.INTENT_EXTRA, book_id);
                startActivity(intent_detail);
            }
        });
    }

    /**
     * view book list method
     */
    private void borrowList() {
        myBorrowList = (ListView) findViewById(R.id.myBorrowList);
        // add user's book to adapter
        query.orderByChild("borrowerId").equalTo(uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Book book = (Book)dataSnapshot.getValue(Book.class);
                if (!borrows.contains(book)) {
                    // add a book to the adapter
                    adpBorrow.add(book);
                    // a boolean helps us to decide if we want to
                    // re-copy the retrieved data from the firebase to the non-filtered borrows
                    firstgrab = true;
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Book borrow = (Book)dataSnapshot.getValue(Book.class);
                String bookID = borrow.getBookId();
                for (int i=0; i<borrows.size(); i++) {
                    if (bookID.equals(borrows.get(i).getBookId())) {
                        borrows.remove(borrows.get(i));
                    }
                }
                borrows.add(borrow);
                myBorrowList.setAdapter(adpBorrow);
                firstgrab = true;
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Book borrow = (Book)dataSnapshot.getValue(Book.class);
                String bookID = borrow.getBookId();
                for (int i=0; i<borrows.size(); i++) {
                    if (bookID.equals(borrows.get(i).getBookId())) {
                        borrows.remove(borrows.get(i));
                    }
                }
                myBorrowList.setAdapter(adpBorrow);
                firstgrab = true;
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        myBorrowList.setAdapter(adpBorrow);
        myBorrowList.setClickable(true);
        // on click to a list item, go to book detail activity
        myBorrowList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * @param parent
             * @param view
             * @param position
             * @param id
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent_detail = new Intent(MainActivity.this, BookDetailActivity.class);
                String book_id = adpBorrow.getItem(position).getBookId();
                intent_detail.putExtra(BookDetailActivity.INTENT_EXTRA, book_id);
                startActivity(intent_detail);
            }
        });
    }

    /**
     * Toggle the visiablities of the listviews to see the borrowlist
     * @param borrowlistV borrow list view
     */
    public void seeBorrowList(View borrowlistV) {
        findViewById(R.id.myBookList).setVisibility(View.GONE);
        findViewById(R.id.myBorrowList).setVisibility(View.VISIBLE);
        // toggle colors
        findViewById(R.id.toolbar3).setBackgroundResource(R.drawable.border);
        ((TextView)findViewById(R.id.title_myborrow)).setTextColor(getResources().getColor(R.color.colorPrimary));
        findViewById(R.id.toolbar2).setBackgroundResource(R.drawable.border_fill);
        ((TextView)findViewById(R.id.title_mybook)).setTextColor(Color.WHITE);
    }

    /**
     * Toggle the visiablities of the listviews to see the book list
     * @param booklistV book list view
     */
    public void seeBookList(View booklistV) {
        findViewById(R.id.myBookList).setVisibility(View.VISIBLE);
        findViewById(R.id.myBorrowList).setVisibility(View.GONE);
        // toggle colors
        findViewById(R.id.toolbar2).setBackgroundResource(R.drawable.border);
        ((TextView)findViewById(R.id.title_mybook)).setTextColor(getResources().getColor(R.color.colorPrimary));
        findViewById(R.id.toolbar3).setBackgroundResource(R.drawable.border_fill);
        ((TextView)findViewById(R.id.title_myborrow)).setTextColor(Color.WHITE);
    }

    /**
     * when click the refresh button, this function will be called
     * @param refreshV refresh button
     */
    public void refreshPage(View refreshV) {
        finish();
        startActivity(getIntent());
    }
}
