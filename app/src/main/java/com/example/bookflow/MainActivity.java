package com.example.bookflow;
import com.bumptech.glide.Glide;
import com.example.bookflow.Model.Book;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity extends BasicActivity {

    private static ListView myBookList;
    private static ListView myBorrowList;
    private FirebaseListAdapter adapterBook;
    private FirebaseListAdapter adapterBorrow;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        bookList();
        borrowList();
    }

    private void bookList() {
        myBookList = (ListView) findViewById(R.id.myBookList);
        Query query = database.getReference().child("Users").child(uid).child("myBooks");
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
        };
        myBookList.setAdapter(adapterBook);
    }

    private void borrowList() {
        myBorrowList = (ListView) findViewById(R.id.myBorrowList);
        Query query = database.getReference().child("Users").child(uid).child("myBorrows");
        FirebaseListOptions<Book> options = new FirebaseListOptions.Builder<Book>()
                .setLayout(R.layout.main_listitem)
                .setLifecycleOwner(MainActivity.this)
                .setQuery(query,Book.class).build();
        adapterBorrow = new FirebaseListAdapter<Book>(options) {
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
        };
        myBorrowList.setAdapter(adapterBorrow);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        adapterBook.startListening();
//
//        // TODO: delete after testing firebase
////        DatabaseReference myRef = database.getReference("Books");
////        myRef.child("Author").setValue("1", null);
//
//
//    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        adapterBook.stopListening();
//    }

    public void seeMoreBooks(View moreBookView) {
        Intent intent_booklist = new Intent(this, BookListActivity.class);
        startActivity(intent_booklist);
    }

    public void seeMoreBorrows(View moreBorrowView) {
        Intent intent_borrowlist = new Intent(this, BorrowListActivity.class);
        startActivity(intent_borrowlist);
    }
}