package com.example.bookflow;
import com.bumptech.glide.Glide;
import com.example.bookflow.Model.Book;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends BasicActivity {

    private static ListView myBookList;
    private static ListView myBorrowList;
    private MyAdapter adpBook;
    private FirebaseListAdapter adapterBook;
    private FirebaseListAdapter adapterBorrow;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private String uid;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        ArrayList<Book> books = new ArrayList<>();
        adpBook = new MyAdapter(this,books);

        bookList();
        borrowList();
    }

    private void bookList() {
        myBookList = (ListView) findViewById(R.id.myBookList);
//        Query query = database.getReference().child("Users").child(uid).child("myBooks");

        Query query = database.getReference().child("Books");
        query.orderByChild("ownerId").equalTo(uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                System.out.println("Added " + " " +dataSnapshot.getValue());
                adpBook.add(dataSnapshot.getValue(Book.class));
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
        FirebaseListOptions<Book> options = new FirebaseListOptions.Builder<Book>()
                .setLayout(R.layout.main_listitem)
                .setLifecycleOwner(MainActivity.this)
                .setQuery(query,Book.class).build();

//        FirebaseRecyclerAdapter;
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
//        myBookList.setAdapter(adapterBook);
//        adpBook.notifyDataSetChanged();
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
                intent_detail.putExtra("position",position);
                startActivity(intent_detail);
            }
        });
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